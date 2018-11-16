package demo.security;

import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.UnsupportedJwtException;

import lombok.extern.slf4j.Slf4j;

import demo.model.User;
import demo.service.UserService;

/**
 * Used jsonwebtoken library to create some claims
 */
@Slf4j
@Component
public class JwtValidator {

	private String secretKey = "test";

	@Autowired
	private UserService userService;

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

			String userName = body.getIssuer();
			Date currentTime = body.getIssuedAt();
			Date exp = body.getExpiration();
			long userId = Long.parseLong((String)body.get("userId"));

			user = userService.findUserById((userId));
			user.setTokenDate(currentTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
		}
		catch (Exception e){
			throw new UnsupportedJwtException("JWT Token is missing"+ e);
		}
		return user;
	}
}
