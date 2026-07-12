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
    // CREAZIONE APPUNTAMENTO (BLINDATA)
    // ---------------------------------------------------------
    @Override
    public AppuntamentoDTO creaAppuntamento(AppuntamentoDTO dto, Long idPaziente) {

        if (dto == null) throw new RuntimeException("Dati appuntamento mancanti");
        if (dto.getDataAppuntamento() == null) throw new RuntimeException("La data è obbligatoria");
        if (dto.getOraAppuntamento() == null) throw new RuntimeException("L'orario è obbligatorio");
        if (dto.getIdMedico() == null) throw new RuntimeException("ID medico mancante");
        if (dto.getTipoVisita() == null || dto.getTipoVisita().isBlank())
            throw new RuntimeException("Il tipo visita è obbligatorio");
        if (dto.getNote() != null && dto.getNote().length() > 2000)
            throw new RuntimeException("Le note non possono superare 2000 caratteri");

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
    // GET APPUNTAMENTI FUTURI PAZIENTE
    // ---------------------------------------------------------
    @Override
    public List<AppuntamentoDTO> getAppuntamentiFuturiPaziente(Long idPaziente) {
        return appuntamentoRepository.findByPazienteId(idPaziente)
                .stream()
                .filter(a -> !a.getDataAppuntamento().isBefore(LocalDate.now()))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ---------------------------------------------------------
    // GET APPUNTAMENTI FUTURI MEDICO
    // ---------------------------------------------------------
    @Override
    public List<AppuntamentoDTO> getAppuntamentiFuturiMedico(Long idMedico) {
        return appuntamentoRepository.findByMedicoId(idMedico)
                .stream()
                .filter(a -> !a.getDataAppuntamento().isBefore(LocalDate.now()))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
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
    // AGGIORNA APPUNTAMENTO (BLINDATO)
    // ---------------------------------------------------------
    @Override
    public AppuntamentoDTO aggiornaAppuntamento(Long id, AppuntamentoDTO dto, Long idUtente) {

        Appuntamento app = appuntamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appuntamento non trovato"));

        if (!app.getPaziente().getId().equals(idUtente)) {
            throw new RuntimeException("Non autorizzato");
        }

        if (app.getStato() == StatoAppuntamento.COMPLETATO ||
            app.getStato() == StatoAppuntamento.ANNULLATO) {
            throw new RuntimeException("Non è possibile modificare questo appuntamento");
        }

        if (dto.getDataAppuntamento().isBefore(LocalDate.now())) {
            throw new RuntimeException("La data deve essere futura");
        }

        if (appuntamentoRepository.existsByMedicoIdAndDataAppuntamentoAndOraAppuntamento(
                app.getMedico().getId(), dto.getDataAppuntamento(), dto.getOraAppuntamento())) {
            throw new RuntimeException("Il medico ha già un appuntamento in questo orario");
        }

        app.setDataAppuntamento(dto.getDataAppuntamento());
        app.setOraAppuntamento(dto.getOraAppuntamento());
        app.setNote(dto.getNote());

        appuntamentoRepository.save(app);

        return convertToDTO(app);
    }

    // ---------------------------------------------------------
    // CAMBIO STATO (BLINDATO)
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

        if (app.getStato() == StatoAppuntamento.COMPLETATO ||
            app.getStato() == StatoAppuntamento.ANNULLATO) {
            throw new RuntimeException("Non è possibile modificare lo stato di questo appuntamento");
        }

        StatoAppuntamento statoEnum = StatoAppuntamento.valueOf(nuovoStato.toUpperCase());
        app.setStato(statoEnum);

        appuntamentoRepository.save(app);

        return convertToDTO(app);
    }

    // ---------------------------------------------------------
    // CONFERMA APPUNTAMENTO (MEDICO)
    // ---------------------------------------------------------
    @Override
    public AppuntamentoDTO confermaAppuntamento(Long idAppuntamento, Long idMedico) {

        Appuntamento app = appuntamentoRepository.findById(idAppuntamento)
                .orElseThrow(() -> new RuntimeException("Appuntamento non trovato"));

        if (!app.getMedico().getId().equals(idMedico)) {
            throw new RuntimeException("Non autorizzato");
        }

        if (app.getStato() != StatoAppuntamento.PRENOTATO) {
            throw new RuntimeException("Puoi confermare solo appuntamenti prenotati");
        }

        app.setStato(StatoAppuntamento.CONFERMATO);
        appuntamentoRepository.save(app);

        return convertToDTO(app);
    }

    // ---------------------------------------------------------
    // COMPLETA APPUNTAMENTO (MEDICO)
    // ---------------------------------------------------------
    @Override
    public AppuntamentoDTO completaAppuntamento(Long idAppuntamento, Long idMedico) {

        Appuntamento app = appuntamentoRepository.findById(idAppuntamento)
                .orElseThrow(() -> new RuntimeException("Appuntamento non trovato"));

        if (!app.getMedico().getId().equals(idMedico)) {
            throw new RuntimeException("Non autorizzato");
        }

        if (app.getStato() == StatoAppuntamento.ANNULLATO) {
            throw new RuntimeException("Non puoi completare un appuntamento annullato");
        }

        app.setStato(StatoAppuntamento.COMPLETATO);
        appuntamentoRepository.save(app);

        return convertToDTO(app);
    }

    // ---------------------------------------------------------
    // ANNULLA APPUNTAMENTO (PAZIENTE)
    // ---------------------------------------------------------
    @Override
    public AppuntamentoDTO annullaAppuntamento(Long idAppuntamento, Long idUtente) {

        Appuntamento app = appuntamentoRepository.findById(idAppuntamento)
                .orElseThrow(() -> new RuntimeException("Appuntamento non trovato"));

        if (!app.getPaziente().getId().equals(idUtente)) {
            throw new RuntimeException("Non autorizzato");
        }

        if (app.getStato() == StatoAppuntamento.COMPLETATO) {
            throw new RuntimeException("Non puoi annullare un appuntamento completato");
        }

        app.setStato(StatoAppuntamento.ANNULLATO);
        appuntamentoRepository.save(app);

        return convertToDTO(app);
    }

    // ---------------------------------------------------------
    // ELIMINA APPUNTAMENTO (BLINDATO)
    // ---------------------------------------------------------
    @Override
    public boolean eliminaAppuntamento(Long idAppuntamento, Long idUtente) {

        Appuntamento app = appuntamentoRepository.findById(idAppuntamento)
                .orElseThrow(() -> new RuntimeException("Appuntamento non trovato"));

        boolean isPaziente = app.getPaziente().getId().equals(idUtente);

        if (!isPaziente) {
            throw new RuntimeException("Non hai i permessi per eliminare questo appuntamento");
        }

        if (app.getDataAppuntamento().isBefore(LocalDate.now())) {
            throw new RuntimeException("Non puoi eliminare un appuntamento passato");
        }

        if (app.getStato() == StatoAppuntamento.COMPLETATO) {
            throw new RuntimeException("Non puoi eliminare un appuntamento completato");
        }

        app.setStato(StatoAppuntamento.ANNULLATO);
        appuntamentoRepository.save(app);

        return true;
    }

    // ---------------------------------------------------------
    // UTILITY
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
    // ORARI OCCUPATI
    // ---------------------------------------------------------
    @Override
    public List<String> getOrariOccupati(Long idMedico, LocalDate data) {
        return appuntamentoRepository
                .findByMedicoIdAndDataAppuntamento(idMedico, data)
                .stream()
                .map(a -> a.getOraAppuntamento().toString())
                .collect(Collectors.toList());
    }

    // ---------------------------------------------------------
    // TERAPIE — APPUNTAMENTI DISPONIBILI
    // ---------------------------------------------------------
    @Override
    public List<AppuntamentoDTO> getAppuntamentiDisponibili(Long idMedico) {
        return appuntamentoRepository.findAppuntamentiNonUsati(idMedico)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
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
}
