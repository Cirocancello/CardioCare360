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
    public ResponseEntity<Conversazione> creaConversazione(
            @RequestParam Long pazienteId,
            @RequestParam Long medicoId) {

        Conversazione conv = conversazioneService.getOrCreateConversazione(pazienteId, medicoId);
        return ResponseEntity.ok(conv);
    }

    // 🔹 Lista conversazioni del paziente
    @GetMapping("/paziente/{pazienteId}")
    public ResponseEntity<List<Conversazione>> getConversazioniPaziente(@PathVariable Long pazienteId) {
        return ResponseEntity.ok(conversazioneService.getConversazioniPaziente(pazienteId));
    }

    // 🔹 Lista conversazioni del medico
    @GetMapping("/medico/{medicoId}")
    public ResponseEntity<List<Conversazione>> getConversazioniMedico(@PathVariable Long medicoId) {
        return ResponseEntity.ok(conversazioneService.getConversazioniMedico(medicoId));
    }

    // 🔹 Recupera conversazione specifica
    @GetMapping("/{id}")
    public ResponseEntity<Conversazione> getConversazione(@PathVariable Long id) {
        return ResponseEntity.ok(conversazioneService.getConversazione(id));
    }
}
