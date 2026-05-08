package com.cardiocare360.repository;

import com.cardiocare360.model.entity.Notifica;
import com.cardiocare360.model.entity.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificaRepository extends JpaRepository<Notifica, Long> {

    // Tutte le notifiche di un utente, ordinate dalla più recente
    List<Notifica> findByUtenteOrderByDataCreazioneDesc(Utente utente);

    // Solo notifiche non lette
    List<Notifica> findByUtenteAndLettoFalseOrderByDataCreazioneDesc(Utente utente);
}
