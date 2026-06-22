package com.cardiocare360.controller;

import com.cardiocare360.model.entity.Medico;
import com.cardiocare360.model.entity.Paziente;
import com.cardiocare360.repository.MedicoRepository;
import com.cardiocare360.repository.PazienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final MedicoRepository medicoRepository;
    private final PazienteRepository pazienteRepository;

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
}
