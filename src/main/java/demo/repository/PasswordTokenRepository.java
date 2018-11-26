package demo.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import demo.model.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import demo.model.User;

/**
 * Repository interface of PasswordTokenRepository
 */
@Repository
public interface PasswordTokenRepository extends JpaRepository<PasswordResetToken, Long> {
	/**
	 *
	 * @param user
	 * @return
	 */
	Optional<PasswordResetToken> findByUser(User user);

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
