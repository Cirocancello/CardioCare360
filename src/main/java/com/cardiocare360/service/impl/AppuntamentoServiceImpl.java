package com.cardiocare360.service.impl;

import com.cardiocare360.model.entity.Appuntamento;
import com.cardiocare360.model.entity.Medico;
import com.cardiocare360.model.entity.Paziente;
import com.cardiocare360.model.entity.Appuntamento.StatoAppuntamento;
import com.cardiocare360.model.response.AppuntamentoDTO;
import com.cardiocare360.repository.AppuntamentoRepository;
import com.cardiocare360.repository.MedicoRepository;
import com.cardiocare360.repository.PazienteRepository;
import com.cardiocare360.repository.UtenteRepository;
import com.cardiocare360.service.AppuntamentoService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class AppuntamentoServiceImpl implements AppuntamentoService {

    private final AppuntamentoRepository appuntamentoRepository;
    private final PazienteRepository pazienteRepository;
    private final MedicoRepository medicoRepository;
    private final UtenteRepository utenteRepository;

    public AppuntamentoServiceImpl(AppuntamentoRepository appuntamentoRepository,
                                   PazienteRepository pazienteRepository,
                                   MedicoRepository medicoRepository,
                                   UtenteRepository utenteRepository) {
        this.appuntamentoRepository = appuntamentoRepository;
        this.pazienteRepository = pazienteRepository;
        this.medicoRepository = medicoRepository;
        this.utenteRepository = utenteRepository;
    }

    // ---------------------------------------------------------
    // CREAZIONE APPUNTAMENTO
    // ---------------------------------------------------------
    @Override
    public AppuntamentoDTO creaAppuntamento(AppuntamentoDTO dto, Long idPaziente) {

        if (dto == null) throw new RuntimeException("Dati appuntamento mancanti");
        if (dto.getDataAppuntamento() == null) throw new RuntimeException("La data è obbligatoria");
        if (dto.getOraAppuntamento() == null) throw new RuntimeException("L'orario è obbligatorio");
        if (dto.getIdMedico() == null) throw new RuntimeException("ID medico mancante");

        Paziente paziente = pazienteRepository.findById(idPaziente)
                .orElseThrow(() -> new RuntimeException("Paziente non trovato"));

        Medico medico = medicoRepository.findById(dto.getIdMedico())
                .orElseThrow(() -> new RuntimeException("Medico non trovato"));

        if (dto.getDataAppuntamento().isBefore(LocalDate.now())) {
            throw new RuntimeException("La data deve essere futura");
        }

        if (appuntamentoRepository.existsByMedicoIdAndDataAppuntamentoAndOraAppuntamento(
                medico.getId(), dto.getDataAppuntamento(), dto.getOraAppuntamento())) {
            throw new RuntimeException("Il medico ha già un appuntamento in questo orario");
        }

        if (appuntamentoRepository.existsByPazienteIdAndDataAppuntamentoAndOraAppuntamento(
                paziente.getId(), dto.getDataAppuntamento(), dto.getOraAppuntamento())) {
            throw new RuntimeException("Hai già un appuntamento in questo orario");
        }

        Appuntamento app = new Appuntamento();
        app.setPaziente(paziente);
        app.setMedico(medico);
        app.setDataAppuntamento(dto.getDataAppuntamento());
        app.setOraAppuntamento(dto.getOraAppuntamento());
        app.setStato(StatoAppuntamento.PRENOTATO);
        app.setNote(dto.getNote());
        app.setTipoVisita(dto.getTipoVisita());

        appuntamentoRepository.save(app);

        return convertToDTO(app);
    }

    // ---------------------------------------------------------
    // GET APPUNTAMENTI PAZIENTE
    // ---------------------------------------------------------
    @Override
    public List<AppuntamentoDTO> getAppuntamentiPaziente(Long idPaziente) {
        return appuntamentoRepository.findByPazienteId(idPaziente)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ---------------------------------------------------------
    // GET APPUNTAMENTI MEDICO
    // ---------------------------------------------------------
    @Override
    public List<AppuntamentoDTO> getAppuntamentiMedico(Long idMedico) {
        return appuntamentoRepository.findByMedicoId(idMedico)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ---------------------------------------------------------
    // AGGIORNA STATO
    // ---------------------------------------------------------
    @Override
    public AppuntamentoDTO aggiornaStato(Long idAppuntamento, String nuovoStato, Long idUtente) {

        Appuntamento app = appuntamentoRepository.findById(idAppuntamento)
                .orElseThrow(() -> new RuntimeException("Appuntamento non trovato"));

        boolean isPaziente = app.getPaziente().getId().equals(idUtente);
        boolean isMedico = app.getMedico().getId().equals(idUtente);

        if (!isPaziente && !isMedico) {
            throw new RuntimeException("Non hai i permessi");
        }

        StatoAppuntamento statoEnum = StatoAppuntamento.valueOf(nuovoStato.toUpperCase());
        app.setStato(statoEnum);

        appuntamentoRepository.save(app);

        return convertToDTO(app);
    }

    // ---------------------------------------------------------
    // ELIMINA APPUNTAMENTO
    // ---------------------------------------------------------
    @Override
    public boolean eliminaAppuntamento(Long idAppuntamento, Long idUtente) {

        Appuntamento app = appuntamentoRepository.findById(idAppuntamento)
                .orElseThrow(() -> new RuntimeException("Appuntamento non trovato"));

        boolean isPaziente = app.getPaziente().getId().equals(idUtente);

        if (isPaziente && app.getDataAppuntamento().isAfter(LocalDate.now())) {
            appuntamentoRepository.delete(app);
            return true;
        }

        throw new RuntimeException("Non hai i permessi per eliminare questo appuntamento");
    }

    // ---------------------------------------------------------
    // METODI AGGIUNTIVI
    // ---------------------------------------------------------
    @Override
    public Long getIdUtenteByEmail(String email) {
        return utenteRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"))
                .getId();
    }

    @Override
    public Long getIdMedicoByEmail(String email) {
        return medicoRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Medico non trovato"))
                .getId();
    }

    // ---------------------------------------------------------
    // CONVERSIONE DTO
    // ---------------------------------------------------------
    private AppuntamentoDTO convertToDTO(Appuntamento app) {
        AppuntamentoDTO dto = new AppuntamentoDTO();

        dto.setId(app.getId());
        dto.setIdPaziente(app.getPaziente().getId());
        dto.setIdMedico(app.getMedico().getId());

        dto.setNomeMedico(app.getMedico().getNome());
        dto.setCognomeMedico(app.getMedico().getCognome());
        dto.setSpecializzazioneMedico(app.getMedico().getSpecializzazione());

        dto.setNomePaziente(app.getPaziente().getNome());
        dto.setCognomePaziente(app.getPaziente().getCognome());

        dto.setTipoVisita(app.getTipoVisita());
        dto.setDataAppuntamento(app.getDataAppuntamento());
        dto.setOraAppuntamento(app.getOraAppuntamento());
        dto.setStato(app.getStato().name());
        dto.setNote(app.getNote());

        return dto;
    }

    // ---------------------------------------------------------
    // ORARI OCCUPATI
    // ---------------------------------------------------------
    @Override
    public List<String> getOrariOccupati(Long idMedico, LocalDate data) {
        return appuntamentoRepository
                .findByMedicoIdAndDataAppuntamento(idMedico, data)
                .stream()
                .map(a -> a.getOraAppuntamento().toString())
                .toList();
    }

    // ---------------------------------------------------------
    // GET SINGOLO APPUNTAMENTO
    // ---------------------------------------------------------
    @Override
    public AppuntamentoDTO getAppuntamentoById(Long id, Long idUtente) {

        Appuntamento app = appuntamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appuntamento non trovato"));

        boolean isPaziente = Objects.equals(app.getPaziente().getId(), idUtente);
        boolean isMedico = Objects.equals(app.getMedico().getId(), idUtente);

        if (!isPaziente && !isMedico) {
            throw new RuntimeException("Accesso non autorizzato");
        }

        return convertToDTO(app);
    }

    // ---------------------------------------------------------
    // GET APPUNTAMENTI DISPONIBILI PER TERAPIA
    // ---------------------------------------------------------
    @Override
    public List<AppuntamentoDTO> getAppuntamentiDisponibili(Long idMedico) {
        return appuntamentoRepository.findAppuntamentiNonUsati(idMedico)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }



    // ---------------------------------------------------------
    // AGGIORNA APPUNTAMENTO
    // ---------------------------------------------------------
    @Override
    public AppuntamentoDTO aggiornaAppuntamento(Long id, AppuntamentoDTO dto, Long idUtente) {

        Appuntamento app = appuntamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appuntamento non trovato"));

        if (!app.getPaziente().getId().equals(idUtente)) {
            throw new RuntimeException("Non autorizzato");
        }

        app.setDataAppuntamento(dto.getDataAppuntamento());
        app.setOraAppuntamento(dto.getOraAppuntamento());
        app.setNote(dto.getNote());

        appuntamentoRepository.save(app);

        return convertToDTO(app);
    }
    
    @Override
    public void completaVisita(Long idAppuntamento) {
        Appuntamento app = appuntamentoRepository.findById(idAppuntamento)
                .orElseThrow(() -> new RuntimeException("Appuntamento non trovato"));

        app.setStato(StatoAppuntamento.COMPLETATO);  // ⭐ ENUM, non String
        appuntamentoRepository.save(app);
    }


}
