package com.cardiocare360.repository;

import com.cardiocare360.model.entity.DisponibilitaMedico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DisponibilitaMedicoRepository extends JpaRepository<DisponibilitaMedico, Long> {

    // Tutte le disponibilità di un medico
    List<DisponibilitaMedico> findByMedicoId(Long medicoId);

    // Verifica se il medico ha già una disponibilità per quel giorno (abbreviazione: LUN, MAR...)
    boolean existsByMedicoIdAndGiornoSettimana(Long medicoId, String giornoSettimana);

    // Recupera la disponibilità specifica (medico + giorno abbreviato)
    Optional<DisponibilitaMedico> findByMedicoIdAndGiornoSettimana(Long medicoId, String giornoSettimana);
}
