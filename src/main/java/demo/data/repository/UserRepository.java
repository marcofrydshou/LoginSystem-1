package demo.data.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import demo.data.model.User;

/**
 * Repository interface of User
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	/**
	 * Find a enabled user with given email
	 *
	 * @param email user's email
	 * @return a user with the given email
	 */
	Optional<User> findByEmailIgnoreCase(String email);

	/**
	 * Find a enabled user by user id
	 * @param userId a unik user id
	 * @return A container object of user
	 */
	Optional<User> findByIdAndEnabledIsTrue(long userId);

	/**
	 * Find a exists user with given username
	 * @param username
	 * @return  a user with the given username
	 */
	Optional<User> findByUsernameAndEnabledIsTrue(String username);



	/**
	 * Find all enabled users
	 *
	 * @return a list of exists users
	 */
	List<User> findUsersByEnabledIsTrue();

}
