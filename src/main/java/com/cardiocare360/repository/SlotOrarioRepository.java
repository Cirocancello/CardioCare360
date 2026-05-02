package com.cardiocare360.repository;

import com.cardiocare360.model.entity.SlotOrario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SlotOrarioRepository extends JpaRepository<SlotOrario, Long> {

    // Tutti gli slot di una disponibilità
    List<SlotOrario> findByDisponibilitaId(Long disponibilitaId);

    // Slot che iniziano in un certo intervallo (utile per controlli)
    List<SlotOrario> findByInizioBetween(LocalDateTime start, LocalDateTime end);

    // Tutti gli slot di un medico (tramite disponibilità)
    List<SlotOrario> findByDisponibilitaMedicoId(Long medicoId);

    // Controllo se uno slot è già prenotato
    boolean existsByInizioAndPrenotato(LocalDateTime inizio, boolean prenotato);
}
