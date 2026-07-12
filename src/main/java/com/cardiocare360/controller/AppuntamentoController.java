package com.cardiocare360.controller;

import com.cardiocare360.model.response.AppuntamentoDTO;
import com.cardiocare360.repository.PazienteRepository;
import com.cardiocare360.service.AppuntamentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/appuntamenti")
public class AppuntamentoController {

    private final AppuntamentoService appuntamentoService;
    private final PazienteRepository pazienteRepository;

    public AppuntamentoController(AppuntamentoService appuntamentoService,
                                  PazienteRepository pazienteRepository) {
        this.appuntamentoService = appuntamentoService;
        this.pazienteRepository = pazienteRepository;
    }

    // ---------------------------------------------------------
    // CREA APPUNTAMENTO (solo paziente)
    // ---------------------------------------------------------
    @PostMapping
    public ResponseEntity<?> creaAppuntamento(
            @RequestBody AppuntamentoDTO dto,
            Principal principal) {

        if (dto == null)
            return ResponseEntity.badRequest().body("Dati appuntamento mancanti");

        if (dto.getDataAppuntamento() == null)
            return ResponseEntity.badRequest().body("La data è obbligatoria");

        if (dto.getOraAppuntamento() == null)
            return ResponseEntity.badRequest().body("L'orario è obbligatorio");

        if (dto.getIdMedico() == null)
            return ResponseEntity.badRequest().body("ID medico mancante");

        if (dto.getTipoVisita() == null || dto.getTipoVisita().isBlank())
            return ResponseEntity.badRequest().body("Il tipo visita è obbligatorio");

        if (dto.getNote() != null && dto.getNote().length() > 2000)
            return ResponseEntity.badRequest().body("Le note non possono superare 2000 caratteri");

        String email = principal.getName();
        Long idPaziente = pazienteRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Paziente non trovato"))
                .getId();

        AppuntamentoDTO created = appuntamentoService.creaAppuntamento(dto, idPaziente);
        return ResponseEntity.ok(created);
    }

    // ---------------------------------------------------------
    // GET APPUNTAMENTI DEL PAZIENTE
    // ---------------------------------------------------------
    @GetMapping("/paziente")
    public ResponseEntity<List<AppuntamentoDTO>> getAppuntamentiPaziente(Principal principal) {
        String email = principal.getName();
        Long idPaziente = pazienteRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Paziente non trovato"))
                .getId();

        return ResponseEntity.ok(appuntamentoService.getAppuntamentiPaziente(idPaziente));
    }

    // ---------------------------------------------------------
    // GET APPUNTAMENTI DEL MEDICO AUTENTICATO
    // ---------------------------------------------------------
    @GetMapping("/medico")
    public ResponseEntity<List<AppuntamentoDTO>> getAppuntamentiMedico(Principal principal) {
        String email = principal.getName();
        Long idMedico = appuntamentoService.getIdMedicoByEmail(email);
        return ResponseEntity.ok(appuntamentoService.getAppuntamentiMedico(idMedico));
    }

    // ---------------------------------------------------------
    // GET APPUNTAMENTI DI UN MEDICO SPECIFICO (per ID)
    // ---------------------------------------------------------
    @GetMapping("/medico/{id}")
    public ResponseEntity<List<AppuntamentoDTO>> getAppuntamentiMedicoById(@PathVariable Long id) {
        return ResponseEntity.ok(appuntamentoService.getAppuntamentiMedico(id));
    }

    // ---------------------------------------------------------
    // GET APPUNTAMENTI DISPONIBILI PER TERAPIA
    // ---------------------------------------------------------
    @GetMapping("/medico/disponibili")
    public ResponseEntity<List<AppuntamentoDTO>> getAppuntamentiDisponibili(Principal principal) {
        String email = principal.getName();
        Long idMedico = appuntamentoService.getIdMedicoByEmail(email);
        return ResponseEntity.ok(appuntamentoService.getAppuntamentiDisponibili(idMedico));
    }

    // ---------------------------------------------------------
    // AGGIORNA STATO (medico o paziente)
    // ---------------------------------------------------------
    @PutMapping("/{id}/stato")
    public ResponseEntity<?> aggiornaStato(
            @PathVariable Long id,
            @RequestParam String stato,
            Principal principal) {

        if (stato == null || stato.isBlank())
            return ResponseEntity.badRequest().body("Stato mancante");

        String email = principal.getName();
        Long idUtente = appuntamentoService.getIdUtenteByEmail(email);

        try {
            AppuntamentoDTO updated = appuntamentoService.aggiornaStato(id, stato, idUtente);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ---------------------------------------------------------
    // ELIMINA / ANNULLA APPUNTAMENTO (paziente)
    // ---------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminaAppuntamento(@PathVariable Long id, Principal principal) {
        String email = principal.getName();
        Long idUtente = appuntamentoService.getIdUtenteByEmail(email);

        try {
            boolean ok = appuntamentoService.eliminaAppuntamento(id, idUtente);
            return ok ? ResponseEntity.ok().build() : ResponseEntity.status(403).build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ---------------------------------------------------------
    // ORARI OCCUPATI DEL MEDICO
    // ---------------------------------------------------------
    @GetMapping("/occupati")
    public ResponseEntity<?> getOrariOccupati(
            @RequestParam Long idMedico,
            @RequestParam String data) {

        if (idMedico == null)
            return ResponseEntity.badRequest().body("ID medico mancante");

        if (data == null || data.isBlank())
            return ResponseEntity.badRequest().body("Data mancante");

        List<String> orari = appuntamentoService.getOrariOccupati(idMedico, LocalDate.parse(data));
        return ResponseEntity.ok(orari);
    }

    // ---------------------------------------------------------
    // GET SINGOLO APPUNTAMENTO
    // ---------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<?> getAppuntamentoById(@PathVariable Long id, Principal principal) {
        String email = principal.getName();
        Long idUtente = appuntamentoService.getIdUtenteByEmail(email);

        try {
            return ResponseEntity.ok(appuntamentoService.getAppuntamentoById(id, idUtente));
        } catch (RuntimeException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }

    // ---------------------------------------------------------
    // AGGIORNA APPUNTAMENTO (paziente)
    // ---------------------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<?> aggiornaAppuntamento(
            @PathVariable Long id,
            @RequestBody AppuntamentoDTO dto,
            Principal principal) {

        if (dto == null)
            return ResponseEntity.badRequest().body("Dati appuntamento mancanti");

        if (dto.getDataAppuntamento() == null)
            return ResponseEntity.badRequest().body("La data è obbligatoria");

        if (dto.getOraAppuntamento() == null)
            return ResponseEntity.badRequest().body("L'orario è obbligatorio");

        String email = principal.getName();
        Long idUtente = appuntamentoService.getIdUtenteByEmail(email);

        try {
            return ResponseEntity.ok(appuntamentoService.aggiornaAppuntamento(id, dto, idUtente));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
