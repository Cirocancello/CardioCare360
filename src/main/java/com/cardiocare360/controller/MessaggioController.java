package com.cardiocare360.controller;

import com.cardiocare360.model.entity.Messaggio;
import com.cardiocare360.service.MessaggioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messaggi")
public class MessaggioController {

    private final MessaggioService messaggioService;

    public MessaggioController(MessaggioService messaggioService) {
        this.messaggioService = messaggioService;
    }

    // 🔹 Invia un messaggio
    @PostMapping("/invia")
    public ResponseEntity<?> inviaMessaggio(
            @RequestParam(required = false) Long conversazioneId,
            @RequestParam(required = false) Messaggio.Mittente mittente,
            @RequestParam(required = false) String testo) {

        try {
            if (conversazioneId == null || conversazioneId <= 0) {
                return ResponseEntity.badRequest().body("CONVERSAZIONE_ID_NON_VALIDO");
            }

            if (mittente == null) {
                return ResponseEntity.badRequest().body("MITTENTE_NON_VALIDO");
            }

            if (testo == null || testo.isBlank()) {
                return ResponseEntity.badRequest().body("TESTO_NON_VALIDO");
            }

            Messaggio msg = messaggioService.inviaMessaggio(conversazioneId, mittente, testo);
            return ResponseEntity.ok(msg);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());

        } catch (Exception e) {
            System.err.println(">>> [MSG] Errore invio messaggio: " + e.getMessage());
            return ResponseEntity.status(500).body("ERRORE_SERVER");
        }
    }

    // 🔹 Recupera tutti i messaggi di una conversazione
    @GetMapping("/{conversazioneId}")
    public ResponseEntity<?> getMessaggi(@PathVariable Long conversazioneId) {

        try {
            if (conversazioneId == null || conversazioneId <= 0) {
                return ResponseEntity.badRequest().body("CONVERSAZIONE_ID_NON_VALIDO");
            }

            List<Messaggio> lista = messaggioService.getMessaggiConversazione(conversazioneId);
            return ResponseEntity.ok(lista);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());

        } catch (Exception e) {
            System.err.println(">>> [MSG] Errore recupero messaggi: " + e.getMessage());
            return ResponseEntity.status(500).body("ERRORE_SERVER");
        }
    }

    // 🔹 Segna tutti i messaggi come letti
    @PutMapping("/segna-letti/{conversazioneId}")
    public ResponseEntity<?> segnaComeLetti(@PathVariable Long conversazioneId) {

        try {
            if (conversazioneId == null || conversazioneId <= 0) {
                return ResponseEntity.badRequest().body("CONVERSAZIONE_ID_NON_VALIDO");
            }

            messaggioService.segnaComeLetti(conversazioneId);
            return ResponseEntity.ok("MESSAGGI_SEGNATI_COME_LETTI");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());

        } catch (Exception e) {
            System.err.println(">>> [MSG] Errore segna letti: " + e.getMessage());
            return ResponseEntity.status(500).body("ERRORE_SERVER");
        }
    }
}
