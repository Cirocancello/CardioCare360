package com.cardiocare360.controller;

import com.cardiocare360.model.entity.Appuntamento;
import com.cardiocare360.model.entity.Terapia;
import com.cardiocare360.model.entity.ParametroClinico;
import com.cardiocare360.model.entity.Esame;
import com.cardiocare360.model.request.PazienteUpdateDTO;
import com.cardiocare360.model.response.PazienteDTO;
import com.cardiocare360.model.response.PazienteResponse;
import com.cardiocare360.service.PazienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/paziente")
@RequiredArgsConstructor
public class PazienteController {

    private final PazienteService pazienteService;

    // 🔵 Lista pazienti
    @GetMapping("/all")
    public ResponseEntity<List<PazienteResponse>> getAllPazienti() {
        List<PazienteDTO> lista = pazienteService.getAllPazienti();
        List<PazienteResponse> response = lista.stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    // 🔵 Profilo singolo paziente
    @GetMapping("/{id}")
    public ResponseEntity<PazienteResponse> getProfilo(@PathVariable Long id) {
        PazienteDTO dto = pazienteService.getProfilo(id);
        return ResponseEntity.ok(toResponse(dto));
    }

    // 🔵 Aggiornamento profilo paziente
    @PutMapping("/{id}")
    public ResponseEntity<PazienteResponse> aggiornaProfilo(
            @PathVariable Long id,
            @RequestBody PazienteUpdateDTO request) {

        PazienteDTO dto = pazienteService.aggiornaProfilo(id, request);
        return ResponseEntity.ok(toResponse(dto));
    }

    // 🔵 Storico visite del paziente
    @GetMapping("/{id}/visite")
    public ResponseEntity<List<Appuntamento>> getVisiteByPaziente(@PathVariable Long id) {
        return ResponseEntity.ok(pazienteService.getVisiteByPaziente(id));
    }

    // 🔵 Terapie attive del paziente
    @GetMapping("/{id}/terapie")
    public ResponseEntity<List<Terapia>> getTerapieByPaziente(@PathVariable Long id) {
        return ResponseEntity.ok(pazienteService.getTerapieByPaziente(id));
    }

    // 🔵 Parametri clinici del paziente
    @GetMapping("/{id}/parametri")
    public ResponseEntity<List<ParametroClinico>> getParametriByPaziente(@PathVariable Long id) {
        return ResponseEntity.ok(pazienteService.getParametriByPaziente(id));
    }

    // 🔵 Storico esami del paziente (opzionale)
    @GetMapping("/{id}/esami")
    public ResponseEntity<List<Esame>> getEsamiByPaziente(@PathVariable Long id) {
        return ResponseEntity.ok(pazienteService.getEsamiByPaziente(id));
    }

    // 🔵 Mapper DTO → Response
    private PazienteResponse toResponse(PazienteDTO dto) {
        PazienteResponse res = new PazienteResponse();
        res.setId(dto.getId());
        res.setNomeCompleto(dto.getNome() + " " + dto.getCognome());
        res.setEmail(dto.getEmail());
        res.setCodiceFiscale(dto.getCodiceFiscale());
        res.setLuogoNascita(dto.getLuogoNascita());
        res.setDataNascita(dto.getDataNascita());
        res.setTelefono(dto.getTelefono());
        res.setIndirizzo(dto.getIndirizzo());
        return res;
    }
}
