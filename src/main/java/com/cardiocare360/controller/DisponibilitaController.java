package com.cardiocare360.controller;

import com.cardiocare360.model.entity.DisponibilitaMedico;
import com.cardiocare360.model.entity.Medico;
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
    public ResponseEntity<?> creaDisponibilita(
            @RequestHeader("Authorization") String token,
            @RequestBody DisponibilitaMedico disponibilita
    ) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body("TOKEN_NON_VALIDO");
            }

            if (disponibilita == null) {
                return ResponseEntity.badRequest().body("DISPONIBILITA_NULL");
            }

            String email = jwtUtil.extractEmail(token.substring(7));

            Medico medico = medicoRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("MEDICO_NON_TROVATO"));

            disponibilita.setMedico(medico);

            DisponibilitaMedico salvata = disponibilitaService.salva(disponibilita);
            return ResponseEntity.ok(salvata);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());

        } catch (Exception e) {
            System.err.println(">>> [DISP] Errore inatteso: " + e.getMessage());
            return ResponseEntity.status(500).body("ERRORE_SERVER");
        }
    }

    // ---------------------------------------------------------
    // 2) MODIFICA DISPONIBILITÀ
    // ---------------------------------------------------------
    @PutMapping("/{idDisponibilita}")
    public ResponseEntity<?> modificaDisponibilita(
            @PathVariable Long idDisponibilita,
            @RequestParam String oraInizio,
            @RequestParam String oraFine
    ) {
        try {
            if (idDisponibilita == null || idDisponibilita <= 0) {
                return ResponseEntity.badRequest().body("ID_NON_VALIDO");
            }

            DisponibilitaMedico disp = disponibilitaService.modificaDisponibilita(
                    idDisponibilita, oraInizio, oraFine
            );

            return ResponseEntity.ok(disp);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());

        } catch (Exception e) {
            System.err.println(">>> [DISP] Errore modifica: " + e.getMessage());
            return ResponseEntity.status(500).body("ERRORE_SERVER");
        }
    }

    // ---------------------------------------------------------
    // 3) ELIMINA DISPONIBILITÀ
    // ---------------------------------------------------------
    @DeleteMapping("/{idDisponibilita}")
    public ResponseEntity<?> eliminaDisponibilita(@PathVariable Long idDisponibilita) {
        try {
            if (idDisponibilita == null || idDisponibilita <= 0) {
                return ResponseEntity.badRequest().body("ID_NON_VALIDO");
            }

            disponibilitaService.eliminaDisponibilita(idDisponibilita);
            return ResponseEntity.ok("DISPONIBILITA_ELIMINATA");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());

        } catch (Exception e) {
            System.err.println(">>> [DISP] Errore eliminazione: " + e.getMessage());
            return ResponseEntity.status(500).body("ERRORE_SERVER");
        }
    }

    // ---------------------------------------------------------
    // 4) LISTA DISPONIBILITÀ DEL MEDICO (AUTENTICATO)
    // ---------------------------------------------------------
    @GetMapping("/medico")
    public ResponseEntity<?> getDisponibilitaMedico(
            @RequestHeader("Authorization") String token
    ) {
        try {
            if (token == null || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body("TOKEN_NON_VALIDO");
            }

            String email = jwtUtil.extractEmail(token.substring(7));

            Long idMedico = medicoRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("MEDICO_NON_TROVATO"))
                    .getId();

            return ResponseEntity.ok(disponibilitaService.getDisponibilitaMedico(idMedico));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());

        } catch (Exception e) {
            System.err.println(">>> [DISP] Errore lista medico: " + e.getMessage());
            return ResponseEntity.status(500).body("ERRORE_SERVER");
        }
    }

    // ---------------------------------------------------------
    // 5) GENERA SLOT PER UNA DATA (PAZIENTE)
    // ---------------------------------------------------------
    @PostMapping("/slot/{idMedico}")
    public ResponseEntity<?> generaSlot(
            @PathVariable Long idMedico,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data
    ) {
        try {
            if (idMedico == null || idMedico <= 0) {
                return ResponseEntity.badRequest().body("MEDICO_ID_NON_VALIDO");
            }

            if (data == null) {
                return ResponseEntity.badRequest().body("DATA_NON_VALIDA");
            }

            return ResponseEntity.ok(slotService.generaSlotPerData(idMedico, data));

        } catch (Exception e) {
            System.err.println(">>> [DISP] Errore genera slot: " + e.getMessage());
            return ResponseEntity.status(500).body("ERRORE_SERVER");
        }
    }

    // ---------------------------------------------------------
    // 6) OTTIENI SLOT DISPONIBILI PER UNA DATA
    // ---------------------------------------------------------
    @GetMapping("/slot/{idMedico}")
    public ResponseEntity<?> getSlotDisponibili(
            @PathVariable Long idMedico,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data
    ) {
        try {
            if (idMedico == null || idMedico <= 0) {
                return ResponseEntity.badRequest().body("MEDICO_ID_NON_VALIDO");
            }

            if (data == null) {
                return ResponseEntity.badRequest().body("DATA_NON_VALIDA");
            }

            return ResponseEntity.ok(slotService.getSlotDisponibili(idMedico, data));

        } catch (Exception e) {
            System.err.println(">>> [DISP] Errore get slot: " + e.getMessage());
            return ResponseEntity.status(500).body("ERRORE_SERVER");
        }
    }

    // ---------------------------------------------------------
    // 7) PRENOTA SLOT
    // ---------------------------------------------------------
    @PutMapping("/slot/prenota/{idSlot}")
    public ResponseEntity<?> prenotaSlot(@PathVariable Long idSlot) {
        try {
            if (idSlot == null || idSlot <= 0) {
                return ResponseEntity.badRequest().body("SLOT_ID_NON_VALIDO");
            }

            return ResponseEntity.ok(slotService.prenotaSlot(idSlot));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());

        } catch (Exception e) {
            System.err.println(">>> [DISP] Errore prenota slot: " + e.getMessage());
            return ResponseEntity.status(500).body("ERRORE_SERVER");
        }
    }

    // ---------------------------------------------------------
    // 8) LIBERA SLOT
    // ---------------------------------------------------------
    @PutMapping("/slot/libera/{idSlot}")
    public ResponseEntity<?> liberaSlot(@PathVariable Long idSlot) {
        try {
            if (idSlot == null || idSlot <= 0) {
                return ResponseEntity.badRequest().body("SLOT_ID_NON_VALIDO");
            }

            slotService.liberaSlot(idSlot);
            return ResponseEntity.ok("SLOT_LIBERATO");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());

        } catch (Exception e) {
            System.err.println(">>> [DISP] Errore libera slot: " + e.getMessage());
            return ResponseEntity.status(500).body("ERRORE_SERVER");
        }
    }

    // ---------------------------------------------------------
    // 9) DATE DISPONIBILI PER TIPO VISITA
    // ---------------------------------------------------------
    @GetMapping("/date/visita/{tipoVisita}")
    public ResponseEntity<?> getDateDisponibiliPerVisita(@PathVariable String tipoVisita) {
        try {
            if (tipoVisita == null || tipoVisita.isBlank()) {
                return ResponseEntity.badRequest().body("TIPO_VISITA_NON_VALIDO");
            }

            List<String> date = disponibilitaService.generaDateDisponibiliPerSpecializzazione(tipoVisita);
            return ResponseEntity.ok(date);

        } catch (Exception e) {
            System.err.println(">>> [DISP] Errore date visita: " + e.getMessage());
            return ResponseEntity.status(500).body("ERRORE_SERVER");
        }
    }

    // ---------------------------------------------------------
    // 10) DATE DISPONIBILI PER MEDICO
    // ---------------------------------------------------------
    @GetMapping("/date/medico/{idMedico}")
    public ResponseEntity<?> getDateDisponibiliPerMedico(@PathVariable Long idMedico) {
        try {
            if (idMedico == null || idMedico <= 0) {
                return ResponseEntity.badRequest().body("MEDICO_ID_NON_VALIDO");
            }

            List<String> date = disponibilitaService.generaDateDisponibili(idMedico);
            return ResponseEntity.ok(date);

        } catch (Exception e) {
            System.err.println(">>> [DISP] Errore date medico: " + e.getMessage());
            return ResponseEntity.status(500).body("ERRORE_SERVER");
        }
    }
}
