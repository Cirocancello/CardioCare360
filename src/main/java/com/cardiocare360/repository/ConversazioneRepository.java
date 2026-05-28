package com.cardiocare360.repository;

import com.cardiocare360.model.entity.Conversazione;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ConversazioneRepository extends JpaRepository<Conversazione, Long> {

    // 🔹 Recupera tutte le conversazioni di un paziente
    List<Conversazione> findByPazienteIdOrderByUltimoAggiornamentoDesc(Long pazienteId);

    // 🔹 Recupera tutte le conversazioni di un medico
    List<Conversazione> findByMedicoIdOrderByUltimoAggiornamentoDesc(Long medicoId);

    // 🔹 Recupera la conversazione tra paziente e medico (se esiste)
    Optional<Conversazione> findByPazienteIdAndMedicoId(Long pazienteId, Long medicoId);
}
