package demo.config.security;

import java.util.Date;

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

	private String secretKey = "test";

	public User validate(String token) {

		// Jwts gonna parser Jwt message
		// custom secret
		// if the user dont have Claims can throw an exception
		User user = new User();;
		try {
			Claims body = Jwts.parser()
					.setSigningKey(secretKey)
					.parseClaimsJws(token)
					.getBody();

			String username = body.getSubject();
			Long userId = (Long)body.get("userId");
			String email = body.get("email").toString();
			Date exp = body.getExpiration();
//			Object email = body.get("email");
//			user.setId(id);
			user.setUsername(username);
			user.setEmail(email);
		}
		catch (Exception e){
			throw new UnsupportedJwtException("JWT Token is missing");
		}
		return user;
	}
}
