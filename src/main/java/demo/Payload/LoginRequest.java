package demo.Payload;

import javax.validation.constraints.NotBlank;



/*

 */


public class LoginRequest {

	@NotBlank(message = "username cannot be blank")
	private String username;

	@NotBlank(message = "password cannot be blank")
	private String password;


	public String getUserName() {
		return username;
	}

	public void setUserName(String userName) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
