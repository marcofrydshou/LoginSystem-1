package demo.config.security;

import java.util.Date;

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
		claims.put("email",user.getEmail());
		claims.put("role", user.getAuthoritites());
		claims.put("exp", (new Date().getTime()+60*60*1000)/1000); // 1 hour from now:
		return Jwts.builder()
				.setClaims(claims)
				.signWith(SignatureAlgorithm.HS512, "test")
				.compact();
	}
}
