package demo.service.impl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import demo.Util.MailUtil;
import demo.exception.BusinessException;
import demo.model.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import demo.model.PasswordResetToken;
import demo.model.User;
import demo.repository.PasswordTokenRepository;
import demo.repository.RoleRepository;
import demo.repository.UserRepository;
import demo.service.PasswordResetService;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Slf4j
@Service
public class PasswordResetServiceImpl implements PasswordResetService {

	@Value("${app.url.passwordReset}")
	private  String passwordResetUrl;
	private PasswordTokenRepository tokenRepository;
	private PasswordEncoder encoder;
	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private MailUtil mailUtil;

	@Autowired
	public PasswordResetServiceImpl(PasswordTokenRepository tokenRepository, PasswordEncoder encoder, UserRepository userRepository, RoleRepository roleRepository, MailUtil mailUtil) {
		this.tokenRepository = tokenRepository;
		this.encoder = encoder;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.mailUtil = mailUtil;
	}

	@Override
	public void sendPasswordResetEmail(User user) {

		String generatedToken = generatePasswordResetToken(user);

		String emailTemplate = generateResetPasswordEmail(user, generatedToken);

		try {
			mailUtil.sendPasswordResetEmail(user.getEmail(), emailTemplate);

		} catch (MessagingException e) {
			throw new IllegalArgumentException("Could not send to the user with given user information {}");
		}
	}

	@Override
	public String generatePasswordResetToken(User user) {

		// create an instance of PasswordResetToken by given random id, time from now, and the user
		PasswordResetToken resetPasswordResetToken = new PasswordResetToken(UUID.randomUUID().toString(), LocalDateTime.now(), user);

		// save the token in db
		tokenRepository.save(resetPasswordResetToken);

		// return the token
		return resetPasswordResetToken.getToken();
	}

	@Override
	public void validateToken(long userId, String token) throws BusinessException{

		User user = userRepository.findByIdAndEnabledIsTrue(userId).orElseThrow( ()-> new UsernameNotFoundException("User not found with provied ID"));

		PasswordResetToken userToken = tokenRepository.findByUser(user).orElseThrow( ()-> new UsernameNotFoundException("PasswordResetToken not found with the user"));

		LocalDateTime expiryDate = userToken.getExpiryDate();
		long minutes = ChronoUnit.MINUTES.between(expiryDate, LocalDateTime.now());
		if (minutes >= 30) {
			throw new BusinessException("PasswordResetToken has expired.");
		}
		if (userToken.getToken() != null && userToken.getToken().equals(token)) {
			Role passwordRole = roleRepository.findByAuthority("CHANGE_PASSWORD_PRIVILEGE");
			user.addAuthority(passwordRole);
			userRepository.save(user);
		} else {
			log.error("User ({}) found by ID, but token does not match.", user.getUsername());
			throw new BusinessException("Invalid token");
		}
	}

	@Override
	public void updatePassword(User user, String newPassword) {

		// remove the token from db by given user
		tokenRepository.deleteByUser(user);

		// set new encoded password and save in db
		user.setPassword(encoder.encode(newPassword));
		userRepository.save(user);
	}

	private String generateResetPasswordEmail(User user, String token) {
		String EMAIL_RESET_USER_BODY = "<!DOCTYPE html>" +
				"<html>" +
				"  <body>" +
				"  	<p>Kære bruger,</p>" +
				"   <p>Du har bedt om at få ændret din adgangskode til LoginSystem. </p>" +
				"   <p>Følg dette link for at ændre din adgangskode: <a href=\"%s\" target=\"_blank\">Skift adgangskode på LoginSystem</a></p>" +
				"   <p>Med venlig hilsen,<br />" +
				"    <em>LoginSystem</em>" +
				"   </p>" +
				"  </body>" +
				"</html>";
		String endpoint = String.format(passwordResetUrl + "%s/%s", user.getId(), token);

		return String.format(EMAIL_RESET_USER_BODY, endpoint);
	}

}
