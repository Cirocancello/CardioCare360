package com.cardiocare360.controller;

import com.cardiocare360.model.entity.Terapia;
import com.cardiocare360.service.TerapiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/terapie")
public class TerapiaController {

    @Autowired
    private TerapiaService terapiaService;

    // -------------------------
    // MEDICO
    // -------------------------

    @PostMapping
    public Terapia creaTerapia(@RequestParam Long pazienteId,
                               @RequestParam Long medicoId,
                               @RequestParam Long farmacoId,
                               @RequestParam Long appuntamentoId,
                               @RequestParam String dosaggio,
                               @RequestParam(required = false) String note,
                               @RequestParam String dataInizio,
                               @RequestParam(required = false) String dataFine) {

        return terapiaService.creaTerapia(
                pazienteId, medicoId, farmacoId, appuntamentoId,
                dosaggio, note, dataInizio, dataFine
        );
    }

    @GetMapping("/medico/{medicoId}")
    public List<Terapia> getTerapieMedico(@PathVariable Long medicoId) {
        return terapiaService.getTerapieMedico(medicoId);
    }

    // -------------------------
    // NUOVO ENDPOINT
    // -------------------------

    // 🔥 Restituisce gli ID degli appuntamenti che hanno già una terapia
    @GetMapping("/usati")
    public List<Long> getAppuntamentiUsati() {
        return terapiaService.getAppuntamentiUsati();
    }

    // -------------------------
    // PAZIENTE
    // -------------------------

    @GetMapping("/paziente/{pazienteId}")
    public List<Terapia> getTerapiePaziente(@PathVariable Long pazienteId) {
        return terapiaService.getTerapiePaziente(pazienteId);
    }

    @GetMapping("/{id}")
    public Terapia getTerapiaById(@PathVariable Long id) {
        return terapiaService.getTerapiaById(id);
    }
}
