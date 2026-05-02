package com.cardiocare360.controller;

import com.cardiocare360.model.entity.DisponibilitaMedico;
import com.cardiocare360.model.entity.SlotOrario;
import com.cardiocare360.security.jwt.JwtUtil;
import com.cardiocare360.service.DisponibilitaMedicoService;
import com.cardiocare360.service.SlotOrarioService;
import com.cardiocare360.repository.MedicoRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/disponibilita")
@RequiredArgsConstructor
public class DisponibilitaController {

    private final DisponibilitaMedicoService disponibilitaService;
    private final SlotOrarioService slotService;
    private final JwtUtil jwtUtil;
    private final MedicoRepository medicoRepository;

    // ---------------------------------------------------------
    // 1) CREAZIONE DISPONIBILITÀ (MEDICO)
    // ---------------------------------------------------------
    @PostMapping
    public ResponseEntity<DisponibilitaMedico> creaDisponibilita(
            @RequestHeader("Authorization") String token,
            @RequestParam String giornoSettimana,
            @RequestParam String oraInizio,
            @RequestParam String oraFine
    ) {
        String email = jwtUtil.extractEmail(token.substring(7));

        Long idMedico = medicoRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Medico non trovato"))
                .getId();

        DisponibilitaMedico disp = disponibilitaService.creaDisponibilita(
                idMedico, giornoSettimana, oraInizio, oraFine
        );

        return ResponseEntity.ok(disp);
    }


    // ---------------------------------------------------------
    // 2) MODIFICA DISPONIBILITÀ
    // ---------------------------------------------------------
    @PutMapping("/{idDisponibilita}")
    public ResponseEntity<DisponibilitaMedico> modificaDisponibilita(
            @PathVariable Long idDisponibilita,
            @RequestParam String oraInizio,
            @RequestParam String oraFine
    ) {
        DisponibilitaMedico disp = disponibilitaService.modificaDisponibilita(
                idDisponibilita, oraInizio, oraFine
        );

        return ResponseEntity.ok(disp);
    }

    // ---------------------------------------------------------
    // 3) ELIMINA DISPONIBILITÀ
    // ---------------------------------------------------------
    @DeleteMapping("/{idDisponibilita}")
    public ResponseEntity<String> eliminaDisponibilita(
            @PathVariable Long idDisponibilita
    ) {
        disponibilitaService.eliminaDisponibilita(idDisponibilita);
        return ResponseEntity.ok("Disponibilità eliminata");
    }

    // ---------------------------------------------------------
    // 4) LISTA DISPONIBILITÀ DEL MEDICO (AUTENTICATO)
    // ---------------------------------------------------------
    @GetMapping("/medico")
    public ResponseEntity<List<DisponibilitaMedico>> getDisponibilitaMedico(
            @RequestHeader("Authorization") String token
    ) {
        String email = jwtUtil.extractEmail(token.substring(7));

        Long idMedico = medicoRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Medico non trovato"))
                .getId();

        return ResponseEntity.ok(disponibilitaService.getDisponibilitaMedico(idMedico));
    }

    // ---------------------------------------------------------
    // 5) GENERA SLOT PER UNA DATA (PAZIENTE)
    // ---------------------------------------------------------
    @PostMapping("/slot/{idMedico}")
    public ResponseEntity<List<SlotOrario>> generaSlot(
            @PathVariable Long idMedico,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data
    ) {
        return ResponseEntity.ok(slotService.generaSlotPerData(idMedico, data));
    }

    // ---------------------------------------------------------
    // 6) OTTIENI SLOT DISPONIBILI PER UNA DATA
    // ---------------------------------------------------------
    @GetMapping("/slot/{idMedico}")
    public ResponseEntity<List<SlotOrario>> getSlotDisponibili(
            @PathVariable Long idMedico,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data
    ) {
        return ResponseEntity.ok(slotService.getSlotDisponibili(idMedico, data));
    }

    // ---------------------------------------------------------
    // 7) PRENOTA SLOT (usato dal modulo Appuntamenti)
    // ---------------------------------------------------------
    @PutMapping("/slot/prenota/{idSlot}")
    public ResponseEntity<SlotOrario> prenotaSlot(@PathVariable Long idSlot) {
        return ResponseEntity.ok(slotService.prenotaSlot(idSlot));
    }

    // ---------------------------------------------------------
    // 8) LIBERA SLOT (in caso di cancellazione appuntamento)
    // ---------------------------------------------------------
    @PutMapping("/slot/libera/{idSlot}")
    public ResponseEntity<String> liberaSlot(@PathVariable Long idSlot) {
        slotService.liberaSlot(idSlot);
        return ResponseEntity.ok("Slot liberato");
    }
}
