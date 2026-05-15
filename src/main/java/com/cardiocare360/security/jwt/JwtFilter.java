package com.cardiocare360.security.jwt;

import com.cardiocare360.security.userdetails.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

        String uri = request.getRequestURI();
        System.out.println(">>> URI: [" + uri + "]");
        System.out.println(">>> AUTH HEADER: " + request.getHeader("Authorization"));

        // 🔥 Escludi SEMPRE endpoint pubblici (login, register, forgot-password, disponibilità, notifiche)
        if (uri.startsWith("/auth/") ||
            uri.startsWith("/disponibilita/slot") ||
            uri.startsWith("/api/notifiche")) {

            System.out.println(">>> [JWT] Endpoint pubblico, filtro saltato");
            filterChain.doFilter(request, response);
            return;
        }

        // 🔥 Recupera header Authorization
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 🔥 Estrai token e email
        String token = authHeader.substring(7);
        String email = jwtUtil.extractEmail(token);

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

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
