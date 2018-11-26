package demo.service;

import demo.exception.BusinessException;
import demo.model.User;

/**
 * Service of Password reset
 */
public interface PasswordResetService {

	void updatePassword(User user, String newPassword);

	String generatePasswordResetToken(User user);

	void validateToken(long userId, String token) throws BusinessException;

	void sendPasswordResetEmail(User user);
}
