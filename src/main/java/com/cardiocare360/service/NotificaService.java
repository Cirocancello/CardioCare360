package com.cardiocare360.service;

import com.cardiocare360.model.entity.Notifica;

import java.util.List;

public interface NotificaService {

    Notifica creaNotifica(Notifica notifica);

    List<Notifica> getNotificheUtente(Long utenteId);

    List<Notifica> getNotificheNonLette(Long utenteId);

    Notifica getNotificaById(Long id);

    Notifica segnaComeLetta(Long id);
}
