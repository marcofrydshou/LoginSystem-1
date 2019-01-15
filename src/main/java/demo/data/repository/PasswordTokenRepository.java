package demo.data.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import demo.data.model.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import demo.data.model.User;

/**
 * Repository interface of PasswordTokenRepository
 */
@Repository
public interface PasswordTokenRepository extends JpaRepository<PasswordResetToken, Long> {

	/**
	 * Find the token belongs to the given user
	 *
	 * @param user the user which is token belongs to
	 * @return Optional of token
	 */
	Optional<PasswordResetToken> findByUser(User user);

	/**
	 * Delete the token by given user
	 *
	 * @param user the user needs to be delete token
	 */
	void deleteByUser(User user);

	/**
	 * Delete the token by given time
	 *
	 * @param currentTime the time, token needs to be delete
	 */
	void deleteByExpiryDateBefore(LocalDateTime currentTime);

}
