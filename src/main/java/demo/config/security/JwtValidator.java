package demo.config.security;



import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.UnsupportedJwtException;

import lombok.extern.slf4j.Slf4j;

import demo.model.Role;
import demo.model.User;

/**
 * Used jsonwebtoken library to create some claims
 */
@Slf4j
@Component
public class JwtValidator {

	private String secret = "test";

	public User validate(String token) {

		// Jwts gonna parser Jwt message
		// custom secret
		// if the user dont have Claims can throw an exception
		User user = null;
		try {
			Claims body = Jwts.parser()
					.setSigningKey(secret)
					.parseClaimsJwt(token)
					.getBody();

			user = new User();

			user.setId(Long.parseLong((String) body.get("userId")));
			user.setUsername(body.getSubject());
			List<Role> roles = new ArrayList<>();
			Role role = new Role();
			role.setAuthority((String)body.get("role"));
			roles.add(role);
			user.setAuthoritites(roles);
		}
		catch (Exception e){
			log.debug("Can't find Claims from the token" + e);
			throw new UnsupportedJwtException("JWT Token is missing");
		}
		return user;
	}
}
