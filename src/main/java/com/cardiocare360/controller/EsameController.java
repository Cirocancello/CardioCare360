package com.cardiocare360.controller;

import com.cardiocare360.model.response.DisponibilitaEsameResponse;
import com.cardiocare360.model.response.EsameDTO;
import com.cardiocare360.model.response.RefertoDTO;
import com.cardiocare360.service.EsameService;
import com.cardiocare360.security.jwt.JwtUtil;
import com.cardiocare360.repository.MedicoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    // 📌 Prenotazione esame (solo paziente)
    @PreAuthorize("hasAuthority('PAZIENTE')")
    @PostMapping("/prenota")
    public EsameDTO prenotaEsame(@RequestBody EsameDTO dto) {
        return esameService.creaEsame(dto);
    }

    // 📌 Lista esami del paziente
    @PreAuthorize("hasAuthority('PAZIENTE')")
    @GetMapping("/paziente/{idPaziente}")
    public List<EsameDTO> getEsamiPaziente(@PathVariable Long idPaziente) {
        return esameService.getEsamiPaziente(idPaziente);
    }

    // 📌 Lista esami del medico
    @PreAuthorize("hasAuthority('MEDICO')")
    @GetMapping("/medico/{idMedico}")
    public List<EsameDTO> getEsamiMedico(@PathVariable Long idMedico) {
        return esameService.getEsamiMedico(idMedico);
    }

    // 📌 Dettaglio esame (paziente o medico)
    @PreAuthorize("hasAnyAuthority('PAZIENTE','MEDICO')")
    @GetMapping("/{idEsame}")
    public EsameDTO getEsameById(@PathVariable Long idEsame) {
        return esameService.getEsameById(idEsame);
    }

    // 📌 Aggiornamento stato esame (solo medico)
    @PreAuthorize("hasAuthority('MEDICO')")
    @PutMapping("/{idEsame}/stato")
    public EsameDTO aggiornaStatoEsame(
            @PathVariable Long idEsame,
            @RequestParam String nuovoStato
    ) {
        return esameService.aggiornaStatoEsame(idEsame, nuovoStato);
    }

    // 📌 Eliminazione esame (solo admin)
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{idEsame}")
    public void eliminaEsame(@PathVariable Long idEsame) {
        esameService.eliminaEsame(idEsame);
    }

    // 📌 Recupero referto (paziente o medico)
    @PreAuthorize("hasAnyAuthority('PAZIENTE','MEDICO')")
    @GetMapping("/{idEsame}/referto")
    public RefertoDTO getReferto(@PathVariable Long idEsame) {
        return esameService.getRefertoByEsame(idEsame);
    }

    // 📌 Prossima disponibilità (paziente)
    @PreAuthorize("hasAuthority('PAZIENTE')")
    @GetMapping("/disponibilita/prossima")
    public ResponseEntity<DisponibilitaEsameResponse> getProssimaDisponibilita(
            @RequestParam String tipo
    ) {
        DisponibilitaEsameResponse disponibilita = esameService.calcolaProssimaDisponibilita(tipo);
        return ResponseEntity.ok(disponibilita);
    }

    // 📌 Lista esami COMPLETATI da refertare (solo medico)
    @PreAuthorize("hasAuthority('MEDICO')")
    @GetMapping("/medico/da-refertare")
    public ResponseEntity<List<EsameDTO>> getEsamiDaRefertare(
            @RequestHeader("Authorization") String token) {

        String rawToken = token.substring(7);
        String email = jwtUtil.extractEmail(rawToken);

        Long idMedico = medicoRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Medico non trovato"))
                .getId();

        List<EsameDTO> esami = esameService.getEsamiDaRefertare(idMedico);

        return ResponseEntity.ok(esami);
    }
}
