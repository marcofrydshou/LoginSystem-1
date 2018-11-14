package demo.config.security;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.UnsupportedJwtException;

import lombok.extern.slf4j.Slf4j;

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
		User user = new User();;
		try {
			Claims body = Jwts.parser()
					.setSigningKey(secret)
					.parseClaimsJws(token)
					.getBody();

			Long userId =  Long.parseLong((String)body.get("userId"));
			String userName = (String)body.get("sub");
			user.setId(userId);
			user.setUsername(userName);
		}
		catch (Exception e){
			throw new UnsupportedJwtException("JWT Token is missing");
		}
		return user;
	}
}
