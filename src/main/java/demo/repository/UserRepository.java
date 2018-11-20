package demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import demo.model.User;

/**
 * Repository interface of User
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	/**
	 * Find a enabled user by user id
	 * @param userId a unik user id
	 * @return A container object of user
	 */
	Optional<User> findByIdAndEnabledIsTrue(long userId);

	/**
	 * Find all enabled users
	 * @return a list of exists users
	 */
	List<User> findUserByEnabledIsTrue();

	/**
	 * Find a exists user with given username
	 * @param username
	 * @return  a user with the given username
	 */
	Optional<User> findByUsernameAndEnabledIsTrue(String username);

	/**
	 * Find a enabled user with given email
	 * @param email user's email
	 * @return a user with the given email
	 */
	Optional<User> findByEmailAndEnabledIsTrue(String email);

	/**
	 * Find a enabled user withen given name
	 * @param name the user's name
	 * @return a container object with user
	 */
	Optional<User> findByNameAndEnabledIsTrue(String name);

}
