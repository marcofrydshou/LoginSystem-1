package demo.data.model;

import java.time.LocalDateTime;

import javax.persistence.*;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name="authority")
	private String authority;

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

}
