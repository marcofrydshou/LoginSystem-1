package demo.security;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import demo.model.User;

@Component
public class JwtGenerator {

	public String generate(User user) {

		Date expTime =  new GregorianCalendar(2018, Calendar.DECEMBER,25,5,0).getTime();

		// iss means who the token belongs to
		Claims claims = Jwts.claims()
				.setIssuer(user.getName())
				.setIssuedAt(new Date())
				.setExpiration(expTime);
		claims.put("userId",String.valueOf(user.getId()));
		return Jwts.builder()
				.setClaims(claims)
				.signWith(SignatureAlgorithm.HS512, "test")
				.compact();
	}
}
