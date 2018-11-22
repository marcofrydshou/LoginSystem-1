package demo.security;

import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import demo.model.User;

@Component
public class JwtGenerator {

	@Value("${app.jwt.secret}")
	private String secretKey;

	public String generate(User user) {

		final Date currentTime = new Date();
		final Date expiry = DateUtils.addMinutes(currentTime, 20);

		// issuer means who is the issuer, subject means the token belongs to
		Claims claims = Jwts.claims()
				.setIssuer("LoginSystem")
				.setSubject(user.getName())
				.setIssuedAt(currentTime)
				.setExpiration(expiry);
		claims.put("userId",String.valueOf(user.getId()));
		return Jwts.builder()
				.setClaims(claims)
				.signWith(SignatureAlgorithm.HS512, secretKey)
				.compact();
	}
}
