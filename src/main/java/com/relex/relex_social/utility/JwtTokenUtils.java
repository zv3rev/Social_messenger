package com.relex.relex_social.utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;

@Component
@Log
public class JwtTokenUtils {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.lifetime}")
    private Duration jwtLifetime;

    public String generateToken(UserDetails userDetails){
        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + jwtLifetime.toMillis());
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        claims.put("roles", roles);

        /// TODO: 07.10.2023 добавить сохранение токена в базу вместе с id
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    private Claims getClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(secret.getBytes())
                .build().parseClaimsJws(token).getBody();
    }

    public String getUsername(String token){
        return getClaims(token).getSubject();
    }
    public List<String> getRoles (String token){
        return getClaims(token).get("roles", List.class);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secret.getBytes())
                    .build().parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            log.log(Level.INFO, "A token with an incorrect signature was received", e);
        } catch (ExpiredJwtException e) {
           log.log(Level.INFO, "Expired token received", e);
        } catch (Exception e) {
            log.log(Level.INFO, "Unexpected error while validating JWT", e);
        }
        return false;
    }
}
