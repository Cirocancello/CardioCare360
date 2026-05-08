package com.cardiocare360.service.impl;

import com.cardiocare360.model.entity.Messaggio;
import com.cardiocare360.model.entity.Utente;
import com.cardiocare360.model.entity.Appuntamento;
import com.cardiocare360.repository.MessaggioRepository;
import com.cardiocare360.repository.UtenteRepository;
import com.cardiocare360.repository.AppuntamentoRepository;
import com.cardiocare360.service.MessaggioService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MessaggioServiceImpl implements MessaggioService {

    private final MessaggioRepository messaggioRepository;
    private final UtenteRepository utenteRepository;
    private final AppuntamentoRepository appuntamentoRepository;

    public MessaggioServiceImpl(MessaggioRepository messaggioRepository,
                                UtenteRepository utenteRepository,
                                AppuntamentoRepository appuntamentoRepository) {
        this.messaggioRepository = messaggioRepository;
        this.utenteRepository = utenteRepository;
        this.appuntamentoRepository = appuntamentoRepository;
    }

    @Override
    public Messaggio inviaMessaggio(Long mittenteId, Long destinatarioId, Long appuntamentoId, String contenuto) {
        Utente mittente = utenteRepository.findById(mittenteId).orElseThrow();
        Utente destinatario = utenteRepository.findById(destinatarioId).orElseThrow();
        Appuntamento appuntamento = appuntamentoId != null
                ? appuntamentoRepository.findById(appuntamentoId).orElse(null)
                : null;

        Messaggio messaggio = new Messaggio();
        messaggio.setMittente(mittente);
        messaggio.setDestinatario(destinatario);
        messaggio.setAppuntamento(appuntamento);
        messaggio.setContenuto(contenuto);

        return messaggioRepository.save(messaggio);
    }

    @Override
    public List<Messaggio> getConversazione(Long utente1Id, Long utente2Id) {
        return messaggioRepository.findConversazione(utente1Id, utente2Id);
    }

    @Override
    public List<Messaggio> getMessaggiNonLetti(Long utenteId) {
        return messaggioRepository.findByDestinatarioIdAndLettoFalse(utenteId);
    }

    @Override
    public void segnaComeLetto(Long messaggioId) {
        Messaggio messaggio = messaggioRepository.findById(messaggioId).orElseThrow();
        messaggio.setLetto(true);
        messaggioRepository.save(messaggio);
    }
}
