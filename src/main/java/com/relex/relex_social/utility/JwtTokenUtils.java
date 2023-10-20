package com.relex.relex_social.utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
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
    @Value("${jwt.access.secret}")
    private String accessSecret;

    @Value("${jwt.access.lifetime}")
    private Duration accessLifetime;

    @Value("${jwt.refresh.secret}")
    private String refreshSecret;

    @Value("${jwt.refresh.lifetime}")
    private Duration refreshLifetime;

    public String generateAccessToken(UserDetails userDetails) {
        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + accessLifetime.toMillis());
        Map<String, Object> claims = new HashMap<>();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        claims.put("roles", roles);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(Keys.hmacShaKeyFor(accessSecret.getBytes()))
                .compact();
    }

    public String generateRefreshToken(UserDetails userDetails) {
        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + refreshLifetime.toMillis());

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(Keys.hmacShaKeyFor(refreshSecret.getBytes()))
                .compact();
    }

    private Claims getClaims(String token, String secret) {
        return Jwts.parserBuilder()
                .setSigningKey(secret.getBytes())
                .build().parseClaimsJws(token).getBody();
    }

    public String getUsernameFromAccessToken(String token) {
        return getClaims(token, accessSecret).getSubject();
    }

    public String getUsernameFromRefreshToken(String token) {
        return getClaims(token, refreshSecret).getSubject();
    }

    public List<String> getRolesFromAccessToken(String token) {
        return getClaims(token, accessSecret).get("roles", List.class);
    }

    public boolean validateAccessToken(String token) {
        return validateToken(token, accessSecret);
    }

    public boolean validateRefreshToken(String token) {
        return validateToken(token, refreshSecret);
    }

    private boolean validateToken(String token, String key) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key.getBytes())
                    .build().parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            log.log(Level.INFO, "A token with an incorrect signature was received", e);
            throw new AccessDeniedException("A token with an incorrect signature was received");
        } catch (ExpiredJwtException e) {
            log.log(Level.INFO, "Expired token received", e);
            throw new AccessDeniedException("Expired token received");
        } catch (Exception e) {
            log.log(Level.INFO, "Unexpected error while validating JWT", e);
            throw new AccessDeniedException("Unexpected error while validating JWT");
        }
    }
}
