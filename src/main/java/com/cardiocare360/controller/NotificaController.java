package com.cardiocare360.controller;

import com.cardiocare360.model.request.NotificaRequestDTO;
import com.cardiocare360.model.response.NotificaDTO;
import com.cardiocare360.model.entity.Appuntamento;
import com.cardiocare360.model.entity.Notifica;
import com.cardiocare360.model.entity.ParametroClinico;
import com.cardiocare360.model.entity.Utente;
import com.cardiocare360.model.mapper.NotificaMapper;
import com.cardiocare360.repository.AppuntamentoRepository;
import com.cardiocare360.repository.ParametroClinicoRepository;
import com.cardiocare360.repository.UtenteRepository;
import com.cardiocare360.service.NotificaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notifiche")
@CrossOrigin(origins = "*")
public class NotificaController {

    private final NotificaService notificaService;
    private final UtenteRepository utenteRepository;
    private final AppuntamentoRepository appuntamentoRepository;
    private final ParametroClinicoRepository parametroClinicoRepository;

    public NotificaController(
            NotificaService notificaService,
            UtenteRepository utenteRepository,
            AppuntamentoRepository appuntamentoRepository,
            ParametroClinicoRepository parametroClinicoRepository
    ) {
        this.notificaService = notificaService;
        this.utenteRepository = utenteRepository;
        this.appuntamentoRepository = appuntamentoRepository;
        this.parametroClinicoRepository = parametroClinicoRepository;
    }

    // -------------------------
    // CREAZIONE NOTIFICA
    // -------------------------
    @PostMapping
    public ResponseEntity<NotificaDTO> creaNotifica(@RequestBody NotificaRequestDTO req) {

    	System.out.println(">>> Metodo giusto chiamato");

    	
        Utente utente = utenteRepository.findById(req.getUtenteId())
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        Appuntamento appuntamento = null;
        if (req.getAppuntamentoId() != null) {
            appuntamento = appuntamentoRepository.findById(req.getAppuntamentoId())
                    .orElseThrow(() -> new RuntimeException("Appuntamento non trovato"));
        }

        ParametroClinico parametro = null;
        if (req.getParametroClinicoId() != null) {
            parametro = parametroClinicoRepository.findById(req.getParametroClinicoId())
                    .orElseThrow(() -> new RuntimeException("Parametro clinico non trovato"));
        }

        Notifica notifica = new Notifica();
        notifica.setTitolo(req.getTitolo());
        notifica.setMessaggio(req.getMessaggio());
        notifica.setLetto(req.isLetto());
        notifica.setUtente(utente);
        notifica.setAppuntamento(appuntamento);
        notifica.setParametroClinico(parametro);

        Notifica salvata = notificaService.creaNotifica(notifica);

        return ResponseEntity.ok(NotificaMapper.toDTO(salvata));
    }

    // -------------------------
    // NOTIFICHE DI UN UTENTE
    // -------------------------
    @GetMapping("/utente/{utenteId}")
    public ResponseEntity<List<NotificaDTO>> getNotificheUtente(@PathVariable Long utenteId) {
        List<NotificaDTO> lista = notificaService.getNotificheUtente(utenteId)
                .stream()
                .map(NotificaMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    // -------------------------
    // NOTIFICHE NON LETTE
    // -------------------------
    @GetMapping("/utente/{utenteId}/non-lette")
    public ResponseEntity<List<NotificaDTO>> getNotificheNonLette(@PathVariable Long utenteId) {
        List<NotificaDTO> lista = notificaService.getNotificheNonLette(utenteId)
                .stream()
                .map(NotificaMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    // -------------------------
    // NOTIFICA SINGOLA
    // -------------------------
    @GetMapping("/{id}")
    public ResponseEntity<NotificaDTO> getNotificaById(@PathVariable Long id) {
        return ResponseEntity.ok(
                NotificaMapper.toDTO(notificaService.getNotificaById(id))
        );
    }

    // -------------------------
    // SEGNARE COME LETTA
    // -------------------------
    @PutMapping("/{id}/lettura")
    public ResponseEntity<NotificaDTO> segnaComeLetta(@PathVariable Long id) {
        Notifica aggiornata = notificaService.segnaComeLetta(id);
        return ResponseEntity.ok(NotificaMapper.toDTO(aggiornata));
    }
}
