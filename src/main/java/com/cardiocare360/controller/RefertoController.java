package com.cardiocare360.controller;

import com.cardiocare360.model.response.RefertoDTO;
import com.cardiocare360.service.RefertoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/referti")
@CrossOrigin(origins = "*")
public class RefertoController {

    @Autowired
    private RefertoService refertoService;

    // 📌 1. Upload referto PDF (solo medico)
    @PreAuthorize("hasAuthority('MEDICO')")
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

    // 📌 2. Generazione automatica PDF (solo medico)
    @PreAuthorize("hasAuthority('MEDICO')")
    @PostMapping("/genera/{esameId}")
    public ResponseEntity<RefertoDTO> generaReferto(@PathVariable Long esameId) {
        RefertoDTO dto = refertoService.generaPdfReferto(esameId);
        return ResponseEntity.ok(dto);
    }

    // 📌 3. Recupera referto tramite esame (paziente o medico)
    @PreAuthorize("hasAnyAuthority('PAZIENTE','MEDICO')")
    @GetMapping("/esame/{esameId}")
    public ResponseEntity<?> getRefertoByEsame(@PathVariable Long esameId) {

        RefertoDTO dto = refertoService.getRefertoByEsame(esameId);

        if (dto == null) {
            return ResponseEntity.status(404).body("Nessun referto disponibile per questo esame");
        }

        return ResponseEntity.ok(dto);
    }

    // 📌 4. DOWNLOAD PDF (paziente o medico)
    @PreAuthorize("hasAnyAuthority('PAZIENTE','MEDICO')")
    @GetMapping("/download/{refertoId}")
    public ResponseEntity<byte[]> downloadReferto(@PathVariable Long refertoId) {
        byte[] fileBytes = refertoService.downloadFile(refertoId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=referto_" + refertoId + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(fileBytes);
    }

    // 📌 5. Anteprima PDF (paziente o medico)
    @PreAuthorize("hasAnyAuthority('PAZIENTE','MEDICO')")
    @GetMapping("/preview/{esameId}")
    public ResponseEntity<?> previewReferto(@PathVariable Long esameId) {

        RefertoDTO referto = refertoService.getRefertoByEsame(esameId);

        if (referto == null) {
            return ResponseEntity.status(404).body("Nessun referto disponibile per questo esame");
        }

        byte[] fileBytes = refertoService.downloadFile(referto.getId());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=referto_" + referto.getId() + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(fileBytes);
    }

    // 📌 6. Salva referto (solo medico)
    @PreAuthorize("hasAuthority('MEDICO')")
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
