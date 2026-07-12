package com.cardiocare360.service.impl;

import com.cardiocare360.model.entity.Medico;
import com.cardiocare360.model.entity.Utente;
import com.cardiocare360.model.request.MedicoUpdateDTO;
import com.cardiocare360.model.response.MedicoResponse;
import com.cardiocare360.repository.MedicoRepository;
import com.cardiocare360.repository.UtenteRepository;
import com.cardiocare360.service.MedicoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicoServiceImpl implements MedicoService {

    private final MedicoRepository medicoRepository;
    private final UtenteRepository utenteRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MedicoServiceImpl(MedicoRepository medicoRepository,
                             UtenteRepository utenteRepository,
                             PasswordEncoder passwordEncoder) {
        this.medicoRepository = medicoRepository;
        this.utenteRepository = utenteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ---------------------------------------------------------
    // GET MEDICO
    // ---------------------------------------------------------
    @Override
    public MedicoResponse getMedicoById(Long id) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medico non trovato"));
        return convertToResponse(medico);
    }

    // ---------------------------------------------------------
    // UPDATE MEDICO (BLINDATO)
    // ---------------------------------------------------------
    @Override
    public MedicoResponse updateMedico(Long id, MedicoUpdateDTO updateDTO) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medico non trovato"));

        // VALIDAZIONI BASE (senza rompere nulla)
        if (updateDTO.getNome() != null && updateDTO.getNome().length() < 2)
            throw new IllegalArgumentException("Nome troppo corto");

        if (updateDTO.getCognome() != null && updateDTO.getCognome().length() < 2)
            throw new IllegalArgumentException("Cognome troppo corto");

        if (updateDTO.getSpecializzazione() != null &&
                updateDTO.getSpecializzazione().length() < 3)
            throw new IllegalArgumentException("Specializzazione non valida");

        // Campi ereditati da Utente
        if (updateDTO.getNome() != null) medico.setNome(updateDTO.getNome());
        if (updateDTO.getCognome() != null) medico.setCognome(updateDTO.getCognome());

        // Campi specifici di Medico
        if (updateDTO.getSpecializzazione() != null)
            medico.setSpecializzazione(updateDTO.getSpecializzazione());

        medicoRepository.save(medico);
        return convertToResponse(medico);
    }

    // ---------------------------------------------------------
    // GET MEDICI BY SPECIALIZZAZIONE
    // ---------------------------------------------------------
    @Override
    public List<MedicoResponse> getMediciBySpecializzazione(String specializzazione) {

        if (specializzazione == null || specializzazione.isBlank())
            throw new IllegalArgumentException("Specializzazione non valida");

        List<Medico> medici = medicoRepository.findBySpecializzazioneIgnoreCase(specializzazione);
        return medici.stream().map(this::convertToResponse).toList();
    }

    // ---------------------------------------------------------
    // GET MEDICI PER TIPO ESAME
    // ---------------------------------------------------------
    @Override
    public List<MedicoResponse> getMediciPerTipoEsame(String tipoEsame) {

        if (tipoEsame == null || tipoEsame.isBlank())
            throw new IllegalArgumentException("Tipo esame non valido");

        String specializzazione = switch (tipoEsame.toUpperCase()) {
            case "ECG", "HOLTER", "ECOCARDIOGRAMMA" -> "Cardiologia";
            default -> throw new IllegalArgumentException("Tipo esame non riconosciuto");
        };

        List<Medico> medici = medicoRepository.findBySpecializzazioneIgnoreCase(specializzazione);
        return medici.stream().map(this::convertToResponse).toList();
    }

    // ---------------------------------------------------------
    // CAMBIO PASSWORD (BLINDATO)
    // ---------------------------------------------------------
    @Override
    public boolean cambiaPassword(Long id, String passwordAttuale, String nuovaPassword) {

        Medico medico = medicoRepository.findById(id).orElse(null);
        if (medico == null) return false;

        if (!passwordEncoder.matches(passwordAttuale, medico.getPassword()))
            return false;

        if (nuovaPassword.length() < 8)
            throw new IllegalArgumentException("La nuova password è troppo corta");

        medico.setPassword(passwordEncoder.encode(nuovaPassword));
        utenteRepository.save(medico);

        return true;
    }

    // ---------------------------------------------------------
    // CONVERSIONE RESPONSE
    // ---------------------------------------------------------
    private MedicoResponse convertToResponse(Medico medico) {
        MedicoResponse response = new MedicoResponse();
        response.setId(medico.getId());
        response.setNomeCompleto(medico.getNome() + " " + medico.getCognome());
        response.setEmail(medico.getEmail());
        response.setSpecializzazione(medico.getSpecializzazione());
        return response;
    }
    
 // ---------------------------------------------------------
 // DELETE MEDICO (BLINDATO)
 // ---------------------------------------------------------
 @Override
 public void deleteMedico(Long id) {

     Medico medico = medicoRepository.findById(id)
             .orElseThrow(() -> new RuntimeException("Medico non trovato"));

     // Blindatura: impedisci eliminazioni non consentite
     if (medico.getEmail() == null || medico.getEmail().isBlank()) {
         throw new RuntimeException("Medico non valido");
     }

     // Se vuoi impedire eliminazioni di medici con appuntamenti futuri:
     // if (!medico.getAppuntamenti().isEmpty()) {
     //     throw new RuntimeException("Non puoi eliminare un medico con appuntamenti attivi");
     // }

     medicoRepository.delete(medico);
 }




}
