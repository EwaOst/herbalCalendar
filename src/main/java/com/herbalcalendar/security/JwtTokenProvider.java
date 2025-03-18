package com.herbalcalendar.security;


import com.herbalcalendar.exception.JwtAuthenticationException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    /**
     * Generowanie tokenu JWT dla użytkownika.
     * Zapisuje **ID użytkownika** jako `subject` tokenu.
     */
    public String generateToken(Long userId, String username) {
        // Tworzymy claims (dane, które będą zawarte w tokenie)
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);  // Dodajemy ID użytkownika do claims
        claims.put("username", username);  // Dodajemy nazwę użytkownika do claims

        // Ustalamy czas wygaśnięcia tokenu (np. 1 godzina)
        Date now = new Date();
        Date expiration = new Date(now.getTime() + 3600 * 1000); // 1 godzina

        // Tworzymy klucz podpisu z tajnego klucza
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());  // jwtSecret to Twój tajny klucz, np. z pliku konfiguracyjnego

        // Generujemy token JWT
        return Jwts.builder()
                .setClaims(claims)  // Dodajemy claims z userId i username
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS256) // Zamiast String używamy SecretKey
                .compact();
    }
    /**
     * Pobiera ID użytkownika z tokenu JWT.
     */
    public Long getUserIdFromToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

            String userId = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject(); // Subject przechowuje ID użytkownika

            return Long.parseLong(userId);
        } catch (JwtException e) {
            throw new JwtAuthenticationException("Invalid or expired JWT token", e);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid user ID format in token", e);
        }
    }

    /**
     * Walidacja tokenu JWT.
     */
    public boolean validateToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token); // Parsowanie tokenu
            return true;
        } catch (JwtException ex) {
            return false;
        }
    }
}
