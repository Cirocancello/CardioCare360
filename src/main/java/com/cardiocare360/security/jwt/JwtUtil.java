package com.cardiocare360.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
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

    // ⭐ GENERA TOKEN USANDO I RUOLI REALI DEL DB
    public String generateToken(UserDetails userDetails) {

        String ruolo = userDetails.getAuthorities()
                .iterator()
                .next()
                .getAuthority();

        Map<String, Object> claims = new HashMap<>();
        claims.put("ruolo", ruolo);
        claims.put("authorities", List.of(ruolo));

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ⭐ ESTRAZIONE DATI DAL TOKEN (ROBUSTA)
    public String extractEmail(String token) {
        Claims claims = safeGetClaims(token);
        return claims != null ? claims.getSubject() : null;
    }

    public String extractRuolo(String token) {
        Claims claims = safeGetClaims(token);
        return claims != null ? (String) claims.get("ruolo") : null;
    }

    public List<String> extractAuthorities(String token) {
        Claims claims = safeGetClaims(token);

        if (claims == null) return List.of();

        Object raw = claims.get("authorities");

        if (raw instanceof List<?> list) {
            return list.stream().map(Object::toString).toList();
        }

        return List.of();
    }

    // ⭐ VALIDAZIONE TOKEN (ROBUSTA)
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            Claims claims = safeGetClaims(token);
            if (claims == null) {
                System.out.println(">>> [JWT] Claims null, token non valido");
                return false;
            }

            String email = claims.getSubject();
            boolean emailMatch = email.equals(userDetails.getUsername());
            boolean expired = isTokenExpired(claims);

            System.out.println(">>> [JWT] Email nel token: " + email);
            System.out.println(">>> [JWT] Email utente: " + userDetails.getUsername());
            System.out.println(">>> [JWT] Match email: " + emailMatch);
            System.out.println(">>> [JWT] Token scaduto: " + expired);

            return emailMatch && !expired;

        } catch (Exception e) {
            System.out.println(">>> [JWT] Errore validazione token: " + e.getMessage());
            return false;
        }
    }


    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    // ⭐ METODO SICURO PER OTTENERE I CLAIMS
    private Claims safeGetClaims(String token) {
        try {
            // Controllo token malformato
            if (token == null || token.split("\\.").length != 3) {
                System.out.println(">>> [JWT] Token malformato: " + token);
                return null;
            }

            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

        } catch (JwtException | IllegalArgumentException e) {
            System.out.println(">>> [JWT] Errore parsing token: " + e.getMessage());
            return null;
        }
    }
}
