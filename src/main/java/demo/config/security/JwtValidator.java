package demo.config.security;

import java.util.Date;

import io.jsonwebtoken.*;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import demo.data.model.User;
import demo.business.UserService;

/**
 * 4. parser token to Claims with the secretKey
 * Claims is the object with expire date, userID, userName, etc.
 * Validate the userID from db and find the user belongs the userID
 */
@Slf4j
@Component
public class JwtValidator {

	@Value("${app.jwt.secret}")
	private String secretKey;

	@Autowired
	private UserService userService;

	public User validate(String token) {

		User user = new User();;
		try {
			Claims body = Jwts.parser()
					.setSigningKey(secretKey)
					.parseClaimsJws(token)
					.getBody();

			long userId = Long.parseLong((String)body.get("userId"));

			user = userService.findUserById((userId));
		}
		catch (Exception e){
			throw new UnsupportedJwtException("JWT PasswordResetToken is missing"+ e);
		}
		return user;
	}

	public String refreshAccessToken(String accessToken) {

		Claims claims = Jwts.parser()
				.setSigningKey(secretKey)
				.parseClaimsJws(accessToken)
				.getBody();

		return Jwts.builder()
				.setSubject(claims.getSubject())
				.setIssuedAt(new Date())
				.setExpiration(DateUtils.addMinutes(new Date(), 20))
				.signWith(SignatureAlgorithm.HS512, secretKey)
				.compact();
	}

	public String getUsernameFromJWT(String token) {
		Claims claims = Jwts.parser()
				.setSigningKey(secretKey)
				.parseClaimsJws(token)
				.getBody();

		return claims.getSubject();
	}

	public boolean validateToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException ex) {
			log.warn("Invalid JWT signature");
		} catch (MalformedJwtException ex) {
			log.warn("Invalid JWT token");
		} catch (ExpiredJwtException ex) {
			log.warn("Expired JWT token");
			throw ex;
		} catch (UnsupportedJwtException ex) {
			log.warn("Unsupported JWT token");
		} catch (IllegalArgumentException ex) {
			log.warn("JWT claims string is empty.");
		}
		return false;
	}
}
