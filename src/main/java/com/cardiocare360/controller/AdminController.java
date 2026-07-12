package com.cardiocare360.controller;

import com.cardiocare360.model.entity.Medico;
import com.cardiocare360.model.entity.Paziente;
import com.cardiocare360.model.entity.Utente;
import com.cardiocare360.model.request.CreaMedicoRequest;
import com.cardiocare360.model.response.PazienteResponse;
import com.cardiocare360.repository.MedicoRepository;
import com.cardiocare360.repository.PazienteRepository;
import com.cardiocare360.repository.UtenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final MedicoRepository medicoRepository;
    private final PazienteRepository pazienteRepository;
    private final UtenteRepository utenteRepository;
    private final PasswordEncoder passwordEncoder;

    // LISTA MEDICI
    @GetMapping("/medici")
    public ResponseEntity<List<Medico>> getMedici() {
        return ResponseEntity.ok(medicoRepository.findAll());
    }

    // LISTA PAZIENTI
    @GetMapping("/pazienti")
    public ResponseEntity<List<Paziente>> getPazienti() {
        return ResponseEntity.ok(pazienteRepository.findAll());
    }

    // ELIMINA MEDICO
    @DeleteMapping("/medici/{id}")
    public ResponseEntity<?> deleteMedico(@PathVariable Long id) {
        medicoRepository.deleteById(id);
        utenteRepository.deleteById(id);
        return ResponseEntity.ok("MEDICO_ELIMINATO");
    }

    // CREA MEDICO
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
        return ResponseEntity.ok(medicoRepository.save(medico));
    }

    // AGGIORNA MEDICO
    @PutMapping("/medici/{id}")
    public ResponseEntity<Medico> aggiornaMedico(
            @PathVariable Long id,
            @RequestBody CreaMedicoRequest req) {

        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medico non trovato"));

        medico.setNome(req.nome());
        medico.setCognome(req.cognome());
        medico.setEmail(req.email());
        medico.setSpecializzazione(req.specializzazione());

        return ResponseEntity.ok(medicoRepository.save(medico));
    }

    // DETTAGLIO MEDICO
    @GetMapping("/medici/{id}")
    public ResponseEntity<Medico> getMedicoById(@PathVariable Long id) {
        return ResponseEntity.ok(
                medicoRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Medico non trovato"))
        );
    }

    // AGGIORNA PAZIENTE
    @PutMapping("/pazienti/{id}")
    public ResponseEntity<Paziente> aggiornaPaziente(
            @PathVariable Long id,
            @RequestBody Paziente req) {

        Paziente paziente = pazienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paziente non trovato"));

        paziente.setNome(req.getNome());
        paziente.setCognome(req.getCognome());
        paziente.setEmail(req.getEmail());
        paziente.setTelefono(req.getTelefono());
        paziente.setCodiceFiscale(req.getCodiceFiscale());
        paziente.setLuogoNascita(req.getLuogoNascita());
        paziente.setDataNascita(req.getDataNascita());
        paziente.setIndirizzo(req.getIndirizzo());

        return ResponseEntity.ok(pazienteRepository.save(paziente));
    }

    // CREA PAZIENTE
    @PostMapping("/pazienti")
    public ResponseEntity<Paziente> creaPaziente(@RequestBody Paziente req) {

        Paziente paziente = new Paziente();
        paziente.setNome(req.getNome());
        paziente.setCognome(req.getCognome());
        paziente.setEmail(req.getEmail());
        paziente.setTelefono(req.getTelefono());
        paziente.setCodiceFiscale(req.getCodiceFiscale());
        paziente.setLuogoNascita(req.getLuogoNascita());
        paziente.setDataNascita(req.getDataNascita());
        paziente.setIndirizzo(req.getIndirizzo());
        paziente.setPassword(passwordEncoder.encode(req.getPassword()));
        paziente.setRuolo(Utente.Ruolo.PAZIENTE);
        paziente.setDataRegistrazione(LocalDateTime.now());

        return ResponseEntity.ok(pazienteRepository.save(paziente));
    }

    // DETTAGLIO PAZIENTE
    @GetMapping("/pazienti/{id}")
    public ResponseEntity<PazienteResponse> getPazienteById(@PathVariable Long id) {

        Paziente paziente = pazienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paziente non trovato"));

        PazienteResponse resp = new PazienteResponse();
        resp.setId(paziente.getId());
        resp.setNome(paziente.getNome());
        resp.setCognome(paziente.getCognome());
        resp.setEmail(paziente.getEmail());
        resp.setCodiceFiscale(paziente.getCodiceFiscale());
        resp.setLuogoNascita(paziente.getLuogoNascita());
        resp.setDataNascita(paziente.getDataNascita());
        resp.setTelefono(paziente.getTelefono());
        resp.setIndirizzo(paziente.getIndirizzo());

        return ResponseEntity.ok(resp);
    }

    // 🔥 ELIMINA PAZIENTE (FUNZIONANTE)
    @DeleteMapping("/pazienti/{id}")
    @Transactional
    public ResponseEntity<?> deletePaziente(@PathVariable Long id) {

        if (!pazienteRepository.existsById(id))
            throw new RuntimeException("Paziente non trovato");

        pazienteRepository.deleteById(id);
        utenteRepository.deleteById(id);

        return ResponseEntity.ok("PAZIENTE_ELIMINATO");
    }
}
