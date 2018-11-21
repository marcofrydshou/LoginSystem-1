package demo.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;

import demo.model.User;
import demo.repository.PasswordTokenRepository;
import demo.repository.RoleRepository;
import demo.repository.UserRepository;
import demo.service.PasswordResetService;

public class PasswordResetImpl implements PasswordResetService {

	private PasswordTokenRepository tokenRepository;
	private PasswordEncoder encoder;
	private UserRepository userRepository;
	private RoleRepository roleRepository;




	@Override public void updatePassword(User user, String newPassword) {

	}

	@Override public String generatePasswordResetToken(User user) {
		return null;
	}

	@Override public void validateToken(long userId, String token) {

	}
}
