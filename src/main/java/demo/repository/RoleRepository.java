package demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import demo.model.Role;

/**
 * Repository interface of Role
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	/**
	 * Find authorities by checking every single authority of the given list
	 *
	 * @param authorities a list of string
	 * @return a list of role entities
	 */
	List<Role> findByAuthorityIn(List<String> authorities);

	Role findByAuthority(String authroity);

}
