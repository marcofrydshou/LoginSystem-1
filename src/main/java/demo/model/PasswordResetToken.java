package demo.model;

import java.time.LocalDateTime;

import javax.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "password_token")
public class PasswordResetToken {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column
	private String token;

	@OneToOne(targetEntity = User.class)
	@JoinColumn(nullable = false, name = "user_id")
	private User user;

	@Column
	private LocalDateTime expiryDate;

	@Column(nullable = false)
	protected LocalDateTime created;

	@Column(nullable = false)
	protected LocalDateTime modified;

	@PrePersist
	public void prePersist() {
		created = modified = LocalDateTime.now();
	}

	@PreUpdate
	public void preUpdate() {
		modified = LocalDateTime.now();
	}

	public PasswordResetToken(String token, LocalDateTime expiryDate, User user) {
		this.token = token;
		this.expiryDate = expiryDate;
		this.user = user;
	}
}
