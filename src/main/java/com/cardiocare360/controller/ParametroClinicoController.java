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

    // -------------------------
    // POST → Inserimento parametro
    // -------------------------
    @PostMapping
    public ResponseEntity<?> inserisciParametro(
            @PathVariable Long pazienteId,
            @RequestBody ParametroClinicoRequest request) {

        ParametroClinico parametro = parametroService.inserisciParametro(pazienteId, request);
        return ResponseEntity.ok(parametro);
    }

    // -------------------------
    // GET → Lista parametri del paziente (ordinati per data)
    // -------------------------
    @GetMapping
    public ResponseEntity<?> getParametri(@PathVariable Long pazienteId) {

        List<ParametroClinico> parametri =
                parametroRepo.findByPazienteIdOrderByDataRilevazioneDesc(pazienteId);

        return ResponseEntity.ok(parametri);
    }
}
