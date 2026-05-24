package com.cardiocare360.controller;

import com.cardiocare360.model.response.DisponibilitaEsameResponse;
import com.cardiocare360.model.response.EsameDTO;
import com.cardiocare360.model.response.RefertoDTO;
import com.cardiocare360.service.EsameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/esami")
@CrossOrigin(origins = "*")
public class EsameController {

    @Autowired
    private EsameService esameService;

    // Prenotazione esame
    @PostMapping("/prenota")
    public EsameDTO prenotaEsame(@RequestBody EsameDTO dto) {
        return esameService.creaEsame(dto);
    }


    // Lista esami del paziente
    @GetMapping("/paziente/{idPaziente}")
    public List<EsameDTO> getEsamiPaziente(@PathVariable Long idPaziente) {
        return esameService.getEsamiPaziente(idPaziente);
    }

    // Lista esami del medico (per refertazione)
    @GetMapping("/medico/{idMedico}")
    public List<EsameDTO> getEsamiMedico(@PathVariable Long idMedico) {
        return esameService.getEsamiMedico(idMedico);
    }

    // Dettaglio esame
    @GetMapping("/{idEsame}")
    public EsameDTO getEsameById(@PathVariable Long idEsame) {
        return esameService.getEsameById(idEsame);
    }

    // Aggiornamento stato esame (PRENOTATO → COMPLETATO → REFERTATO)
    @PutMapping("/{idEsame}/stato")
    public EsameDTO aggiornaStatoEsame(
            @PathVariable Long idEsame,
            @RequestParam String nuovoStato
    ) {
        return esameService.aggiornaStatoEsame(idEsame, nuovoStato);
    }

    // Eliminazione esame
    @DeleteMapping("/{idEsame}")
    public void eliminaEsame(@PathVariable Long idEsame) {
        esameService.eliminaEsame(idEsame);
    }
    
    @GetMapping("/{idEsame}/referto")
    public RefertoDTO getReferto(@PathVariable Long idEsame) {
        return esameService.getRefertoByEsame(idEsame);
    }

    @GetMapping("/disponibilita/prossima")
    public ResponseEntity<DisponibilitaEsameResponse> getProssimaDisponibilita(
            @RequestParam String tipo
    ) {
        DisponibilitaEsameResponse disponibilita = esameService.calcolaProssimaDisponibilita(tipo);
        return ResponseEntity.ok(disponibilita);
    }

}
