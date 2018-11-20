package demo.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import demo.model.Token;
import demo.model.User;

@Repository
public interface PasswordTokenRepository extends JpaRepository<Token, Long> {
	/**
	 *
	 * @param user
	 * @return
	 */
	Optional<Token> findByUser(User user);

	/**
	 *
	 * @param user
	 */
	void deleteByUser(User user);

	/**
	 *
	 * @param currentTime
	 */
	void deleteByExpiryDateBefore(LocalDateTime currentTime);
}
