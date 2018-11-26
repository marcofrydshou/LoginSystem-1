package demo.security;

import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

	@Value("${app.jwt.secret}")
	private String secretKey;

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

			long userId = Long.parseLong((String)body.get("userId"));
			String userName = body.getSubject();
			Date currentTime = body.getIssuedAt();
			Date exp = body.getExpiration();

			// check if the userId exists in our db
			user = userService.findUserById((userId));
		}
		catch (Exception e){
			throw new UnsupportedJwtException("JWT PasswordResetToken is missing"+ e);
		}
		return user;
	}
}
