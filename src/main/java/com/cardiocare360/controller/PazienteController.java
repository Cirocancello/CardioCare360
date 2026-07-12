package com.cardiocare360.controller;

import com.cardiocare360.model.entity.Appuntamento;
import com.cardiocare360.model.entity.Terapia;
import com.cardiocare360.model.entity.ParametroClinico;
import com.cardiocare360.model.entity.Esame;
import com.cardiocare360.model.request.CambiaPasswordRequest;
import com.cardiocare360.model.request.PazienteUpdateDTO;
import com.cardiocare360.model.response.PazienteDTO;
import com.cardiocare360.model.response.PazienteResponse;
import com.cardiocare360.service.PazienteService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.cardiocare360.model.request.CambiaPasswordRequest;

@RestController
@RequestMapping("/paziente")
@RequiredArgsConstructor
public class PazienteController {

    private final PazienteService pazienteService;

    // ⭐ Admin e Medico possono visualizzare tutti i pazienti
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public ResponseEntity<List<PazienteResponse>> getAllPazienti() {
        List<PazienteDTO> lista = pazienteService.getAllPazienti();
        List<PazienteResponse> response = lista.stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    // ⭐ Profilo paziente (accessibile al paziente stesso, al medico e all’admin)
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('PAZIENTE', 'MEDICO', 'ADMIN')")
    public ResponseEntity<PazienteResponse> getProfilo(@PathVariable Long id) {
        PazienteDTO dto = pazienteService.getProfilo(id);
        return ResponseEntity.ok(toResponse(dto));
    }

    // ⭐ Aggiornamento profilo (solo il paziente può modificare i propri dati)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PAZIENTE')")
    public ResponseEntity<PazienteResponse> aggiornaProfilo(
            @PathVariable Long id,
            @RequestBody PazienteUpdateDTO request) {

        PazienteDTO dto = pazienteService.aggiornaProfilo(id, request);
        return ResponseEntity.ok(toResponse(dto));
    }

    // ⭐ Visite del paziente (visibili a paziente, medico e admin)
    @GetMapping("/{id}/visite")
    @PreAuthorize("hasAnyRole('PAZIENTE', 'MEDICO', 'ADMIN')")
    public ResponseEntity<List<Appuntamento>> getVisiteByPaziente(@PathVariable Long id) {
        return ResponseEntity.ok(pazienteService.getVisiteByPaziente(id));
    }

    // ⭐ Terapie del paziente
    @GetMapping("/{id}/terapie")
    @PreAuthorize("hasAnyRole('PAZIENTE', 'MEDICO', 'ADMIN')")
    public ResponseEntity<List<Terapia>> getTerapieByPaziente(@PathVariable Long id) {
        return ResponseEntity.ok(pazienteService.getTerapieByPaziente(id));
    }

    // ⭐ Parametri clinici del paziente
    @GetMapping("/{id}/parametri")
    @PreAuthorize("hasAnyRole('PAZIENTE', 'MEDICO', 'ADMIN')")
    public ResponseEntity<List<ParametroClinico>> getParametriByPaziente(@PathVariable Long id) {
        return ResponseEntity.ok(pazienteService.getParametriByPaziente(id));
    }

    // ⭐ Esami del paziente
    @GetMapping("/{id}/esami")
    @PreAuthorize("hasAnyRole('PAZIENTE', 'MEDICO', 'ADMIN')")
    public ResponseEntity<List<Esame>> getEsamiByPaziente(@PathVariable Long id) {
        return ResponseEntity.ok(pazienteService.getEsamiByPaziente(id));
    }

    // ⭐ Conversione DTO → Response
    private PazienteResponse toResponse(PazienteDTO dto) {
        PazienteResponse res = new PazienteResponse();
        res.setId(dto.getId());
        res.setNome(dto.getNome());
        res.setCognome(dto.getCognome());
        res.setEmail(dto.getEmail());
        res.setCodiceFiscale(dto.getCodiceFiscale());
        res.setLuogoNascita(dto.getLuogoNascita());
        res.setDataNascita(dto.getDataNascita());
        res.setTelefono(dto.getTelefono());
        res.setIndirizzo(dto.getIndirizzo());
        return res;
    }

    // ⭐ Creazione paziente (solo admin)
    @PostMapping("/crea")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PazienteResponse> creaPaziente(@RequestBody PazienteUpdateDTO request) {
        PazienteDTO dto = pazienteService.creaPaziente(request);
        return ResponseEntity.ok(toResponse(dto));
    }
    
    @PutMapping("/{id}/cambia-password")
    @PreAuthorize("hasRole('PAZIENTE')")
    public ResponseEntity<String> cambiaPassword(
            @PathVariable Long id,
            @RequestBody CambiaPasswordRequest request) {

        boolean success = pazienteService.cambiaPassword(
                id,
                request.getPasswordAttuale(),
                request.getNuovaPassword()
        );

        if (success) {
            return ResponseEntity.ok("Password aggiornata con successo!");
        } else {
            return ResponseEntity.status(400).body("Password attuale errata.");
        }
    }



}
