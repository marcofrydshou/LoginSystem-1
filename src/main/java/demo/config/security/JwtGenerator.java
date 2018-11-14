package demo.config.security;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import demo.model.User;

@Component
public class JwtGenerator {

	public String generate(User user) {

		Claims claims = Jwts.claims()
				.setSubject(user.getUsername());
		claims.put("userId", user.getId());
		claims.put("role", user.getAuthoritites());
		return Jwts.builder()
				.setClaims(claims)
				.signWith(SignatureAlgorithm.HS512, "test")
				.compact();
	}
}
