package com.cardiocare360.repository;

import com.cardiocare360.model.entity.Messaggio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessaggioRepository extends JpaRepository<Messaggio, Long> {

    // 🔹 Recupera tutti i messaggi di una conversazione
    List<Messaggio> findByConversazioneIdOrderByTimestampAsc(Long conversazioneId);

    // 🔹 Recupera messaggi non letti per una conversazione
    List<Messaggio> findByConversazioneIdAndLettoFalse(Long conversazioneId);
}
