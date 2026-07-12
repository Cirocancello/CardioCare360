package com.cardiocare360.controller;

import com.cardiocare360.model.entity.Terapia;
import com.cardiocare360.service.TerapiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/terapie")
public class TerapiaController {

    @Autowired
    private TerapiaService terapiaService;

    // ---------------------------------------------------------
    // 1) CREAZIONE TERAPIA (MEDICO)
    // ---------------------------------------------------------
    @PostMapping
    public ResponseEntity<?> creaTerapia(
            @RequestParam(required = false) Long pazienteId,
            @RequestParam(required = false) Long medicoId,
            @RequestParam(required = false) Long farmacoId,
            @RequestParam(required = false) Long appuntamentoId,
            @RequestParam(required = false) String dosaggio,
            @RequestParam(required = false) String note,
            @RequestParam(required = false) String dataInizio,
            @RequestParam(required = false) String dataFine) {

        try {
            if (pazienteId == null || pazienteId <= 0)
                return ResponseEntity.badRequest().body("PAZIENTE_ID_NON_VALIDO");

            if (medicoId == null || medicoId <= 0)
                return ResponseEntity.badRequest().body("MEDICO_ID_NON_VALIDO");

            if (farmacoId == null || farmacoId <= 0)
                return ResponseEntity.badRequest().body("FARMACO_ID_NON_VALIDO");

            if (appuntamentoId == null || appuntamentoId <= 0)
                return ResponseEntity.badRequest().body("APPUNTAMENTO_ID_NON_VALIDO");

            if (dosaggio == null || dosaggio.isBlank())
                return ResponseEntity.badRequest().body("DOSAGGIO_OBBLIGATORIO");

            if (dataInizio == null || dataInizio.isBlank())
                return ResponseEntity.badRequest().body("DATA_INIZIO_OBBLIGATORIA");

            Terapia terapia = terapiaService.creaTerapia(
                    pazienteId, medicoId, farmacoId, appuntamentoId,
                    dosaggio, note, dataInizio, dataFine
            );

            return ResponseEntity.ok(terapia);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());

        } catch (Exception e) {
            System.err.println(">>> [TERAPIA] Errore creazione: " + e.getMessage());
            return ResponseEntity.status(500).body("ERRORE_SERVER");
        }
    }

    // ---------------------------------------------------------
    // 2) TERAPIE DEL MEDICO
    // ---------------------------------------------------------
    @GetMapping("/medico/{medicoId}")
    public ResponseEntity<?> getTerapieMedico(@PathVariable Long medicoId) {
        try {
            if (medicoId == null || medicoId <= 0)
                return ResponseEntity.badRequest().body("MEDICO_ID_NON_VALIDO");

            List<Terapia> lista = terapiaService.getTerapieMedico(medicoId);
            return ResponseEntity.ok(lista);

        } catch (Exception e) {
            System.err.println(">>> [TERAPIA] Errore get medico: " + e.getMessage());
            return ResponseEntity.status(500).body("ERRORE_SERVER");
        }
    }

    // ---------------------------------------------------------
    // 3) APPUNTAMENTI GIÀ USATI PER TERAPIE
    // ---------------------------------------------------------
    @GetMapping("/usati")
    public ResponseEntity<?> getAppuntamentiUsati() {
        try {
            List<Long> ids = terapiaService.getAppuntamentiUsati();
            return ResponseEntity.ok(ids);

        } catch (Exception e) {
            System.err.println(">>> [TERAPIA] Errore appuntamenti usati: " + e.getMessage());
            return ResponseEntity.status(500).body("ERRORE_SERVER");
        }
    }

    // ---------------------------------------------------------
    // 4) TERAPIE DEL PAZIENTE
    // ---------------------------------------------------------
    @GetMapping("/paziente/{pazienteId}")
    public ResponseEntity<?> getTerapiePaziente(@PathVariable Long pazienteId) {
        try {
            if (pazienteId == null || pazienteId <= 0)
                return ResponseEntity.badRequest().body("PAZIENTE_ID_NON_VALIDO");

            List<Terapia> lista = terapiaService.getTerapiePaziente(pazienteId);
            return ResponseEntity.ok(lista);

        } catch (Exception e) {
            System.err.println(">>> [TERAPIA] Errore get paziente: " + e.getMessage());
            return ResponseEntity.status(500).body("ERRORE_SERVER");
        }
    }

    // ---------------------------------------------------------
    // 5) TERAPIA SINGOLA
    // ---------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<?> getTerapiaById(@PathVariable Long id) {
        try {
            if (id == null || id <= 0)
                return ResponseEntity.badRequest().body("TERAPIA_ID_NON_VALIDO");

            Terapia terapia = terapiaService.getTerapiaById(id);

            if (terapia == null)
                return ResponseEntity.status(404).body("TERAPIA_NON_TROVATA");

            return ResponseEntity.ok(terapia);

        } catch (Exception e) {
            System.err.println(">>> [TERAPIA] Errore get by id: " + e.getMessage());
            return ResponseEntity.status(500).body("ERRORE_SERVER");
        }
    }
}
