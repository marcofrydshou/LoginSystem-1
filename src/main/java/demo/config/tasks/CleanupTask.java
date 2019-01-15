package demo.config.tasks;

import demo.data.repository.PasswordTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Slf4j
@Component
@Transactional
public class CleanupTask {
    private PasswordTokenRepository passwordTokenRepository;

    @Autowired
    public CleanupTask(PasswordTokenRepository passwordTokenRepository) {
        this.passwordTokenRepository = passwordTokenRepository;
    }

    @Scheduled(cron = "${app.cron.passwordResetCleanup}")
    public void cleanupExpiredPasswordResetTokens() {

        log.info("Cleaning up expired password reset tokens.");
        LocalDateTime expiryDate = LocalDateTime.now();
        expiryDate = expiryDate.minusMinutes(30);

        passwordTokenRepository.deleteByExpiryDateBefore(expiryDate);
    }
}
