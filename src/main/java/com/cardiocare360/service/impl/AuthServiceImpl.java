package com.cardiocare360.service.impl;

import com.cardiocare360.model.entity.Utente;
import com.cardiocare360.model.entity.Paziente;
import com.cardiocare360.model.entity.Medico;
import com.cardiocare360.model.request.LoginRequest;
import com.cardiocare360.model.request.RegisterRequest;
import com.cardiocare360.model.response.AuthResponse;
import com.cardiocare360.repository.UtenteRepository;
import com.cardiocare360.repository.PazienteRepository;
import com.cardiocare360.repository.MedicoRepository;
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
    private final MedicoRepository medicoRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // ---------------------------------------------------------
    // 🔥 REGISTRAZIONE
    // ---------------------------------------------------------
    @Override
    public AuthResponse register(RegisterRequest request) {

        if (utenteRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("EMAIL_DUPLICATA");
        }

        if (request.getRuolo() == null) {
            throw new IllegalArgumentException("RUOLO_OBBLIGATORIO");
        }

        Utente.Ruolo ruolo = Utente.Ruolo.valueOf(request.getRuolo().toUpperCase());

        switch (ruolo) {

            case PAZIENTE -> {

                if (pazienteRepository.existsByCodiceFiscale(request.getCodiceFiscale())) {
                    throw new IllegalArgumentException("CF_DUPLICATO");
                }

                Paziente paziente = new Paziente();
                paziente.setNome(request.getNome());
                paziente.setCognome(request.getCognome());
                paziente.setEmail(request.getEmail());
                paziente.setPassword(passwordEncoder.encode(request.getPassword()));
                paziente.setRuolo(ruolo);

                paziente.setCodiceFiscale(request.getCodiceFiscale());
                paziente.setTelefono(request.getTelefono());
                paziente.setIndirizzo(request.getIndirizzo());
                paziente.setLuogoNascita(request.getLuogoNascita());
                paziente.setDataNascita(LocalDate.parse(request.getDataNascita()));

                paziente = pazienteRepository.save(paziente);

                String token = jwtUtil.generateToken(paziente.getEmail(), ruolo.name());

                return new AuthResponse(
                        token,
                        ruolo.name(),
                        paziente.getId(),   // idUtente
                        paziente.getId(),   // idPaziente
                        null                // idMedico
                );
            }

            case MEDICO -> {

                Medico medico = new Medico();
                medico.setNome(request.getNome());
                medico.setCognome(request.getCognome());
                medico.setEmail(request.getEmail());
                medico.setPassword(passwordEncoder.encode(request.getPassword()));
                medico.setRuolo(ruolo);

                medico.setSpecializzazione(request.getSpecializzazione());
                medico.setNumeroLicenza(request.getNumeroLicenza());

                medico = medicoRepository.save(medico);

                String token = jwtUtil.generateToken(medico.getEmail(), ruolo.name());

                return new AuthResponse(
                        token,
                        ruolo.name(),
                        medico.getId(),   // idUtente
                        null,             // idPaziente
                        medico.getId()    // idMedico
                );
            }

            case ADMIN -> {

                Utente admin = new Utente();
                admin.setNome(request.getNome());
                admin.setCognome(request.getCognome());
                admin.setEmail(request.getEmail());
                admin.setPassword(passwordEncoder.encode(request.getPassword()));
                admin.setRuolo(ruolo);

                admin = utenteRepository.save(admin);

                String token = jwtUtil.generateToken(admin.getEmail(), ruolo.name());

                return new AuthResponse(
                        token,
                        ruolo.name(),
                        admin.getId(),
                        null,
                        null
                );
            }
        }

        throw new IllegalArgumentException("RUOLO_NON_VALIDO");
    }

    // ---------------------------------------------------------
    // 🔥 LOGIN
    // ---------------------------------------------------------
    @Override
    public AuthResponse login(LoginRequest request) {

        Utente utente = utenteRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("CREDENZIALI_ERRATE"));

        if (!passwordEncoder.matches(request.getPassword(), utente.getPassword())) {
            throw new IllegalArgumentException("CREDENZIALI_ERRATE");
        }

        String token = jwtUtil.generateToken(
                utente.getEmail(),
                utente.getRuolo().name()
        );

        // 🔥 PAZIENTE
        if (utente.getRuolo() == Utente.Ruolo.PAZIENTE) {
            Paziente paziente = pazienteRepository.findById(utente.getId())
                    .orElseThrow(() -> new IllegalArgumentException("PAZIENTE_NON_TROVATO"));

            return new AuthResponse(
                    token,
                    utente.getRuolo().name(),
                    utente.getId(),
                    paziente.getId(),   // idPaziente
                    null                // idMedico
            );
        }

        // 🔥 MEDICO
        if (utente.getRuolo() == Utente.Ruolo.MEDICO) {
            Medico medico = medicoRepository.findById(utente.getId())
                    .orElseThrow(() -> new IllegalArgumentException("MEDICO_NON_TROVATO"));

            return new AuthResponse(
                    token,
                    utente.getRuolo().name(),
                    utente.getId(),
                    null,               // idPaziente
                    medico.getId()      // idMedico
            );
        }

        // 🔥 ADMIN
        return new AuthResponse(
                token,
                utente.getRuolo().name(),
                utente.getId(),
                null,
                null
        );
    }
}
