package com.cardiocare360.security.jwt;

import com.cardiocare360.security.userdetails.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();

        // 🔹 Endpoint pubblici o non protetti
        if (path.startsWith("/auth")
                || path.startsWith("/disponibilita/slot")
                || path.startsWith("/disponibilita/date")
                || path.startsWith("/notifiche")
                || path.startsWith("/api/notifiche")
                || path.startsWith("/api/appuntamenti")) {   // ⭐ FIX FONDAMENTALE

            filterChain.doFilter(request, response);
            return;
        }

        // 🔹 Recupera header Authorization
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 🔹 Estrai token
        String token = authHeader.substring(7).trim();

        // 🔥 Token malformato
        if (token.split("\\.").length != 3) {
            System.out.println(">>> [JWT] Token malformato: " + token);
            filterChain.doFilter(request, response);
            return;
        }

        // 🔹 Estrai email dal token
        String email = jwtUtil.extractEmail(token);

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            var userDetails = customUserDetailsService.loadUserByUsername(email);

            if (jwtUtil.validateToken(token, userDetails)) {

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);

                System.out.println(">>> [JWT] Utente autenticato: " + email +
                        " | Ruoli: " + userDetails.getAuthorities());
            } else {
                System.out.println(">>> [JWT] Token non valido per utente: " + email);
            }
        }

        filterChain.doFilter(request, response);
    }
}
