package demo.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserConfigurationForm {
	private String username;
	private String password;
	private boolean enabled;
	private List<String> roles;
	private String email;
}
