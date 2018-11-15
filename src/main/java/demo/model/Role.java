package demo.model;



import java.util.Collection;
import javax.persistence.*;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity(name = "roles")
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;


	@ManyToMany(mappedBy = "roles", targetEntity = User.class, fetch = FetchType.LAZY)
	private Collection<User> users;

}
