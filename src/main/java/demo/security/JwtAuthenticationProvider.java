package demo.security;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import demo.model.JwtUserDetails;
import demo.model.User;


@Slf4j
@Component
public class JwtAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {


@Autowired
JwtTokenProvider jwtTokenProvider;

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {

	}



	@Override protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		log.debug("USERNAMEPASSWORDAUTHENTICATIONTOKEN ->" + authentication);
		// convert UsernamePasswordAuthenticationToken into Our JwtAuthenticationToken
		JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;

		// get token
		String token = jwtAuthenticationToken.getToken();
		log.debug("TOKEN ->" + jwtAuthenticationToken.getToken());

		// validating the token and decode the user identify
		User user = jwtTokenProvider.validateToken(token);
		log.debug("VALIDATE USER FROM TOKEN ->" + user);

		if(user == null){
			throw new RuntimeException("JWT Token is incorrect");
		}

		// create grants
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList(user.getRoles().toString());
		log.debug("GRANTEAUTHORITIES ->" + grantedAuthorities);
		return new JwtUserDetails(user.getId(), user.getUsername(), token, grantedAuthorities);
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return (JwtAuthenticationToken.class.isAssignableFrom(aClass));
	}
}
