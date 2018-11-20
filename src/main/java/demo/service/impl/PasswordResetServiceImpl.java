package demo.service.impl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import demo.model.Role;
import demo.model.Token;
import demo.model.User;
import demo.repository.PasswordTokenRepository;
import demo.repository.RoleRepository;
import demo.repository.UserRepository;
import demo.service.PasswordResetService;

public class PasswordResetServiceImpl implements PasswordResetService {

	private PasswordTokenRepository tokenRepository;
	private PasswordEncoder encoder;
	private UserRepository userRepository;
	private RoleRepository roleRepository;

	@Autowired
	public PasswordResetServiceImpl(PasswordTokenRepository tokenRepository, PasswordEncoder encoder, UserRepository userRepository, RoleRepository roleRepository) {
		this.tokenRepository = tokenRepository;
		this.encoder = encoder;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}

	@Override
	public void updatePassword(User user, String newPassword) {
		tokenRepository.deleteByUser(user);
		user.setPassword(encoder.encode(newPassword));
		userRepository.save(user);
	}

	@Override
	public String generatePasswordResetToken(User user) {
		Token resetToken = new Token(UUID.randomUUID().toString(), LocalDateTime.now(), user);
		tokenRepository.save(resetToken);
		return resetToken.getToken();
	}

	@Override
	public void validateToken(long userId, String token) {
		User user = userRepository.findByIdAndEnabledIsTrue(userId).orElseThrow( ()-> new UsernameNotFoundException("User not found with provied ID"));
		Token userToken = tokenRepository.findByUser(user).orElseThrow( ()-> new UsernameNotFoundException("Token not found with the user"));

		LocalDateTime expireDate = userToken.getExpiryDate();
		if(userToken.getToken()!= null &&userToken.getToken().equals((token))){
			Role passwordRole = roleRepository.findByAuthority("CHANGE_PASSWORD_PRIVILEGE");
		}
	}
}
