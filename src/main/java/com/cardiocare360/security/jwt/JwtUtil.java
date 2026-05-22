package com.cardiocare360.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    // ⭐ GENERA TOKEN SENZA ROLE_
    public String generateToken(String email, String ruolo) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("ruolo", ruolo);
        claims.put("authorities", List.of(ruolo)); // 🔥 niente ROLE_

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractEmail(String token) {
        return getAllClaims(token).getSubject();
    }

    public String extractRuolo(String token) {
        return (String) getAllClaims(token).get("ruolo");
    }

    // ⭐ ESTRAE AUTHORITIES SENZA ROLE_
    public List<SimpleGrantedAuthority> extractAuthorities(String token) {
        Object raw = getAllClaims(token).get("authorities");

        if (raw instanceof List<?> list) {
            return list.stream()
                    .map(a -> new SimpleGrantedAuthority(a.toString()))
                    .toList();
        }

        return List.of();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            String email = extractEmail(token);
            return email.equals(userDetails.getUsername()) && !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println(">>> JWT NON VALIDO: " + e.getMessage());
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return getAllClaims(token).getExpiration().before(new Date());
    }

    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
