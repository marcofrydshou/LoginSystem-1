package demo.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import lombok.extern.slf4j.Slf4j;

import demo.model.JwtAuthenticationToken;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * TokenFilter will be filtering out the URL's
 */
@Slf4j
public class JwtAuthenticationTokenFilter extends AbstractAuthenticationProcessingFilter {

	//pass all incoming URL's, to be hit for every URL which is having slash star star
	public JwtAuthenticationTokenFilter() {
		super("/api/**");
	}

	//the method authorize our requests, where we handling our request and the validating, where token will be used
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
		Authentication authenticatation = null;
		try {
			String authenticationToken = getJwtFromRequest(request);

			// create a new instance of custom token object with the token from request
			JwtAuthenticationToken jwt = new JwtAuthenticationToken(authenticationToken);

			// get authenticationManager from extends library
			// then authenticating it
			authenticatation =  getAuthenticationManager().authenticate(jwt);
		}
		catch (Exception e){
			log.warn("Failed to authenticate the token", e);
		}
		return authenticatation;
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
		super.successfulAuthentication(request, response, chain, authResult);
		// block any request and response
		chain.doFilter(request, response);
	}

	private String getJwtFromRequest(HttpServletRequest request){

		String bearerToken = request.getHeader(AUTHORIZATION);

		if( StringUtils.isNotBlank(bearerToken) && bearerToken.startsWith("Bearer ") ){
			return bearerToken.substring(7);
		}
		return null;
	}

}
