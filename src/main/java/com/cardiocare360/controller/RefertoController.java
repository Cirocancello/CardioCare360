package com.cardiocare360.controller;

import com.cardiocare360.model.response.RefertoDTO;
import com.cardiocare360.service.RefertoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/referti")
@CrossOrigin(origins = "*")
public class RefertoController {

    @Autowired
    private RefertoService refertoService;

    // Upload referto per un esame
    @PostMapping("/upload")
    public ResponseEntity<RefertoDTO> uploadReferto(
            @RequestParam Long esameId,
            @RequestParam Long medicoId,
            @RequestParam String noteMedico,
            @RequestParam MultipartFile file
    ) {
        RefertoDTO dto = refertoService.uploadReferto(esameId, medicoId, noteMedico, file);
        return ResponseEntity.ok(dto);
    }

    // Recupera referto tramite esame
    @GetMapping("/esame/{esameId}")
    public ResponseEntity<RefertoDTO> getRefertoByEsame(@PathVariable Long esameId) {
        RefertoDTO dto = refertoService.getRefertoByEsame(esameId);
        return ResponseEntity.ok(dto);
    }

    // Download PDF referto
    @GetMapping("/download/{refertoId}")
    public ResponseEntity<byte[]> downloadReferto(@PathVariable Long refertoId) {
        byte[] fileBytes = refertoService.downloadFile(refertoId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=referto.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(fileBytes);
    }
}
