package demo.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import demo.model.JwtAuthenticationToken;
import demo.model.User;

/**
 * Provider
 * all our major processing takes care
 * this is where we are going to authenticate the class
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

		// convert UsernamePasswordAuthenticationToken into Our JwtAuthenticationToken
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
