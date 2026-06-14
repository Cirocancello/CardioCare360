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
    public ResponseEntity<AppuntamentoDTO> creaAppuntamento(
            @RequestBody AppuntamentoDTO dto,
            Principal principal) {

        if (dto == null || dto.getDataAppuntamento() == null || dto.getOraAppuntamento() == null || dto.getIdMedico() == null) {
            throw new RuntimeException("Dati appuntamento incompleti");
        }

        String email = principal.getName();
        Long idPaziente = pazienteRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Paziente non trovato"))
                .getId();

        AppuntamentoDTO created = appuntamentoService.creaAppuntamento(dto, idPaziente);
        return ResponseEntity.ok(created);
    }

    // ---------------------------------------------------------
    // GET APPUNTAMENTI DEL PAZIENTE (token → email → id)
    // ---------------------------------------------------------
    @GetMapping("/paziente")
    public ResponseEntity<List<AppuntamentoDTO>> getAppuntamentiPaziente(Principal principal) {
        String email = principal.getName();
        Long idPaziente = pazienteRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Paziente non trovato"))
                .getId();

        List<AppuntamentoDTO> lista = appuntamentoService.getAppuntamentiPaziente(idPaziente);
        return ResponseEntity.ok(lista);
    }

    // ---------------------------------------------------------
    // GET APPUNTAMENTI DEL MEDICO AUTENTICATO
    // ---------------------------------------------------------
    @GetMapping("/medico")
    public ResponseEntity<List<AppuntamentoDTO>> getAppuntamentiMedico(Principal principal) {
        String email = principal.getName();
        Long idMedico = appuntamentoService.getIdMedicoByEmail(email);
        List<AppuntamentoDTO> lista = appuntamentoService.getAppuntamentiMedico(idMedico);
        return ResponseEntity.ok(lista);
    }

    // ---------------------------------------------------------
    // GET APPUNTAMENTI DI UN MEDICO SPECIFICO (per ID)
    // ---------------------------------------------------------
    @GetMapping("/medico/{id}")
    public ResponseEntity<List<AppuntamentoDTO>> getAppuntamentiMedicoById(@PathVariable Long id) {
        List<AppuntamentoDTO> lista = appuntamentoService.getAppuntamentiMedico(id);
        return ResponseEntity.ok(lista);
    }

    // ---------------------------------------------------------
    // GET APPUNTAMENTI DISPONIBILI PER CREARE UNA TERAPIA
    // ---------------------------------------------------------
    @GetMapping("/medico/disponibili")
    public ResponseEntity<List<AppuntamentoDTO>> getAppuntamentiDisponibili(Principal principal) {
        String email = principal.getName();
        Long idMedico = appuntamentoService.getIdMedicoByEmail(email);
        List<AppuntamentoDTO> lista = appuntamentoService.getAppuntamentiDisponibili(idMedico);
        return ResponseEntity.ok(lista);
    }

    // ---------------------------------------------------------
    // AGGIORNA STATO (medico o paziente)
    // ---------------------------------------------------------
    @PutMapping("/{id}/stato")
    public ResponseEntity<AppuntamentoDTO> aggiornaStato(
            @PathVariable Long id,
            @RequestParam String stato,
            Principal principal) {

        String email = principal.getName();
        Long idUtente = appuntamentoService.getIdUtenteByEmail(email);
        AppuntamentoDTO updated = appuntamentoService.aggiornaStato(id, stato, idUtente);
        return ResponseEntity.ok(updated);
    }

    // ---------------------------------------------------------
    // ELIMINA APPUNTAMENTO (paziente)
    // ---------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminaAppuntamento(@PathVariable Long id, Principal principal) {
        String email = principal.getName();
        Long idUtente = appuntamentoService.getIdUtenteByEmail(email);
        boolean ok = appuntamentoService.eliminaAppuntamento(id, idUtente);
        return ok ? ResponseEntity.ok().build() : ResponseEntity.status(403).build();
    }

    // ---------------------------------------------------------
    // ORARI OCCUPATI DEL MEDICO
    // ---------------------------------------------------------
    @GetMapping("/occupati")
    public ResponseEntity<List<String>> getOrariOccupati(
            @RequestParam Long idMedico,
            @RequestParam String data) {

        List<String> orari = appuntamentoService.getOrariOccupati(idMedico, LocalDate.parse(data));
        return ResponseEntity.ok(orari);
    }

    // ---------------------------------------------------------
    // GET SINGOLO APPUNTAMENTO (paziente o medico)
    // ---------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<AppuntamentoDTO> getAppuntamentoById(@PathVariable Long id, Principal principal) {
        String email = principal.getName();
        Long idUtente = appuntamentoService.getIdUtenteByEmail(email);
        AppuntamentoDTO dto = appuntamentoService.getAppuntamentoById(id, idUtente);
        return ResponseEntity.ok(dto);
    }

    // ---------------------------------------------------------
    // AGGIORNA APPUNTAMENTO (paziente)
    // ---------------------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<AppuntamentoDTO> aggiornaAppuntamento(
            @PathVariable Long id,
            @RequestBody AppuntamentoDTO dto,
            Principal principal) {

        String email = principal.getName();
        Long idUtente = appuntamentoService.getIdUtenteByEmail(email);
        AppuntamentoDTO updated = appuntamentoService.aggiornaAppuntamento(id, dto, idUtente);
        return ResponseEntity.ok(updated);
    }
}
