package demo.repository;

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
	 * Find the user by name
	 * @param username
	 * @return the user
	 */
	Optional<User> findByUsername(String username);

	Optional<User> findByEmail(String email);
}
