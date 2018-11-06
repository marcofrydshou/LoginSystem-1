package Repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import Model.User;

@Repository
public interface UserRepository extends CrudRepository <User, String> {

	/**
	 * Deletes the entity with the given id.
	 *
	 * @param s must not be {@literal null}.
	 * @throws IllegalArgumentException in case the given {@code id} is {@literal null}
	 */
	@Override void deleteById(String s);




	/**
	 * Deletes a given entity.
	 *
	 * @param entity
	 * @throws IllegalArgumentException in case the given entity is {@literal null}.
	 */
	@Override void delete(User entity);

	/**
	 * Retrieves an entity by its id.
	 *
	 * @param s must not be {@literal null}.
	 * @return the entity with the given id or {@literal Optional#empty()} if none found
	 * @throws IllegalArgumentException if {@code id} is {@literal null}.
	 */
	@Override Optional<User> findById(String s);



	User findByUserName(String userName);



	User findByEmail(String email);


	/**
	 * Returns all instances of the type.
	 *
	 * @return all entities
	 */
	@Override Iterable<User> findAll();

	/**
	 * Saves a given entity. Use the returned instance for further operations as the save operation might have changed the
	 * entity instance completely.
	 *
	 * @param entity must not be {@literal null}.
	 * @return the saved entity will never be {@literal null}.
	 */
	@Override <S extends User> S save(S entity);
}
