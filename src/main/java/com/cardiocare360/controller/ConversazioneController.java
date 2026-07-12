package com.cardiocare360.controller;

import com.cardiocare360.model.entity.Conversazione;
import com.cardiocare360.service.ConversazioneService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conversazioni")
public class ConversazioneController {

    private final ConversazioneService conversazioneService;

    public ConversazioneController(ConversazioneService conversazioneService) {
        this.conversazioneService = conversazioneService;
    }

    // 🔹 Crea o recupera conversazione tra paziente e medico
    @PostMapping
    public ResponseEntity<?> creaConversazione(
            @RequestParam(required = false) Long pazienteId,
            @RequestParam(required = false) Long medicoId) {

        if (pazienteId == null || medicoId == null) {
            return ResponseEntity.badRequest().body("ID_NON_VALIDI");
        }

        try {
            Conversazione conv = conversazioneService.getOrCreateConversazione(pazienteId, medicoId);
            return ResponseEntity.ok(conv);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());

        } catch (Exception e) {
            System.err.println(">>> [CONV] Errore inatteso: " + e.getMessage());
            return ResponseEntity.status(500).body("ERRORE_SERVER");
        }
    }

    // 🔹 Lista conversazioni del paziente
    @GetMapping("/paziente/{pazienteId}")
    public ResponseEntity<?> getConversazioniPaziente(@PathVariable Long pazienteId) {

        if (pazienteId == null || pazienteId <= 0) {
            return ResponseEntity.badRequest().body("PAZIENTE_ID_NON_VALIDO");
        }

        try {
            List<Conversazione> lista = conversazioneService.getConversazioniPaziente(pazienteId);
            return ResponseEntity.ok(lista);

        } catch (Exception e) {
            System.err.println(">>> [CONV] Errore lista paziente: " + e.getMessage());
            return ResponseEntity.status(500).body("ERRORE_SERVER");
        }
    }

    // 🔹 Lista conversazioni del medico
    @GetMapping("/medico/{medicoId}")
    public ResponseEntity<?> getConversazioniMedico(@PathVariable Long medicoId) {

        if (medicoId == null || medicoId <= 0) {
            return ResponseEntity.badRequest().body("MEDICO_ID_NON_VALIDO");
        }

        try {
            List<Conversazione> lista = conversazioneService.getConversazioniMedico(medicoId);
            return ResponseEntity.ok(lista);

        } catch (Exception e) {
            System.err.println(">>> [CONV] Errore lista medico: " + e.getMessage());
            return ResponseEntity.status(500).body("ERRORE_SERVER");
        }
    }

    // 🔹 Recupera conversazione specifica
    @GetMapping("/{id}")
    public ResponseEntity<?> getConversazione(@PathVariable Long id) {

        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().body("CONVERSAZIONE_ID_NON_VALIDO");
        }

        try {
            Conversazione conv = conversazioneService.getConversazione(id);

            if (conv == null) {
                return ResponseEntity.status(404).body("CONVERSAZIONE_NON_TROVATA");
            }

            return ResponseEntity.ok(conv);

        } catch (Exception e) {
            System.err.println(">>> [CONV] Errore recupero conversazione: " + e.getMessage());
            return ResponseEntity.status(500).body("ERRORE_SERVER");
        }
    }
}
