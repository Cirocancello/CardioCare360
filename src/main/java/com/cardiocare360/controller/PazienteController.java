package com.cardiocare360.controller;

import com.cardiocare360.model.dto.PazienteDTO;
import com.cardiocare360.model.dto.PazienteUpdateDTO;
import com.cardiocare360.model.response.PazienteResponse;
import com.cardiocare360.service.PazienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/paziente")
public class PazienteController {

    @Autowired
    private PazienteService pazienteService;

    @GetMapping("/{id}")
    public ResponseEntity<PazienteResponse> getProfilo(@PathVariable Long id) {
        PazienteDTO dto = pazienteService.getProfilo(id);
        return ResponseEntity.ok(toResponse(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PazienteResponse> aggiornaProfilo(
            @PathVariable Long id,
            @RequestBody PazienteUpdateDTO request) {

        PazienteDTO dto = pazienteService.aggiornaProfilo(id, request);
        return ResponseEntity.ok(toResponse(dto));
    }

    private PazienteResponse toResponse(PazienteDTO dto) {
        PazienteResponse res = new PazienteResponse();
        res.setId(dto.getId());
        res.setNomeCompleto(dto.getNome() + " " + dto.getCognome());
        res.setEmail(dto.getEmail());
        res.setCodiceFiscale(dto.getCodiceFiscale());
        res.setDataNascita(dto.getDataNascita());
        res.setTelefono(dto.getTelefono());
        res.setIndirizzo(dto.getIndirizzo());
        return res;
    }
}
