package com.cardiocare360.service.impl;

import com.cardiocare360.model.entity.Utente;
import com.cardiocare360.model.request.LoginRequest;
import com.cardiocare360.model.request.RegisterRequest;
import com.cardiocare360.model.response.AuthResponse;
import com.cardiocare360.repository.UtenteRepository;
import com.cardiocare360.security.jwt.JwtUtil;
import com.cardiocare360.service.AuthService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UtenteRepository utenteRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public AuthResponse register(RegisterRequest request) {

        if (utenteRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email già registrata");
        }

        Utente utente = new Utente();
        utente.setNome(request.getNome());
        utente.setCognome(request.getCognome());
        utente.setEmail(request.getEmail());
        utente.setPassword(passwordEncoder.encode(request.getPassword()));
        utente.setRuolo(Utente.Ruolo.valueOf(request.getRuolo()));

        utenteRepository.save(utente);

        String token = jwtUtil.generateToken(
                utente.getEmail(),
                utente.getRuolo().name()
        );

        return new AuthResponse(token, utente.getRuolo().name(), utente.getId());
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        System.out.println(">>> AuthServiceImpl.login() chiamato con email = " + request.getEmail());

        // ⭐ BLOCCO TRY/CATCH PER CAPIRE PERCHÉ FALLISCE L’AUTENTICAZIONE
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            System.out.println(">>> Autenticazione riuscita per email = " + request.getEmail());
        } catch (Exception e) {
            System.out.println(">>> AUTENTICAZIONE FALLITA: " 
                    + e.getClass().getSimpleName() 
                    + " - " + e.getMessage());
            throw e;
        }

        Utente utente = utenteRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        String token = jwtUtil.generateToken(
                utente.getEmail(),
                utente.getRuolo().name()
        );

        return new AuthResponse(token, utente.getRuolo().name(), utente.getId());
    }
}
