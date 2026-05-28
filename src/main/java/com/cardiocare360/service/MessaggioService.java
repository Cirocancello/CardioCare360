package com.cardiocare360.service;

import com.cardiocare360.model.entity.Conversazione;
import com.cardiocare360.model.entity.Messaggio;
import com.cardiocare360.repository.MessaggioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessaggioService {

    private final MessaggioRepository messaggioRepository;
    private final ConversazioneService conversazioneService;

    public MessaggioService(MessaggioRepository messaggioRepository,
                            ConversazioneService conversazioneService) {
        this.messaggioRepository = messaggioRepository;
        this.conversazioneService = conversazioneService;
    }

    // 🔹 Invia un messaggio in una conversazione
    public Messaggio inviaMessaggio(Long conversazioneId, Messaggio.Mittente mittente, String testo) {

        Conversazione conversazione = conversazioneService.getConversazione(conversazioneId);

        Messaggio msg = new Messaggio();
        msg.setConversazione(conversazione);
        msg.setMittente(mittente);
        msg.setTesto(testo);
        msg.setTimestamp(LocalDateTime.now());
        msg.setLetto(false);

        Messaggio salvato = messaggioRepository.save(msg);

        // 🔹 Aggiorna ultimo messaggio nella conversazione
        conversazioneService.aggiornaUltimoMessaggio(conversazioneId, testo);

        return salvato;
    }

    // 🔹 Recupera tutti i messaggi di una conversazione
    public List<Messaggio> getMessaggiConversazione(Long conversazioneId) {
        return messaggioRepository.findByConversazioneIdOrderByTimestampAsc(conversazioneId);
    }

    // 🔹 Segna tutti i messaggi come letti
    public void segnaComeLetti(Long conversazioneId) {
        List<Messaggio> nonLetti = messaggioRepository.findByConversazioneIdAndLettoFalse(conversazioneId);

        for (Messaggio m : nonLetti) {
            m.setLetto(true);
        }

        messaggioRepository.saveAll(nonLetti);
    }
}
