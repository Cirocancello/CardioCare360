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

    // 📌 1. Upload referto PDF
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

    // 📌 2. Generazione automatica PDF (non implementato)
    @PostMapping("/genera/{esameId}")
    public ResponseEntity<RefertoDTO> generaReferto(@PathVariable Long esameId) {
        RefertoDTO dto = refertoService.generaPdfReferto(esameId);
        return ResponseEntity.ok(dto);
    }

    // 📌 3. Recupera referto tramite esame
    @GetMapping("/esame/{esameId}")
    public ResponseEntity<RefertoDTO> getRefertoByEsame(@PathVariable Long esameId) {
        RefertoDTO dto = refertoService.getRefertoByEsame(esameId);
        return ResponseEntity.ok(dto);
    }

   

    // 📌 5. DOWNLOAD PDF (scarica il file)
    @GetMapping("/download/{refertoId}")
    public ResponseEntity<byte[]> downloadReferto(@PathVariable Long refertoId) {
        byte[] fileBytes = refertoService.downloadFile(refertoId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=referto_" + refertoId + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(fileBytes);
    }
    
    @GetMapping("/preview/{esameId}")
    public ResponseEntity<byte[]> previewReferto(@PathVariable Long esameId) {

        // Recupero referto tramite esame
        RefertoDTO referto = refertoService.getRefertoByEsame(esameId);
        Long refertoId = referto.getId();

        byte[] fileBytes = refertoService.downloadFile(refertoId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=referto_" + refertoId + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(fileBytes);
    }

    @PostMapping("/esame/{idEsame}")
    public ResponseEntity<RefertoDTO> salvaReferto(
            @PathVariable Long idEsame,
            @RequestParam Long medicoId,
            @RequestParam String noteMedico,
            @RequestParam(required = false) MultipartFile file
    ) {
        RefertoDTO dto = refertoService.uploadReferto(idEsame, medicoId, noteMedico, file);
        return ResponseEntity.ok(dto);
    }

}
