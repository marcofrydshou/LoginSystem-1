package demo.security;

import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;

import demo.model.Role;
import demo.model.User;
import demo.service.UserService;




/*
Tokenprovider der generer token med dependeny io.jsonwebtoken
validere token til  user

 */


@Component
public class JwtTokenProvider {

    public static final String SECRET = "SecretKeyToGenJWTs";
    public static final long EXPIRATION_TIME = 3000_000;
    //generate token


    @Autowired
    private UserService userService;



    public String generateToken(User user) {

        Date now = new Date(System.currentTimeMillis());

        //Expiration time fra vores sec constants added til vores current time
        Date expire = new Date(now.getTime() + EXPIRATION_TIME);

        //Vores token lager nogle fields fra user
       // String userId = Long.toString(user.getId());

        //claims fra user til token
        Claims claims = Jwts.claims().setSubject(user.getUsername());
        claims.put("id", user.getId().toString());
        claims.put("role", user.getRoles());

        //token builder
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expire)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();

    }


    //validate token


    public User validateToken(String token){

        User user = null;
        try {
            Claims claims  = Jwts.parser().setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();


           user = new User();
           String username = claims.getIssuer();
           Date time = claims.getIssuedAt();
           Date exp = claims.getExpiration();

           long id = Long.parseLong((String)claims.get("id"));


            user = userService.getOne(id);
            user.setTokenDate(time.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());



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
        return  user;
    }


    //get user id from token, tager string ind og laver claims der parser jwt med secret med et id der er parset fra string til long
    public Long getUserIdFromJWT(String token){
        Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
      String id = (String)claims.get("Id");
      return Long.parseLong(id);
    }


}
