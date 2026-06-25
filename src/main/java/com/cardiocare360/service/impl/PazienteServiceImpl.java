package com.cardiocare360.service.impl;

import com.cardiocare360.model.entity.*;
import com.cardiocare360.model.request.PazienteUpdateDTO;
import com.cardiocare360.model.response.PazienteDTO;
import com.cardiocare360.repository.*;
import com.cardiocare360.service.PazienteService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PazienteServiceImpl implements PazienteService {

    private final PazienteRepository pazienteRepository;
    private final UtenteRepository utenteRepository;
    private final PasswordEncoder passwordEncoder;

    private final AppuntamentoRepository appuntamentoRepository;
    private final TerapiaRepository terapiaRepository;
    private final ParametroClinicoRepository parametroClinicoRepository;
    private final EsameRepository esameRepository;

    
    @Override
    public PazienteDTO getProfilo(Long idPaziente) {
        Paziente p = pazienteRepository.findById(idPaziente)
                .orElseThrow(() -> new RuntimeException("PAZIENTE_NON_TROVATO"));

        return new PazienteDTO(p);
    }

    @Override
    public PazienteDTO aggiornaProfilo(Long idPaziente, PazienteUpdateDTO request) {

        Paziente p = pazienteRepository.findById(idPaziente)
                .orElseThrow(() -> new RuntimeException("PAZIENTE_NON_TROVATO"));

        // 1️⃣ VALIDAZIONI

        // EMAIL duplicata (solo se cambiata)
        if (request.getEmail() != null && !request.getEmail().equals(p.getEmail())) {
            if (utenteRepository.existsByEmail(request.getEmail())) {
                throw new RuntimeException("EMAIL_DUPLICATA");
            }
        }

        // CODICE FISCALE duplicato (solo se cambiato)
        if (request.getCodiceFiscale() != null && 
            !request.getCodiceFiscale().equals(p.getCodiceFiscale())) {

            if (pazienteRepository.existsByCodiceFiscale(request.getCodiceFiscale())) {
                throw new RuntimeException("CODICE_FISCALE_DUPLICATO");
            }
        }

        // TELEFONO valido
        if (request.getTelefono() != null && !request.getTelefono().matches("\\d{8,15}")) {
            throw new RuntimeException("TELEFONO_NON_VALIDO");
        }

        // DATA DI NASCITA valida
        if (request.getDataNascita() != null) {
            LocalDate data = LocalDate.parse(request.getDataNascita());
            if (data.isAfter(LocalDate.now())) {
                throw new RuntimeException("DATA_NASCITA_NON_VALIDA");
            }
            p.setDataNascita(data);
        }

        // 2️⃣ AGGIORNAMENTO DEI CAMPI

        if (request.getNome() != null) p.setNome(request.getNome());
        if (request.getCognome() != null) p.setCognome(request.getCognome());
        if (request.getEmail() != null) p.setEmail(request.getEmail());

        if (request.getCodiceFiscale() != null) p.setCodiceFiscale(request.getCodiceFiscale());
        if (request.getTelefono() != null) p.setTelefono(request.getTelefono());
        if (request.getIndirizzo() != null) p.setIndirizzo(request.getIndirizzo());
        if (request.getLuogoNascita() != null) p.setLuogoNascita(request.getLuogoNascita());

        pazienteRepository.save(p);

        return new PazienteDTO(p);
    }

    @Override
    public List<PazienteDTO> getAllPazienti() {
        return pazienteRepository.findAll()
                .stream()
                .map(PazienteDTO::new)
                .toList();
    }

    @Override
    public List<Appuntamento> getVisiteByPaziente(Long idPaziente) {
        return appuntamentoRepository.findByPazienteId(idPaziente);
    }

    @Override
    public List<Terapia> getTerapieByPaziente(Long idPaziente) {
        return terapiaRepository.findByPazienteId(idPaziente);
    }

    @Override
    public List<ParametroClinico> getParametriByPaziente(Long idPaziente) {
        return parametroClinicoRepository.findByPazienteId(idPaziente);
    }

    @Override
    public List<Esame> getEsamiByPaziente(Long idPaziente) {
        return esameRepository.findByPazienteId(idPaziente);
    }

    // 🔥🔥🔥 METODO CORRETTO PER CREARE UN PAZIENTE
    @Override
    public PazienteDTO creaPaziente(PazienteUpdateDTO request) {

        // 1️⃣ Validazioni
        if (utenteRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("EMAIL_DUPLICATA");
        }

        if (pazienteRepository.existsByCodiceFiscale(request.getCodiceFiscale())) {
            throw new RuntimeException("CODICE_FISCALE_DUPLICATO");
        }

        if (request.getPassword() == null || request.getPassword().length() < 6) {
            throw new RuntimeException("PASSWORD_TROPPO_CORTA");
        }

        if (request.getTelefono() != null && !request.getTelefono().matches("\\d{8,15}")) {
            throw new RuntimeException("TELEFONO_NON_VALIDO");
        }

        if (request.getDataNascita() != null) {
            LocalDate data = LocalDate.parse(request.getDataNascita());
            if (data.isAfter(LocalDate.now())) {
                throw new RuntimeException("DATA_NASCITA_NON_VALIDA");
            }
        }

        // 2️⃣ Creazione Paziente (gestisce anche Utente)
        Paziente p = new Paziente();

        // Campi ereditati da Utente
        p.setNome(request.getNome());
        p.setCognome(request.getCognome());
        p.setEmail(request.getEmail());
        p.setRuolo(Utente.Ruolo.PAZIENTE);
        p.setPassword(passwordEncoder.encode(request.getPassword()));

        // Campi specifici
        p.setCodiceFiscale(request.getCodiceFiscale());
        p.setTelefono(request.getTelefono());
        p.setIndirizzo(request.getIndirizzo());
        p.setLuogoNascita(request.getLuogoNascita());

        if (request.getDataNascita() != null) {
            p.setDataNascita(LocalDate.parse(request.getDataNascita()));
        }

        pazienteRepository.save(p);

        return new PazienteDTO(p);
    }


}
