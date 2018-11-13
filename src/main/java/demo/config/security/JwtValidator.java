package demo.config.security;


import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

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
			Collection<Role> roles = null;
			Role role = new Role();
			roles.add(role);
			role.setName((String) body.get("role"));

			user.setRoles(roles);
		}
		catch (Exception e){
			log.debug("Can't find Claims from the token" + e);
			throw e;
		}
		return user;
	}
}
