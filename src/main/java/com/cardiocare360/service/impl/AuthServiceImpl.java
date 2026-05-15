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
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UtenteRepository utenteRepository;
    private final PazienteRepository pazienteRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public AuthResponse register(RegisterRequest request) {

        // 🔥 Controllo email duplicata
        if (utenteRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("EMAIL_DUPLICATA");
        }

        // 🔥 Controllo codice fiscale duplicato
        if (pazienteRepository.existsByCodiceFiscale(request.getCodiceFiscale())) {
            throw new IllegalArgumentException("CF_DUPLICATO");
        }

        // 🔥 Creazione paziente (che è anche utente)
        Paziente paziente = new Paziente();
        paziente.setNome(request.getNome());
        paziente.setCognome(request.getCognome());
        paziente.setEmail(request.getEmail());
        paziente.setPassword(passwordEncoder.encode(request.getPassword()));
        paziente.setRuolo(Utente.Ruolo.PAZIENTE);

        paziente.setCodiceFiscale(request.getCodiceFiscale());
        paziente.setTelefono(request.getTelefono());
        paziente.setIndirizzo(request.getIndirizzo());
        paziente.setLuogoNascita(request.getLuogoNascita());

        // 🔥 Conversione sicura della data
        try {
            paziente.setDataNascita(LocalDate.parse(request.getDataNascita()));
        } catch (Exception e) {
            throw new IllegalArgumentException("DATA_NON_VALIDA");
        }

        // 🔥 Salvataggio
        pazienteRepository.save(paziente);

        // 🔥 Generazione token
        String token = jwtUtil.generateToken(
                paziente.getEmail(),
                paziente.getRuolo().name()
        );

        return new AuthResponse(
                token,
                paziente.getRuolo().name(),
                paziente.getId(),
                paziente.getId()
        );
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        Utente utente = utenteRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("CREDENZIALI_ERRATE"));

        if (!passwordEncoder.matches(request.getPassword(), utente.getPassword())) {
            throw new IllegalArgumentException("CREDENZIALI_ERRATE");
        }

        Paziente paziente = pazienteRepository.findById(utente.getId())
                .orElse(null);

        String token = jwtUtil.generateToken(
                utente.getEmail(),
                utente.getRuolo().name()
        );

        return new AuthResponse(
                token,
                utente.getRuolo().name(),
                utente.getId(),
                paziente != null ? paziente.getId() : null
        );
    }
}
