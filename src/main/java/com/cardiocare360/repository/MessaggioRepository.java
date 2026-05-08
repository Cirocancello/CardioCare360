package com.cardiocare360.repository;

import com.cardiocare360.model.entity.Messaggio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface MessaggioRepository extends JpaRepository<Messaggio, Long> {

    @Query("SELECT m FROM Messaggio m WHERE " +
           "(m.mittente.id = :utente1Id AND m.destinatario.id = :utente2Id) OR " +
           "(m.mittente.id = :utente2Id AND m.destinatario.id = :utente1Id) " +
           "ORDER BY m.dataInvio ASC")
    List<Messaggio> findConversazione(Long utente1Id, Long utente2Id);

    List<Messaggio> findByDestinatarioIdAndLettoFalse(Long destinatarioId);
}
