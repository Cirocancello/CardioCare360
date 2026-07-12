package com.cardiocare360.controller;

import com.cardiocare360.model.response.DisponibilitaEsameResponse;
import com.cardiocare360.model.response.EsameDTO;
import com.cardiocare360.model.response.RefertoDTO;
import com.cardiocare360.service.EsameService;
import com.cardiocare360.security.jwt.JwtUtil;
import com.cardiocare360.repository.MedicoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/esami")
@CrossOrigin(origins = "*")
public class EsameController {

    @Autowired
    private EsameService esameService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MedicoRepository medicoRepository;

    // ---------------------------------------------------------
    // PRENOTA ESAME (blindato)
    // ---------------------------------------------------------
    @PostMapping("/prenota")
    public EsameDTO prenotaEsame(@RequestBody EsameDTO dto) {

        if (dto == null) {
            throw new RuntimeException("Dati esame mancanti");
        }

        if (dto.getIdPaziente() == null) {
            throw new RuntimeException("ID paziente obbligatorio");
        }

        if (dto.getIdMedico() == null) {
            throw new RuntimeException("ID medico obbligatorio");
        }

        if (dto.getTipoEsame() == null || dto.getTipoEsame().isBlank()) {
            throw new RuntimeException("Il tipo di esame è obbligatorio");
        }

        return esameService.creaEsame(dto);
    }

    // ---------------------------------------------------------
    // LISTA ESAMI PAZIENTE
    // ---------------------------------------------------------
    @GetMapping("/paziente/{idPaziente}")
    public List<EsameDTO> getEsamiPaziente(@PathVariable Long idPaziente) {

        if (idPaziente == null || idPaziente <= 0) {
            throw new RuntimeException("ID paziente non valido");
        }

        return esameService.getEsamiPaziente(idPaziente);
    }

    // ---------------------------------------------------------
    // LISTA ESAMI MEDICO
    // ---------------------------------------------------------
    @GetMapping("/medico/{idMedico}")
    public List<EsameDTO> getEsamiMedico(@PathVariable Long idMedico) {

        if (idMedico == null || idMedico <= 0) {
            throw new RuntimeException("ID medico non valido");
        }

        return esameService.getEsamiMedico(idMedico);
    }

    // ---------------------------------------------------------
    // DETTAGLIO ESAME
    // ---------------------------------------------------------
    @GetMapping("/{idEsame}")
    public EsameDTO getEsameById(@PathVariable Long idEsame) {

        if (idEsame == null || idEsame <= 0) {
            throw new RuntimeException("ID esame non valido");
        }

        return esameService.getEsameById(idEsame);
    }

    // ---------------------------------------------------------
    // AGGIORNA STATO ESAME (blindato)
    // ---------------------------------------------------------
    @PutMapping("/{idEsame}/stato")
    public EsameDTO aggiornaStatoEsame(
            @PathVariable Long idEsame,
            @RequestParam String nuovoStato
    ) {

        if (idEsame == null || idEsame <= 0) {
            throw new RuntimeException("ID esame non valido");
        }

        if (nuovoStato == null || nuovoStato.isBlank()) {
            throw new RuntimeException("Il nuovo stato è obbligatorio");
        }

        return esameService.aggiornaStatoEsame(idEsame, nuovoStato);
    }

    // ---------------------------------------------------------
    // ELIMINA ESAME (blindato)
    // ---------------------------------------------------------
    @DeleteMapping("/{idEsame}")
    public void eliminaEsame(@PathVariable Long idEsame) {

        if (idEsame == null || idEsame <= 0) {
            throw new RuntimeException("ID esame non valido");
        }

        esameService.eliminaEsame(idEsame);
    }

    // ---------------------------------------------------------
    // RECUPERA REFERTO
    // ---------------------------------------------------------
    @GetMapping("/{idEsame}/referto")
    public RefertoDTO getReferto(@PathVariable Long idEsame) {

        if (idEsame == null || idEsame <= 0) {
            throw new RuntimeException("ID esame non valido");
        }

        return esameService.getRefertoByEsame(idEsame);
    }

    // ---------------------------------------------------------
    // PROSSIMA DISPONIBILITÀ
    // ---------------------------------------------------------
    @GetMapping("/disponibilita/prossima")
    public ResponseEntity<DisponibilitaEsameResponse> getProssimaDisponibilita(
            @RequestParam String tipo
    ) {

        if (tipo == null || tipo.isBlank()) {
            throw new RuntimeException("Il tipo di esame è obbligatorio");
        }

        DisponibilitaEsameResponse disponibilita =
                esameService.calcolaProssimaDisponibilita(tipo);

        return ResponseEntity.ok(disponibilita);
    }

    // ---------------------------------------------------------
    // ESAMI DA REFERTARE (blindato)
    // ---------------------------------------------------------
    @GetMapping("/medico/da-refertare")
    public ResponseEntity<List<EsameDTO>> getEsamiDaRefertare(
            @RequestHeader("Authorization") String token) {

        if (token == null || !token.startsWith("Bearer ")) {
            throw new RuntimeException("Token non valido");
        }

        String rawToken = token.substring(7);

        String email = jwtUtil.extractEmail(rawToken);
        if (email == null || email.isBlank()) {
            throw new RuntimeException("Token non valido");
        }

        Long idMedico = medicoRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Medico non trovato"))
                .getId();

        List<EsameDTO> esami = esameService.getEsamiDaRefertare(idMedico);

        return ResponseEntity.ok(esami);
    }
}
