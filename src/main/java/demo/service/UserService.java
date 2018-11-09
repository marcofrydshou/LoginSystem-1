package demo.service;

import java.util.List;

import demo.exception.DataIntegrityViolationException;
import demo.model.User;

/**
 * Service interface of User
 */
public interface UserService  {

	/**
	 * Find all enabled users
	 * @return A list of enabled users
	 */
	List<User> findEnabledUsers();

	void createNewUser(User user) throws DataIntegrityViolationException;

	void updateUser(Long userId, User user) throws DataIntegrityViolationException;

	void deleteUser(Long userId) throws  DataIntegrityViolationException;


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

	void updatePassword(User user, String password);
}
