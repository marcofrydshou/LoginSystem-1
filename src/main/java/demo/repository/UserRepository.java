package demo.repository;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;
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
	User findByUsername(String username);

	User findByEmail(String email);

	//User findById(Long Id);


	User save(User user);


	/**
	 * Returns a reference to the entity with the given identifier.
	 *
	 * @param aLong must not be {@literal null}.
	 * @return a reference to the entity with the given identifier.
	 * @throws EntityNotFoundException if no entity exists for given {@code id}.
	 * @see EntityManager#getReference(Class, Object)
	 */
	 User getOne(Long Id);
}
