package com.herbalcalendar.security;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        // Tworzenie klucza
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key) // Używamy nowej metody signWith z obiektem Key
                .compact();
    }

    public String getUsernameFromToken(String token) {
        // Tworzenie klucza
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        return Jwts.parserBuilder()
                .setSigningKey(key) // Używamy nowej metody setSigningKey z obiektem Key
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes()); // Tworzenie klucza
            Jwts.parserBuilder()
                    .setSigningKey(key) // Ustawienie klucza
                    .build()
                    .parseClaimsJws(token); // Parsowanie tokenu
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
