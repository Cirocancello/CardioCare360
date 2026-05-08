package com.cardiocare360.controller;

import com.cardiocare360.model.entity.Messaggio;
import com.cardiocare360.model.request.MessaggioRequestDTO;
import com.cardiocare360.model.response.MessaggioDTO;
import com.cardiocare360.service.MessaggioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/messaggi")
public class MessaggioController {

    private final MessaggioService messaggioService;

    public MessaggioController(MessaggioService messaggioService) {
        this.messaggioService = messaggioService;
    }

    @PostMapping("/invia")
    public MessaggioDTO inviaMessaggio(@RequestBody MessaggioRequestDTO request) {

        Messaggio messaggio = messaggioService.inviaMessaggio(
                request.getMittenteId(),
                request.getDestinatarioId(),
                request.getAppuntamentoId(),
                request.getContenuto()
        );

        return toDTO(messaggio);
    }

    @GetMapping("/conversazione/{utente1Id}/{utente2Id}")
    public List<MessaggioDTO> getConversazione(@PathVariable Long utente1Id,
                                               @PathVariable Long utente2Id) {

        return messaggioService.getConversazione(utente1Id, utente2Id)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/non-letti/{utenteId}")
    public List<MessaggioDTO> getMessaggiNonLetti(@PathVariable Long utenteId) {

        return messaggioService.getMessaggiNonLetti(utenteId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @PutMapping("/segna-letto/{messaggioId}")
    public void segnaComeLetto(@PathVariable Long messaggioId) {
        messaggioService.segnaComeLetto(messaggioId);
    }

    private MessaggioDTO toDTO(Messaggio m) {
        MessaggioDTO dto = new MessaggioDTO();
        dto.setId(m.getId());
        dto.setMittenteId(m.getMittente().getId());
        dto.setDestinatarioId(m.getDestinatario().getId());
        dto.setAppuntamentoId(m.getAppuntamento() != null ? m.getAppuntamento().getId() : null);
        dto.setContenuto(m.getContenuto());
        dto.setDataInvio(m.getDataInvio());
        dto.setLetto(m.isLetto());
        return dto;
    }
}
