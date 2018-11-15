package demo.service;

import java.util.List;
import java.util.Optional;

import demo.exception.BusinessException;
import demo.model.User;

/**
 * Service interface of User
 */
public interface UserService  {

	/**
	 *
	 * @return A list of users
	 */
	List<User> getAllUsers() throws BusinessException;

	User getUserByUsername(String username) throws BusinessException;

	User getOne(Long Id);

	User saveUser(User user);

	void createNewUser(User user);
}
