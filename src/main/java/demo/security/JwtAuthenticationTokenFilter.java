package demo.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import lombok.extern.slf4j.Slf4j;

import demo.service.impl.UserServiceImpl;


@Slf4j
public class JwtAuthenticationTokenFilter extends AbstractAuthenticationProcessingFilter {




	//pass all incoming URL's, to be hit for every URL which is having slash star star
	public JwtAuthenticationTokenFilter() {
		super("**/rest/**");
	}

	//the method authorize our requests, where we handling our request and the validating, where token will be used



	private String getToken(HttpServletRequest request){
		// from httpServletRequest can get header infomations
		String header = request.getHeader("AUTH_HEADER");
		if( header != null && header.startsWith("Bearer ") ){
			return header.substring(7);
		}
		return null;
	}


	@Override
	public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {

		String authToken = getToken(httpServletRequest);
		if(authToken != null){

			// need to send back
			JwtAuthenticationToken token = new JwtAuthenticationToken(authToken);

			// get authenticationManager from extends library
			// then authenticating it
			return getAuthenticationManager().authenticate(token);
		}

return  null;
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
		super.successfulAuthentication(request, response, chain, authResult);
		// block any request and response
		chain.doFilter(request, response);
	}

}
