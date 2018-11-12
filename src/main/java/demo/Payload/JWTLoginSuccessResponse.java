package demo.Payload;

/*
Bruges i RestController
Tager en boolean succes og string token som input
 */

public class JWTLoginSuccessResponse {


	private boolean sucess;
	private String token;

	public JWTLoginSuccessResponse(boolean sucess, String token) {
		this.sucess = sucess;
		this.token = token;
	}


	public boolean isSucess() {
		return sucess;
	}

	public void setSucess(boolean sucess) {
		this.sucess = sucess;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override public String toString() {
		return "JWTLoginSuccessResponse{" +
				"sucess=" + sucess +
				", token='" + token + '\'' +
				'}';
	}
}
