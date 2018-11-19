package demo.model.form;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserConfigurationForm {

	private String username;
	private String password;
	private String email;
	private boolean enabled;
	private List<String> roles;

}
