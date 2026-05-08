package com.cardiocare360.service;

import com.cardiocare360.model.entity.Messaggio;
import java.util.List;

public interface MessaggioService {

    Messaggio inviaMessaggio(Long mittenteId, Long destinatarioId, Long appuntamentoId, String contenuto);

    List<Messaggio> getConversazione(Long utente1Id, Long utente2Id);

    List<Messaggio> getMessaggiNonLetti(Long utenteId);

    void segnaComeLetto(Long messaggioId);
}
