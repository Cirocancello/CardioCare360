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

    // ---------------------------------------------------------
    // CREAZIONE NOTIFICA
    // ---------------------------------------------------------
    @PostMapping
    public ResponseEntity<?> creaNotifica(@RequestBody NotificaRequestDTO req) {

        try {
            if (req == null) {
                return ResponseEntity.badRequest().body("REQUEST_NULL");
            }

            if (req.getUtenteId() == null || req.getUtenteId() <= 0) {
                return ResponseEntity.badRequest().body("UTENTE_ID_NON_VALIDO");
            }

            if (req.getTitolo() == null || req.getTitolo().isBlank()) {
                return ResponseEntity.badRequest().body("TITOLO_OBBLIGATORIO");
            }

            if (req.getMessaggio() == null || req.getMessaggio().isBlank()) {
                return ResponseEntity.badRequest().body("MESSAGGIO_OBBLIGATORIO");
            }

            Utente utente = utenteRepository.findById(req.getUtenteId())
                    .orElseThrow(() -> new IllegalArgumentException("UTENTE_NON_TROVATO"));

            Appuntamento appuntamento = null;
            if (req.getAppuntamentoId() != null) {
                appuntamento = appuntamentoRepository.findById(req.getAppuntamentoId())
                        .orElseThrow(() -> new IllegalArgumentException("APPUNTAMENTO_NON_TROVATO"));
            }

            ParametroClinico parametro = null;
            if (req.getParametroClinicoId() != null) {
                parametro = parametroClinicoRepository.findById(req.getParametroClinicoId())
                        .orElseThrow(() -> new IllegalArgumentException("PARAMETRO_CLINICO_NON_TROVATO"));
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

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());

        } catch (Exception e) {
            System.err.println(">>> [NOTIFICA] Errore inatteso: " + e.getMessage());
            return ResponseEntity.status(500).body("ERRORE_SERVER");
        }
    }

    // ---------------------------------------------------------
    // NOTIFICHE DI UN UTENTE
    // ---------------------------------------------------------
    @GetMapping("/utente/{utenteId}")
    public ResponseEntity<?> getNotificheUtente(@PathVariable Long utenteId) {

        try {
            if (utenteId == null || utenteId <= 0) {
                return ResponseEntity.badRequest().body("UTENTE_ID_NON_VALIDO");
            }

            List<NotificaDTO> lista = notificaService.getNotificheUtente(utenteId)
                    .stream()
                    .map(NotificaMapper::toDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(lista);

        } catch (Exception e) {
            System.err.println(">>> [NOTIFICA] Errore get notifiche utente: " + e.getMessage());
            return ResponseEntity.status(500).body("ERRORE_SERVER");
        }
    }

    // ---------------------------------------------------------
    // NOTIFICHE NON LETTE
    // ---------------------------------------------------------
    @GetMapping("/utente/{utenteId}/non-lette")
    public ResponseEntity<?> getNotificheNonLette(@PathVariable Long utenteId) {

        try {
            if (utenteId == null || utenteId <= 0) {
                return ResponseEntity.badRequest().body("UTENTE_ID_NON_VALIDO");
            }

            List<NotificaDTO> lista = notificaService.getNotificheNonLette(utenteId)
                    .stream()
                    .map(NotificaMapper::toDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(lista);

        } catch (Exception e) {
            System.err.println(">>> [NOTIFICA] Errore get notifiche non lette: " + e.getMessage());
            return ResponseEntity.status(500).body("ERRORE_SERVER");
        }
    }

    // ---------------------------------------------------------
    // NOTIFICA SINGOLA
    // ---------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<?> getNotificaById(@PathVariable Long id) {

        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest().body("NOTIFICA_ID_NON_VALIDO");
            }

            Notifica notifica = notificaService.getNotificaById(id);

            if (notifica == null) {
                return ResponseEntity.status(404).body("NOTIFICA_NON_TROVATA");
            }

            return ResponseEntity.ok(NotificaMapper.toDTO(notifica));

        } catch (Exception e) {
            System.err.println(">>> [NOTIFICA] Errore get notifica: " + e.getMessage());
            return ResponseEntity.status(500).body("ERRORE_SERVER");
        }
    }

    // ---------------------------------------------------------
    // SEGNARE COME LETTA
    // ---------------------------------------------------------
    @PutMapping("/{id}/lettura")
    public ResponseEntity<?> segnaComeLetta(@PathVariable Long id) {

        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest().body("NOTIFICA_ID_NON_VALIDO");
            }

            Notifica aggiornata = notificaService.segnaComeLetta(id);

            return ResponseEntity.ok(NotificaMapper.toDTO(aggiornata));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());

        } catch (Exception e) {
            System.err.println(">>> [NOTIFICA] Errore segna letta: " + e.getMessage());
            return ResponseEntity.status(500).body("ERRORE_SERVER");
        }
    }
}
