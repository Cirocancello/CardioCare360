package com.cardiocare360.security.jwt;

import com.cardiocare360.model.entity.Utente;
import com.cardiocare360.security.userdetails.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

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
        System.out.println(">>> [DEBUG] Filtro JWT attivo su path: " + path);

        // 🔥 LOG FONDAMENTALE PER DEBUG
        System.out.println(">>> [JWT] Path ricevuto: " + path);

        // Endpoint pubblici
        if (path.startsWith("/auth")
                || path.startsWith("/disponibilita/slot")
                || path.startsWith("/disponibilita/date")
                || path.startsWith("/notifiche")
                || path.startsWith("/api/notifiche")
                || path.startsWith("/api/appuntamenti")) {

            filterChain.doFilter(request, response);
            return;
        }

        // Header Authorization
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7).trim();

        // Token malformato
        if (token.split("\\.").length != 3) {
            System.out.println(">>> [JWT] Token malformato");
            filterChain.doFilter(request, response);
            return;
        }

        // Estrazione email dal token
        String email = jwtUtil.extractEmail(token);

        // 🔥 LOG DIAGNOSTICI (sempre stampati)
        System.out.println(">>> [JWT] Email estratta dal token: " + email);
        System.out.println(">>> [JWT] AuthContext PRIMA del controllo: "
                + SecurityContextHolder.getContext().getAuthentication());

        // Se email valida e nessuna autenticazione già presente
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            Utente userDetails = customUserDetailsService.loadUserByUsername(email);

            // Validazione token
            if (jwtUtil.validateToken(token, userDetails)) {

                List<String> authoritiesFromToken = jwtUtil.extractAuthorities(token);

                Collection<SimpleGrantedAuthority> grantedAuthorities =
                        (authoritiesFromToken == null || authoritiesFromToken.isEmpty())
                                ? userDetails.getAuthorities().stream()
                                        .map(a -> new SimpleGrantedAuthority(a.getAuthority()))
                                        .toList()
                                : authoritiesFromToken.stream()
                                        .map(SimpleGrantedAuthority::new)
                                        .toList();

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                grantedAuthorities
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);

                System.out.println(">>> [JWT] Utente autenticato: " + email +
                        " | Authorities: " + grantedAuthorities);
            }
        }

        filterChain.doFilter(request, response);
    }
}
