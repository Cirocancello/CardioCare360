package com.cardiocare360.controller;

import com.cardiocare360.model.response.ParametriRecentiDTO;
import com.cardiocare360.model.response.ParametroClinicoStoricoDTO;
import com.cardiocare360.service.ParametroClinicoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medici/{idMedico}/parametri")
public class ParametroClinicoMedicoController {

    private final ParametroClinicoService parametroService;

    public ParametroClinicoMedicoController(ParametroClinicoService parametroService) {
        this.parametroService = parametroService;
    }

    // ---------------------------------------------------------
    // GET → Ultimi parametri dei pazienti del medico
    // ---------------------------------------------------------
    @GetMapping("/recenti")
    public ResponseEntity<List<ParametriRecentiDTO>> getParametriRecenti(
            @PathVariable Long idMedico) {

        List<ParametriRecentiDTO> lista = parametroService.getParametriRecentiByMedico(idMedico);
        return ResponseEntity.ok(lista);
    }

    // ---------------------------------------------------------
    // GET → Storico parametri di un paziente (visibile al medico)
    // ---------------------------------------------------------
    @GetMapping("/storico/{idPaziente}")
    public ResponseEntity<List<ParametroClinicoStoricoDTO>> getStoricoParametriPaziente(
            @PathVariable Long idMedico,
            @PathVariable Long idPaziente) {

        List<ParametroClinicoStoricoDTO> storico =
                parametroService.getStoricoParametriByPaziente(idPaziente);

        return ResponseEntity.ok(storico);
    }
}
