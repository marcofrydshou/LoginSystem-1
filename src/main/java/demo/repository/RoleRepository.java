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
	 *
	 * @param authorities
	 * @return
	 */
	List<Role> findByAuthorityIn(List<String> authorities);

	Role findByAuthority(String authroity);

}
