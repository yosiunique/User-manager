package enat.bank.utils;


import enat.bank.user.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class JwtService {
   @Value("${application.jwt_expiration_time}")
   long timeOut;
   @Autowired
   private ApplicationProps applicationProps;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(applicationProps.getJwt_secret_key().getBytes());
    }


    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUserName())
                .addClaims(Map.of("roles", user.getRoles()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + timeOut))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }



    public String generateToken(String username) {
        String compact = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() +timeOut))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
        return compact;
    }

    public List<String> extractRoles(String token) {
        var claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return (List<String>) claims.get("roles");
    }
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
