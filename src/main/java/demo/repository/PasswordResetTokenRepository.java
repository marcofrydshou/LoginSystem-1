package demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import demo.resetmail.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

	PasswordResetToken findByToken(String token);
}
