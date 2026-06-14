package com.cardiocare360.security.config;

import com.cardiocare360.security.jwt.JwtFilter;
import com.cardiocare360.security.userdetails.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((req, res, exc) ->
                    res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Non autorizzato")
                )
            )

            .authorizeHttpRequests(auth -> auth

                // -------------------------
                // CORS preflight
                // -------------------------
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // -------------------------
                // LOGIN / REGISTRAZIONE
                // -------------------------
                .requestMatchers("/auth/**").permitAll()

                // -------------------------
                // ENDPOINT PUBBLICI
                // -------------------------
                .requestMatchers("/disponibilita/slot/**").permitAll()
                .requestMatchers("/disponibilita/date/**").permitAll()
                .requestMatchers("/notifiche/**").permitAll()

                // -------------------------
                // MEDICI
                // -------------------------
                .requestMatchers(HttpMethod.GET, "/medico/**", "/api/medico/**")
                    .hasAnyAuthority("MEDICO", "ADMIN")

                .requestMatchers("/medico/visita/**", "/api/medico/visita/**")
                    .hasAnyAuthority("PAZIENTE", "MEDICO", "ADMIN")

                // -------------------------
                // MESSAGGI
                // -------------------------
                .requestMatchers("/messaggi/**", "/api/messaggi/**")
                    .hasAnyAuthority("MEDICO", "PAZIENTE")

                // -------------------------
                // REFERTI
                // -------------------------
                .requestMatchers(HttpMethod.POST, "/referti/esame/**", "/api/referti/esame/**")
                    .hasAuthority("MEDICO")

                .requestMatchers(HttpMethod.POST, "/referti/upload", "/api/referti/upload")
                    .hasAuthority("MEDICO")

                .requestMatchers(HttpMethod.GET, "/referti/**", "/api/referti/**")
                    .hasAnyAuthority("MEDICO", "PAZIENTE")

                // -------------------------
                // ESAMI
                // -------------------------
                .requestMatchers(HttpMethod.PUT, "/esami/**", "/api/esami/**")
                    .hasAuthority("MEDICO")

                // -------------------------
                // APPUNTAMENTI
                // -------------------------
                .requestMatchers(HttpMethod.GET, "/appuntamenti/paziente/**", "/api/appuntamenti/paziente/**")
                    .hasAuthority("PAZIENTE")

                .requestMatchers(HttpMethod.GET, "/appuntamenti/medico/**", "/api/appuntamenti/medico/**")
                    .hasAuthority("MEDICO")

                .requestMatchers(HttpMethod.GET, "/appuntamenti/*", "/api/appuntamenti/*")
                    .hasAnyAuthority("PAZIENTE", "MEDICO")

                // -------------------------
                // FARMACI
                // -------------------------
                .requestMatchers(HttpMethod.GET, "/farmaci/**", "/api/farmaci/**")
                    .hasAuthority("MEDICO")

                // -------------------------
                // PAZIENTI
                // -------------------------
                .requestMatchers(HttpMethod.GET, "/paziente/**", "/api/paziente/**")
                    .hasAuthority("MEDICO")

                // -------------------------
                // PARAMETRI CLINICI
                // -------------------------
                .requestMatchers(HttpMethod.POST, "/pazienti/*/parametri/**", "/api/pazienti/*/parametri/**")
                    .hasAuthority("PAZIENTE")

                .requestMatchers(HttpMethod.GET, "/pazienti/*/parametri/**", "/api/pazienti/*/parametri/**")
                    .hasAuthority("PAZIENTE")

                // -------------------------
                // TERAPIE
                // -------------------------
                .requestMatchers(HttpMethod.GET, "/terapie/paziente/*", "/api/terapie/paziente/*")
                    .hasAuthority("PAZIENTE")

                .requestMatchers(HttpMethod.GET, "/terapie/medico/*", "/api/terapie/medico/*")
                    .hasAuthority("MEDICO")

                .requestMatchers(HttpMethod.POST, "/terapie", "/api/terapie")
                    .hasAuthority("MEDICO")

                // -------------------------
                // TUTTO IL RESTO
                // -------------------------
                .anyRequest().authenticated()
            )

            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        config.setExposedHeaders(Arrays.asList("Authorization"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
