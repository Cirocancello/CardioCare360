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
    public ResponseEntity<Messaggio> inviaMessaggio(
            @RequestParam Long conversazioneId,
            @RequestParam Messaggio.Mittente mittente,
            @RequestParam String testo) {

        Messaggio msg = messaggioService.inviaMessaggio(conversazioneId, mittente, testo);
        return ResponseEntity.ok(msg);
    }

    // 🔹 Recupera tutti i messaggi di una conversazione
    @GetMapping("/{conversazioneId}")
    public ResponseEntity<List<Messaggio>> getMessaggi(@PathVariable Long conversazioneId) {
        return ResponseEntity.ok(messaggioService.getMessaggiConversazione(conversazioneId));
    }

    // 🔹 Segna tutti i messaggi come letti
    @PutMapping("/segna-letti/{conversazioneId}")
    public ResponseEntity<Void> segnaComeLetti(@PathVariable Long conversazioneId) {
        messaggioService.segnaComeLetti(conversazioneId);
        return ResponseEntity.ok().build();
    }
}
