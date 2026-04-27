package com.cardiocare360.service.impl;

import com.cardiocare360.model.entity.Utente;
import com.cardiocare360.model.request.LoginRequest;
import com.cardiocare360.model.request.RegisterRequest;
import com.cardiocare360.model.response.AuthResponse;
import com.cardiocare360.repository.UtenteRepository;
import com.cardiocare360.security.jwt.JwtUtil;
import com.cardiocare360.service.AuthService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

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

        // Conversione STRINGA → ENUM
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

        Utente utente = utenteRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Email o password errati"));

        if (!passwordEncoder.matches(request.getPassword(), utente.getPassword())) {
            throw new BadCredentialsException("Email o password errati");
        }

        String token = jwtUtil.generateToken(
                utente.getEmail(),
                utente.getRuolo().name()
        );

        return new AuthResponse(token, utente.getRuolo().name(), utente.getId());
    }
}
