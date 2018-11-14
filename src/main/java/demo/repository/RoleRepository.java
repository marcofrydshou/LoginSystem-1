package demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import demo.model.Role;

/**
 * Repository interface of Role
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	Optional<Role> findByAuthority(String authority);

}
