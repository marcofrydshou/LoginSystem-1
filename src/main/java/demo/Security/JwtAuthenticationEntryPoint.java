package demo.Security;


import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import com.google.gson.Gson;

import demo.exception.InvalidLoginResponse;
import demo.exception.InvalidLoginResponse;

/*
commence laver et Json object, der bruges til error handling ved forkerte crendentials
Tager InvalidLoginReponse klassen og sender videre til SecurityConfig klassen

 */


@Component
@Service
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	 @Override public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			 AuthenticationException e) throws IOException, ServletException {
		 InvalidLoginResponse loginResponse = new InvalidLoginResponse();
		 String jsonLloginReponse = new Gson().toJson(loginResponse);

		 httpServletResponse.setContentType("application/json");
		 httpServletResponse.setStatus(401);
		 httpServletResponse.getWriter().print(jsonLloginReponse);

	}
}
