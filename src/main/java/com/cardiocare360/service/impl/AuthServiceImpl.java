package com.cardiocare360.service.impl;

import com.cardiocare360.model.entity.Utente;
import com.cardiocare360.model.entity.Paziente;
import com.cardiocare360.model.request.LoginRequest;
import com.cardiocare360.model.request.RegisterRequest;
import com.cardiocare360.model.response.AuthResponse;
import com.cardiocare360.repository.UtenteRepository;
import com.cardiocare360.repository.PazienteRepository;
import com.cardiocare360.security.jwt.JwtUtil;
import com.cardiocare360.service.AuthService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UtenteRepository utenteRepository;
    private final PazienteRepository pazienteRepository;
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

        return new AuthResponse(token, utente.getRuolo().name(), utente.getId(), null);
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        // Autenticazione
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Recupero utente
        Utente utente = utenteRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        // 🔥 Recupero paziente tramite ID ereditato (corretto)
        Paziente paziente = pazienteRepository.findById(utente.getId())
                .orElse(null);

        // Generazione token
        String token = jwtUtil.generateToken(
                utente.getEmail(),
                utente.getRuolo().name()
        );

        // 🔥 Risposta completa
        return new AuthResponse(
                token,
                utente.getRuolo().name(),
                utente.getId(),
                paziente != null ? paziente.getId() : null
        );
    }
}
