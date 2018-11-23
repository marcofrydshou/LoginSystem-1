package demo.service;

import java.util.List;

import demo.exception.DataIntegrityViolationException;
import demo.exception.NoRolesFoundException;
import demo.model.User;
import org.springframework.stereotype.Service;

/**
 * Service interface of User
 */
@Service
public interface UserService  {

	User findUserById(long id);

	/**
	 * Find all enabled users
	 * @return A list of enabled users
	 */
	List<User> findEnabledUsers();

	/**
	 * Create new user with those five params
	 * @param username username of the user
	 * @param password password of the user
	 * @param email email of the user
	 * @param roles a list of authorities
	 * @return the created user
	 * @throws NoRolesFoundException if the role does not exists throws this exception
	 * @throws DataIntegrityViolationException throw an exception when the mails exists
	 */
	User createNewUser(String username, String password, String email, List<String> roles) throws NoRolesFoundException,DataIntegrityViolationException;


	void updateUser(long userId, User user) throws DataIntegrityViolationException;

	void deleteUser(long userId);


	/**
	 * Find a enabled user with given username
	 * @param username a specific username
	 * @return a user with the given username
	 * @throws DataIntegrityViolationException if the user not exists
	 */
	User findByUsername(String username) throws DataIntegrityViolationException;

	/**
	 * Find the user with given email
	 * @param email a user email
	 * @return a user with the given email
	 * @throws DataIntegrityViolationException
	 */
	User findByEmail(String email) throws  DataIntegrityViolationException;

	/**
	 * Updates a User's password, hashing the plaintext password before setting it.
	 * @param user The User to update the password for
	 * @param password The plantext password to hash and save for the User
	 */
	void updatePassword(User user, String password);

	/**
	 * Save the user with new informations
	 * @param user
	 */
	void save(User user);
}
