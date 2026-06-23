package com.cardiocare360.controller;

import com.cardiocare360.model.entity.Medico;
import com.cardiocare360.model.entity.Paziente;
import com.cardiocare360.model.entity.Utente;
import com.cardiocare360.model.request.CreaMedicoRequest;
import com.cardiocare360.repository.MedicoRepository;
import com.cardiocare360.repository.PazienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final MedicoRepository medicoRepository;
    private final PazienteRepository pazienteRepository;
    private final PasswordEncoder passwordEncoder;

    // 🔥 LISTA MEDICI
    @GetMapping("/medici")
    public ResponseEntity<List<Medico>> getMedici() {
        return ResponseEntity.ok(medicoRepository.findAll());
    }

    // 🔥 LISTA PAZIENTI
    @GetMapping("/pazienti")
    public ResponseEntity<List<Paziente>> getPazienti() {
        return ResponseEntity.ok(pazienteRepository.findAll());
    }

    // 🔥 ELIMINA MEDICO
    @DeleteMapping("/medici/{id}")
    public ResponseEntity<?> deleteMedico(@PathVariable Long id) {
        medicoRepository.deleteById(id);
        return ResponseEntity.ok("MEDICO_ELIMINATO");
    }

    // 🔥 ELIMINA PAZIENTE
    @DeleteMapping("/pazienti/{id}")
    public ResponseEntity<?> deletePaziente(@PathVariable Long id) {
        pazienteRepository.deleteById(id);
        return ResponseEntity.ok("PAZIENTE_ELIMINATO");
    }
    
 // 🔥 CREA MEDICO
    @PostMapping("/medici")
    public ResponseEntity<Medico> creaMedico(@RequestBody CreaMedicoRequest req) {
        Medico medico = new Medico();
        medico.setNome(req.nome());
        medico.setCognome(req.cognome());
        medico.setEmail(req.email());
        medico.setPassword(passwordEncoder.encode(req.password()));
        medico.setRuolo(Utente.Ruolo.MEDICO);
        medico.setDataRegistrazione(LocalDateTime.now());
        medico.setSpecializzazione(req.specializzazione());
        

        Medico salvato = medicoRepository.save(medico);
        return ResponseEntity.ok(salvato);
    }
    
    @PutMapping("/medici/{id}")
    public ResponseEntity<Medico> aggiornaMedico(
            @PathVariable Long id,
            @RequestBody CreaMedicoRequest req) {

        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medico non trovato"));

        // Campi ereditati da Utente
        medico.setNome(req.nome());
        medico.setCognome(req.cognome());
        medico.setEmail(req.email());

        // Se vuoi permettere il cambio password:
        // medico.setPassword(passwordEncoder.encode(req.password()));

        // Campi specifici di Medico
        medico.setSpecializzazione(req.specializzazione());

        Medico aggiornato = medicoRepository.save(medico);
        return ResponseEntity.ok(aggiornato);
    }

    @GetMapping("/medici/{id}")
    public ResponseEntity<Medico> getMedicoById(@PathVariable Long id) {

        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medico non trovato"));

        return ResponseEntity.ok(medico);
    }





}
