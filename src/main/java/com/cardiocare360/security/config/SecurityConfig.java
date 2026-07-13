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

        System.out.println(">>> SECURITY CONFIG IN USO");

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

                // CORS
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // LOGIN
                .requestMatchers("/auth/**").permitAll()

                // PUBBLICI
                .requestMatchers("/disponibilita/slot/**").permitAll()
                .requestMatchers("/disponibilita/date/**").permitAll()
                .requestMatchers("/notifiche/**").permitAll()
                .requestMatchers("/medico/public/**").permitAll()

                // ⭐ PAZIENTE
                .requestMatchers("/paziente/all").hasAnyAuthority("ADMIN", "MEDICO")
                .requestMatchers("/paziente/**").hasAnyAuthority("PAZIENTE", "MEDICO", "ADMIN")

                .requestMatchers("/esami/paziente/**").hasAuthority("PAZIENTE")
                .requestMatchers("/esami/prenota").hasAuthority("PAZIENTE")
                .requestMatchers("/appuntamenti/paziente/**").hasAuthority("PAZIENTE")
                .requestMatchers("/terapie/paziente/**").hasAuthority("PAZIENTE")
                .requestMatchers("/referti/paziente/**").hasAuthority("PAZIENTE")

                // ⭐ MEDICI VISIBILI AL PAZIENTE (SCELTA MEDICO)
                .requestMatchers("/medici/**").hasAnyAuthority("PAZIENTE", "MEDICO")
                .requestMatchers("/medico/visita/**").hasAnyAuthority("PAZIENTE", "MEDICO")
                .requestMatchers("/medico/esami", "/medico/esami/**").hasAnyAuthority("PAZIENTE", "MEDICO")

                // 🔥 ESAMI (PAZIENTE + MEDICO)
                .requestMatchers("/esami", "/esami/**").hasAnyAuthority("PAZIENTE", "MEDICO")

                // 🔥 REFERTI (PAZIENTE + MEDICO)
                .requestMatchers("/referti/esame", "/referti/esame/**").hasAnyAuthority("PAZIENTE", "MEDICO")
                .requestMatchers("/referti/preview/**").hasAnyAuthority("PAZIENTE", "MEDICO")
                .requestMatchers("/referti/download/**").hasAnyAuthority("PAZIENTE", "MEDICO")

                // Upload referto → solo medico
                .requestMatchers("/referti/upload", "/api/referti/upload").hasAuthority("MEDICO")

                // 🩺 AREA RISERVATA MEDICO
                .requestMatchers("/medico/**", "/api/medico/**").hasAuthority("MEDICO")
                .requestMatchers("/disponibilita/medico/**").hasAuthority("MEDICO")
                .requestMatchers("/appuntamenti/medico/**").hasAuthority("MEDICO")
                .requestMatchers("/terapie/medico/**").hasAuthority("MEDICO")
                .requestMatchers("/farmaci/**", "/api/farmaci/**").hasAuthority("MEDICO")

                // 🔥 ADMIN
                .requestMatchers("/admin/**").hasAuthority("ADMIN")

                // RESTO
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
