package demo.model.dto;



import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginForm {
	@NotNull
	private String username;

	@NotNull
	private String password;

}
