package com.cardiocare360.controller;

import com.cardiocare360.model.entity.Terapia;
import com.cardiocare360.service.TerapiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/terapie")
@CrossOrigin(origins = "*")
public class TerapiaController {

    @Autowired
    private TerapiaService terapiaService;

    // Creazione terapia
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
                pazienteId,
                medicoId,
                farmacoId,
                appuntamentoId,
                dosaggio,
                note,
                dataInizio,
                dataFine
        );
    }

    // Recupero terapia singola
    @GetMapping("/{id}")
    public Terapia getTerapiaById(@PathVariable Long id) {
        return terapiaService.getTerapiaById(id);
    }

    // Lista terapie del paziente
    @GetMapping("/paziente/{pazienteId}")
    public List<Terapia> getTerapiePaziente(@PathVariable Long pazienteId) {
        return terapiaService.getTerapiePaziente(pazienteId);
    }

    // Lista terapie del medico
    @GetMapping("/medico/{medicoId}")
    public List<Terapia> getTerapieMedico(@PathVariable Long medicoId) {
        return terapiaService.getTerapieMedico(medicoId);
    }
}
