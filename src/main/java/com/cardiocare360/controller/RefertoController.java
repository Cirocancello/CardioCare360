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

    // ---------------------------------------------------------
    // 1) UPLOAD REFERTI PDF
    // ---------------------------------------------------------
    @PostMapping("/upload")
    public ResponseEntity<?> uploadReferto(
            @RequestParam(required = false) Long esameId,
            @RequestParam(required = false) Long medicoId,
            @RequestParam(required = false) String noteMedico,
            @RequestParam(required = false) MultipartFile file
    ) {
        try {
            if (esameId == null || esameId <= 0) {
                return ResponseEntity.badRequest().body("ESAME_ID_NON_VALIDO");
            }

            if (medicoId == null || medicoId <= 0) {
                return ResponseEntity.badRequest().body("MEDICO_ID_NON_VALIDO");
            }

            if (noteMedico == null || noteMedico.isBlank()) {
                return ResponseEntity.badRequest().body("NOTE_MEDICO_OBBLIGATORIE");
            }

            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body("FILE_NON_PRESENTE");
            }

            RefertoDTO dto = refertoService.uploadReferto(esameId, medicoId, noteMedico, file);
            return ResponseEntity.ok(dto);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());

        } catch (Exception e) {
            System.err.println(">>> [REFERTO] Errore upload: " + e.getMessage());
            return ResponseEntity.status(500).body("ERRORE_SERVER");
        }
    }

    // ---------------------------------------------------------
    // 2) GENERA PDF AUTOMATICO
    // ---------------------------------------------------------
    @PostMapping("/genera/{esameId}")
    public ResponseEntity<?> generaReferto(@PathVariable Long esameId) {
        try {
            if (esameId == null || esameId <= 0) {
                return ResponseEntity.badRequest().body("ESAME_ID_NON_VALIDO");
            }

            RefertoDTO dto = refertoService.generaPdfReferto(esameId);
            return ResponseEntity.ok(dto);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());

        } catch (Exception e) {
            System.err.println(">>> [REFERTO] Errore generazione PDF: " + e.getMessage());
            return ResponseEntity.status(500).body("ERRORE_SERVER");
        }
    }

    // ---------------------------------------------------------
    // 3) RECUPERA REFERTO TRAMITE ESAME
    // ---------------------------------------------------------
    @GetMapping("/esame/{esameId}")
    public ResponseEntity<?> getRefertoByEsame(@PathVariable Long esameId) {
        try {
            if (esameId == null || esameId <= 0) {
                return ResponseEntity.badRequest().body("ESAME_ID_NON_VALIDO");
            }

            RefertoDTO dto = refertoService.getRefertoByEsame(esameId);

            if (dto == null) {
                return ResponseEntity.status(404).body("REFERTO_NON_TROVATO");
            }

            return ResponseEntity.ok(dto);

        } catch (Exception e) {
            System.err.println(">>> [REFERTO] Errore get referto: " + e.getMessage());
            return ResponseEntity.status(500).body("ERRORE_SERVER");
        }
    }

    // ---------------------------------------------------------
    // 4) DOWNLOAD PDF
    // ---------------------------------------------------------
    @GetMapping("/download/{refertoId}")
    public ResponseEntity<?> downloadReferto(@PathVariable Long refertoId) {
        try {
            if (refertoId == null || refertoId <= 0) {
                return ResponseEntity.badRequest().body("REFERTO_ID_NON_VALIDO");
            }

            byte[] fileBytes = refertoService.downloadFile(refertoId);

            if (fileBytes == null) {
                return ResponseEntity.status(404).body("FILE_NON_TROVATO");
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=referto_" + refertoId + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(fileBytes);

        } catch (Exception e) {
            System.err.println(">>> [REFERTO] Errore download: " + e.getMessage());
            return ResponseEntity.status(500).body("ERRORE_SERVER");
        }
    }

    // ---------------------------------------------------------
    // 5) PREVIEW PDF INLINE
    // ---------------------------------------------------------
    @GetMapping("/preview/{esameId}")
    public ResponseEntity<?> previewReferto(@PathVariable Long esameId) {
        try {
            if (esameId == null || esameId <= 0) {
                return ResponseEntity.badRequest().body("ESAME_ID_NON_VALIDO");
            }

            RefertoDTO referto = refertoService.getRefertoByEsame(esameId);

            if (referto == null) {
                return ResponseEntity.status(404).body("REFERTO_NON_TROVATO");
            }

            Long refertoId = referto.getId();

            byte[] fileBytes = refertoService.downloadFile(refertoId);

            if (fileBytes == null) {
                return ResponseEntity.status(404).body("FILE_NON_TROVATO");
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=referto_" + refertoId + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(fileBytes);

        } catch (Exception e) {
            System.err.println(">>> [REFERTO] Errore preview: " + e.getMessage());
            return ResponseEntity.status(500).body("ERRORE_SERVER");
        }
    }

    // ---------------------------------------------------------
    // 6) SALVA REFERTO (UPLOAD + NOTE)
    // ---------------------------------------------------------
    @PostMapping("/esame/{idEsame}")
    public ResponseEntity<?> salvaReferto(
            @PathVariable Long idEsame,
            @RequestParam(required = false) Long medicoId,
            @RequestParam(required = false) String noteMedico,
            @RequestParam(required = false) MultipartFile file
    ) {
        try {
            if (idEsame == null || idEsame <= 0) {
                return ResponseEntity.badRequest().body("ESAME_ID_NON_VALIDO");
            }

            if (medicoId == null || medicoId <= 0) {
                return ResponseEntity.badRequest().body("MEDICO_ID_NON_VALIDO");
            }

            if (noteMedico == null || noteMedico.isBlank()) {
                return ResponseEntity.badRequest().body("NOTE_MEDICO_OBBLIGATORIE");
            }

            RefertoDTO dto = refertoService.uploadReferto(idEsame, medicoId, noteMedico, file);
            return ResponseEntity.ok(dto);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());

        } catch (Exception e) {
            System.err.println(">>> [REFERTO] Errore salva referto: " + e.getMessage());
            return ResponseEntity.status(500).body("ERRORE_SERVER");
        }
    }
}
