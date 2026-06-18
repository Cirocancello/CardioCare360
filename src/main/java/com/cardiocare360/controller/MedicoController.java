package com.cardiocare360.controller;

import com.cardiocare360.model.request.CambiaPasswordRequest;
import com.cardiocare360.model.request.MedicoUpdateDTO;
import com.cardiocare360.model.response.MedicoResponse;
import com.cardiocare360.service.MedicoService;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/medico")
public class MedicoController {

    private final MedicoService medicoService;

    public MedicoController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    @GetMapping("/{id}")
    public MedicoResponse getMedico(@PathVariable Long id) {
        return medicoService.getMedicoById(id);
    }

    @PutMapping("/{id}")
    public MedicoResponse updateMedico(@PathVariable Long id,
                                       @RequestBody MedicoUpdateDTO updateDTO) {
        return medicoService.updateMedico(id, updateDTO);
    }
    
    @GetMapping("/visita/{specializzazione}")
    public List<MedicoResponse> getMediciBySpecializzazione(@PathVariable String specializzazione) {
        return medicoService.getMediciBySpecializzazione(specializzazione);
    }

    // 🔥 NUOVO ENDPOINT: medici per tipo di esame
    @GetMapping("/esami")
    public List<MedicoResponse> getMediciPerTipoEsame(@RequestParam String tipo) {
        return medicoService.getMediciPerTipoEsame(tipo);
    }
    
    @PutMapping("/{id}/cambia-password")
    public ResponseEntity<String> cambiaPassword(
            @PathVariable Long id,
            @RequestBody CambiaPasswordRequest request) {

        boolean success = medicoService.cambiaPassword(id, request.getPasswordAttuale(), request.getNuovaPassword());

        if (!success) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Password attuale errata o non autorizzato");
        }

        return ResponseEntity.ok("Password aggiornata con successo");
    }

}
