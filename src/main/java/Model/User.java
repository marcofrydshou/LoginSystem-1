package Model;


import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="users")
public class User {


	@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long Id;

@NotBlank(message="username is required")
@Column(name="username", unique = true)
private String userName;


@NotBlank(message="password is required")
@Size(min=8, max=25, message="minimum 8 characters")
@Column(name="password")
private String password;

@NotBlank(message="email is required")
@Column(name="email", unique = true)
private String email;


}
