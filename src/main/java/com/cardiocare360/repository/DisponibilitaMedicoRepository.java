package com.cardiocare360.repository;

import com.cardiocare360.model.entity.DisponibilitaMedico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DisponibilitaMedicoRepository extends JpaRepository<DisponibilitaMedico, Long> {

    // Tutte le disponibilità di un medico
    List<DisponibilitaMedico> findByMedicoId(Long medicoId);

    // Verifica se esiste almeno una disponibilità per quel giorno
    boolean existsByMedicoIdAndGiornoSettimana(Long medicoId, String giornoSettimana);

    // 🔥 CORRETTO: può restituire più disponibilità nello stesso giorno
    List<DisponibilitaMedico> findByMedicoIdAndGiornoSettimana(Long medicoId, String giornoSettimana);

    // Tutte le disponibilità di una lista di medici
    List<DisponibilitaMedico> findByMedicoIdIn(List<Long> ids);
}
