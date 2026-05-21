package com.cardiocare360.controller;

import com.cardiocare360.model.response.AppuntamentoDTO;
import com.cardiocare360.repository.PazienteRepository;
import com.cardiocare360.service.AppuntamentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

import java.time.LocalDate;


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

        // 🔥 Controllo aggiuntivo per evitare DTO null o incompleti
        if (dto == null || dto.getDataAppuntamento() == null) {
            throw new RuntimeException("Campo dataAppuntamento mancante nel body");
        }

        if (dto.getOraAppuntamento() == null) {
            throw new RuntimeException("Campo oraAppuntamento mancante nel body");
        }

        if (dto.getIdMedico() == null) {
            throw new RuntimeException("Campo idMedico mancante nel body");
        }

        // 🔐 Recupero email dal token
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
    // GET APPUNTAMENTI DEL MEDICO (token → email → id)
    // ---------------------------------------------------------
    @GetMapping("/medico")
    public ResponseEntity<List<AppuntamentoDTO>> getAppuntamentiMedico(Principal principal) {

        String email = principal.getName();

        Long idMedico = appuntamentoService.getIdMedicoByEmail(email);

        List<AppuntamentoDTO> lista = appuntamentoService.getAppuntamentiMedico(idMedico);
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
    public ResponseEntity<Void> eliminaAppuntamento(
            @PathVariable Long id,
            Principal principal) {

        String email = principal.getName();

        Long idUtente = appuntamentoService.getIdUtenteByEmail(email);

        boolean ok = appuntamentoService.eliminaAppuntamento(id, idUtente);

        if (ok) return ResponseEntity.ok().build();
        return ResponseEntity.status(403).build();
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

}
