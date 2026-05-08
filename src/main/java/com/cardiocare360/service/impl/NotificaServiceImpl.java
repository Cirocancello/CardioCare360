package com.cardiocare360.service.impl;

import com.cardiocare360.model.entity.Notifica;
import com.cardiocare360.model.entity.Utente;
import com.cardiocare360.repository.NotificaRepository;
import com.cardiocare360.repository.UtenteRepository;
import com.cardiocare360.service.NotificaService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificaServiceImpl implements NotificaService {

    private final NotificaRepository notificaRepository;
    private final UtenteRepository utenteRepository;

    public NotificaServiceImpl(NotificaRepository notificaRepository, UtenteRepository utenteRepository) {
        this.notificaRepository = notificaRepository;
        this.utenteRepository = utenteRepository;
    }

    @Override
    public Notifica creaNotifica(Notifica notifica) {
        return notificaRepository.save(notifica);
    }

    @Override
    public List<Notifica> getNotificheUtente(Long utenteId) {
        Utente utente = utenteRepository.findById(utenteId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        return notificaRepository.findByUtenteOrderByDataCreazioneDesc(utente);
    }

    @Override
    public List<Notifica> getNotificheNonLette(Long utenteId) {
        Utente utente = utenteRepository.findById(utenteId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        return notificaRepository.findByUtenteAndLettoFalseOrderByDataCreazioneDesc(utente);
    }

    @Override
    public Notifica getNotificaById(Long id) {
        return notificaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notifica non trovata"));
    }

    @Override
    public Notifica segnaComeLetta(Long id) {
        Notifica notifica = getNotificaById(id);
        notifica.setLetto(true);
        return notificaRepository.save(notifica);
    }
}
