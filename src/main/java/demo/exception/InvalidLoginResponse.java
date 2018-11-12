package demo.exception;

/*
Exception klasse med generiske harcodede invalid messages
Giver ikke hint om specifikke fejl, så en angriber ikke informeres for meget
Burges i JWTAuthenticationEntryPoint og laves til et json object der bruges i SecurityConfig

 */

public class InvalidLoginResponse extends Throwable {

	//JSON oobject der reutrnere
	private String userName;
	private String password;


	public InvalidLoginResponse() {
		this.userName = "Invalid username";
		this.password = "Invalid password";
	}

	public String getUsername() {
		return userName;
	}

	public void setUsername(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
