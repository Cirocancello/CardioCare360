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
    public ResponseEntity<?> getParametriRecenti(@PathVariable Long idMedico) {

        try {
            if (idMedico == null || idMedico <= 0) {
                return ResponseEntity.badRequest().body("MEDICO_ID_NON_VALIDO");
            }

            List<ParametriRecentiDTO> lista =
                    parametroService.getParametriRecentiByMedico(idMedico);

            return ResponseEntity.ok(lista);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());

        } catch (Exception e) {
            System.err.println(">>> [PARAMETRI_MEDICO] Errore recenti: " + e.getMessage());
            return ResponseEntity.status(500).body("ERRORE_SERVER");
        }
    }

    // ---------------------------------------------------------
    // GET → Storico parametri di un paziente (visibile al medico)
    // ---------------------------------------------------------
    @GetMapping("/storico/{idPaziente}")
    public ResponseEntity<?> getStoricoParametriPaziente(
            @PathVariable Long idMedico,
            @PathVariable Long idPaziente) {

        try {
            if (idMedico == null || idMedico <= 0) {
                return ResponseEntity.badRequest().body("MEDICO_ID_NON_VALIDO");
            }

            if (idPaziente == null || idPaziente <= 0) {
                return ResponseEntity.badRequest().body("PAZIENTE_ID_NON_VALIDO");
            }

            List<ParametroClinicoStoricoDTO> storico =
                    parametroService.getStoricoParametriByPaziente(idPaziente);

            return ResponseEntity.ok(storico);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());

        } catch (Exception e) {
            System.err.println(">>> [PARAMETRI_MEDICO] Errore storico: " + e.getMessage());
            return ResponseEntity.status(500).body("ERRORE_SERVER");
        }
    }
}
