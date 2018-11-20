package demo.model.dto;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResetPasswordForm {
	private long id;

	@NotNull
	private String password;

	@NotNull
	private String confirmPassword;

}
