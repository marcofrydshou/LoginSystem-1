package demo.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import demo.data.model.JwtAuthenticationToken;
import demo.data.model.User;

/**
 * 3. Provider
 * all our validate token processing takes care
 *
 *
 */
@Slf4j
@Component
public class JwtAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

	@Autowired
	private JwtValidator validator;

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {

	}

	@Override
	protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {

		// Get the token already saved in AuthenticationTokenFilter
		JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) usernamePasswordAuthenticationToken;

		// get token
		String token = jwtAuthenticationToken.getToken();

		// validating the token and decode the user identify
		User user = validator.validate(token);

		if(user == null){
			throw new RuntimeException("JWT PasswordResetToken is incorrect");
		}

		return new User(user.getUsername(),user.getPassword(),user.getEmail(), user.isEnabled(),user.getAuthoritites());
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return (JwtAuthenticationToken.class.isAssignableFrom(aClass));
	}
}
