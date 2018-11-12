package demo.Security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;

import demo.model.User;

import static demo.Security.SecurityConstants.EXPIRATION_TIME;
import static demo.Security.SecurityConstants.SECRET;

/*
Tokenprovider der generer token med dependeny io.jsonwebtoken
validere token til  user

 */


@Component
public class JwtTokenProvider {


    //generate token
    public String generateToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        Date now = new Date(System.currentTimeMillis());

        //Expiration time fra vores sec constants added til vores current time
        Date expire = new Date(now.getTime() + EXPIRATION_TIME);

        //Vores token lager nogle fields fra user
        String userId = Long.toString(user.getId());


        //claims fra user til token
        Map<String, Object> jwtClaims = new HashMap<>();
        jwtClaims.put("id", (Long.toString(user.getId())));
        jwtClaims.put("username", user.getUsername());
        jwtClaims.put("name", user.getName());



        //token builder
        return Jwts.builder()
                .setSubject(userId)
                .setClaims(jwtClaims)
                .setIssuedAt(now)
                .setExpiration(expire)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();

    }


    //validate token


    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        }catch (SignatureException ex){
            System.out.println("Invalid jwt signature");
        }catch (MalformedJwtException ex){
            System.out.println("invalid token");
        }catch (ExpiredJwtException ex){
            System.out.println("Expired token");
        } catch (UnsupportedJwtException ex){
            System.out.println("unsupported jwt");
        }catch (IllegalArgumentException ex){
            System.out.println("jwt claims is empty");
        }
        return false;
    }


    //get user id from token, tager string ind og laver claims der parser jwt med secret med et id der er parset fra string til long
    public Long getUserIdFromJWT(String token){
        Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
      String id = (String)claims.get("Id");
      return Long.parseLong(id);
    }


}
