package demo.Payload;

/*
Bruges i RestController
Tager en boolean succes og string token som input
 */

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class JWTLoginSuccessResponse implements AuthenticationSuccessHandler {


	private boolean sucess;
	private String token;

	public JWTLoginSuccessResponse(boolean sucess, String token) {
		this.sucess = sucess;
		this.token = token;
	}

	public JWTLoginSuccessResponse() {
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

	/**
	 * Called when a user has been successfully authenticated.
	 *
	 * @param request        the request which caused the successful authentication
	 * @param response       the response
	 * @param authentication the <tt>Authentication</tt> object which was created during
	 */
	@Override public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		System.out.println("Succesful authentication");

	}
}
