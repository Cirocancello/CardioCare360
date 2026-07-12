package com.cardiocare360.controller;

import com.cardiocare360.model.request.CambiaPasswordRequest;
import com.cardiocare360.model.request.MedicoUpdateDTO;
import com.cardiocare360.model.response.MedicoResponse;
import com.cardiocare360.service.MedicoService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medico")
public class MedicoController {

    private final MedicoService medicoService;

    public MedicoController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    // ---------------------------------------------------------
    // GET MEDICO BY ID
    // ---------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<MedicoResponse> getMedico(@PathVariable Long id) {
        MedicoResponse response = medicoService.getMedicoById(id);
        return ResponseEntity.ok(response);
    }

    // ---------------------------------------------------------
    // UPDATE MEDICO
    // ---------------------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<MedicoResponse> updateMedico(
            @PathVariable Long id,
            @Valid @RequestBody MedicoUpdateDTO updateDTO) {

        MedicoResponse response = medicoService.updateMedico(id, updateDTO);
        return ResponseEntity.ok(response);
    }

    // ---------------------------------------------------------
    // GET MEDICI BY SPECIALIZZAZIONE
    // ---------------------------------------------------------
    @GetMapping("/visita/{specializzazione}")
    public ResponseEntity<List<MedicoResponse>> getMediciBySpecializzazione(
            @PathVariable String specializzazione) {

        List<MedicoResponse> medici = medicoService.getMediciBySpecializzazione(specializzazione);
        return ResponseEntity.ok(medici);
    }

    // ---------------------------------------------------------
    // GET MEDICI PER TIPO ESAME
    // ---------------------------------------------------------
    @GetMapping("/esami")
    public ResponseEntity<List<MedicoResponse>> getMediciPerTipoEsame(
            @RequestParam String tipo) {

        List<MedicoResponse> medici = medicoService.getMediciPerTipoEsame(tipo);
        return ResponseEntity.ok(medici);
    }

    // ---------------------------------------------------------
    // CAMBIO PASSWORD
    // ---------------------------------------------------------
    @PutMapping("/{id}/cambia-password")
    public ResponseEntity<String> cambiaPassword(
            @PathVariable Long id,
            @Valid @RequestBody CambiaPasswordRequest request) {

        boolean success = medicoService.cambiaPassword(
                id,
                request.getPasswordAttuale(),
                request.getNuovaPassword()
        );

        if (!success) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Password attuale errata o non autorizzato");
        }

        return ResponseEntity.ok("Password aggiornata con successo");
    }

    // ---------------------------------------------------------
    // DELETE MEDICO (BLINDATO)
    // ---------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMedico(@PathVariable Long id) {

        medicoService.deleteMedico(id); // LANCIA ECCEZIONI SE NON CONSENTITO

        return ResponseEntity.ok("Medico eliminato con successo");
    }
}
