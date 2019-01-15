package demo.business;

import demo.exposed.exception.BusinessException;
import demo.data.model.User;

import javax.mail.MessagingException;

/**
 * Service of Password reset
 */
public interface PasswordResetService {

	/**
	 * Create an new instance of token object by the given user and the current time, and return a string of this token
	 * Then create a custom email template
	 * At last, sending the mail to the given email and with the created token and the template
	 *
	 * @param user the user needs to be create the token
	 */
	void sendPasswordResetEmail(User user) throws MessagingException;

	/**
	 * Delete the token belongs to the user in database and reset the password with the new password
	 *
	 * @param user the user used to find the token and to reset the password
	 * @param newPassword the password needs to reset
	 */
	void updatePassword(User user, String newPassword);

	/**
	 * create an instance of token with a random token, current time and the given user
	 * save the created token object in db, and to be used in the sendPasswordResetEmail method
	 *
	 * @param user used to create the token object
	 * @return a random token
	 */
	String generatePasswordResetToken(User user);

	/**
	 * Find the user by the given userId, then find the token related the user or throw an exception
	 * Then check the expiry time and of token match, if all passed
	 * Set "CHANGE_PASSWORD_PRIVILEGE" authority to the user
	 *
	 * @param userId used to find the related user
	 * @param token the validate the token match to the token saved in database
	 * @throws BusinessException if the token invalid
	 */
	void validateToken(long userId, String token) throws BusinessException;

}
