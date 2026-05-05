package com.cardiocare360.controller;

import com.cardiocare360.model.entity.Referto;
import com.cardiocare360.service.RefertoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/referti")
public class RefertoController {

    @Autowired
    private RefertoService refertoService;

    @PostMapping
    public ResponseEntity<Referto> creaReferto(
            @RequestParam Long pazienteId,
            @RequestParam Long medicoId,
            @RequestParam String titolo,
            @RequestParam String descrizione,
            @RequestParam String diagnosi,
            @RequestParam MultipartFile file) {

        Referto referto = refertoService.creaReferto(
                pazienteId, medicoId, titolo, descrizione, diagnosi, file
        );

        return ResponseEntity.ok(referto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Referto> getReferto(@PathVariable Long id) {
        return ResponseEntity.ok(refertoService.getRefertoById(id));
    }

    @GetMapping("/paziente/{pazienteId}")
    public ResponseEntity<List<Referto>> getRefertiPaziente(@PathVariable Long pazienteId) {
        return ResponseEntity.ok(refertoService.getRefertiPaziente(pazienteId));
    }

    @GetMapping("/medico/{medicoId}")
    public ResponseEntity<List<Referto>> getRefertiMedico(@PathVariable Long medicoId) {
        return ResponseEntity.ok(refertoService.getRefertiMedico(medicoId));
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long id) {
        byte[] fileBytes = refertoService.downloadFile(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=referto.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(fileBytes);
    }
}
