package demo.exception;

/*
Exception klasse med generiske harcodede invalid messages
Giver ikke hint om specifikke fejl, så en angriber ikke informeres for meget
Burges i JWTAuthenticationEntryPoint og laves til et json object der bruges i SecurityConfig

 */

public class InvalidLoginResponse extends Throwable {

	//JSON oobject der reutrnere
	private String username;
	private String password;


	public InvalidLoginResponse() {
		this.username = "Invalid username";
		this.password = "Invalid password";
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
