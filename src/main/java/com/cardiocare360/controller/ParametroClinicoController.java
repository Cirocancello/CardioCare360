package com.cardiocare360.controller;

import com.cardiocare360.model.entity.ParametroClinico;
import com.cardiocare360.model.request.ParametroClinicoRequest;
import com.cardiocare360.service.ParametroClinicoService;
import com.cardiocare360.repository.ParametroClinicoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pazienti/{pazienteId}/parametri")
public class ParametroClinicoController {

    @Autowired
    private ParametroClinicoService parametroService;

    @Autowired
    private ParametroClinicoRepository parametroRepo;

    // ---------------------------------------------------------
    // POST → Inserimento parametri vitali multipli
    // ---------------------------------------------------------
    @PostMapping
    public ResponseEntity<?> inserisciParametro(
            @PathVariable Long pazienteId,
            @RequestBody(required = false) ParametroClinicoRequest request) {

        try {
            if (pazienteId == null || pazienteId <= 0) {
                return ResponseEntity.badRequest().body("PAZIENTE_ID_NON_VALIDO");
            }

            if (request == null) {
                return ResponseEntity.badRequest().body("REQUEST_NULL");
            }

            List<ParametroClinico> parametri = parametroService.inserisciParametro(pazienteId, request);
            return ResponseEntity.ok(parametri);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());

        } catch (Exception e) {
            System.err.println(">>> [PARAMETRI] Errore inserimento: " + e.getMessage());
            return ResponseEntity.status(500).body("ERRORE_SERVER");
        }
    }

    // ---------------------------------------------------------
    // GET → Lista parametri del paziente (ordinati per data)
    // ---------------------------------------------------------
    @GetMapping
    public ResponseEntity<?> getParametri(@PathVariable Long pazienteId) {

        try {
            if (pazienteId == null || pazienteId <= 0) {
                return ResponseEntity.badRequest().body("PAZIENTE_ID_NON_VALIDO");
            }

            List<ParametroClinico> parametri =
                    parametroRepo.findByPazienteIdOrderByDataRilevazioneDesc(pazienteId);

            if (parametri == null) {
                return ResponseEntity.ok(List.of()); // lista vuota, nessun errore
            }

            return ResponseEntity.ok(parametri);

        } catch (Exception e) {
            System.err.println(">>> [PARAMETRI] Errore recupero: " + e.getMessage());
            return ResponseEntity.status(500).body("ERRORE_SERVER");
        }
    }
}
