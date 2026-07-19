package com.cardiocare360.controller;

import com.cardiocare360.model.entity.ParametroClinico;
import com.cardiocare360.model.request.ParametroClinicoRequest;
import com.cardiocare360.model.response.AlertDTO;
import com.cardiocare360.service.ParametroClinicoService;
import com.cardiocare360.repository.ParametroClinicoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cardiocare360.model.response.ResponseWrapper;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/pazienti/{pazienteId}/parametri")
public class ParametroClinicoController {

    @Autowired
    private ParametroClinicoService parametroService;

    @Autowired
    private ParametroClinicoRepository parametroRepo;

    // -------------------------
    // POST → Inserimento parametri vitali multipli
    // -------------------------
    @PostMapping
    public ResponseEntity<?> inserisciParametro(
            @PathVariable Long pazienteId,
            @RequestBody ParametroClinicoRequest request) {

        // 1) Inserisci i parametri
        List<ParametroClinico> parametri = parametroService.inserisciParametro(pazienteId, request);

        // 2) Calcolo alert sul parametro appena inserito
        // 2️⃣ Calcola alert per ciascun parametro
        List<AlertDTO> alerts = new ArrayList<>();

        for (ParametroClinico p : parametri) {
            String alertString = parametroService.checkParametroFuoriSoglia(p);
            if (alertString != null) {
                alerts.add(new AlertDTO(alertString, "warning"));
                p.setAlert(alertString);
            }
        }
        // 3) Risposta con wrapper
        return ResponseEntity.ok(new ResponseWrapper<>(parametri, alerts.isEmpty() ? null : alerts));
    }

    // -------------------------
    // GET → Lista parametri del paziente (ordinati per data)
    // -------------------------
    @GetMapping
    public ResponseEntity<List<ParametroClinico>> getParametri(@PathVariable Long pazienteId) {

        List<ParametroClinico> parametri =
                parametroRepo.findByPazienteIdOrderByDataRilevazioneDesc(pazienteId);

        return ResponseEntity.ok(parametri);
    }
}
