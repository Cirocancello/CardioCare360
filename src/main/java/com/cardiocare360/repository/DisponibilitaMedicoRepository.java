package com.cardiocare360.repository;

import com.cardiocare360.model.entity.DisponibilitaMedico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisponibilitaMedicoRepository extends JpaRepository<DisponibilitaMedico, Long> {

    // Tutte le disponibilità di un medico
    List<DisponibilitaMedico> findByMedicoId(Long idMedico);

    // Disponibilità di un medico per giorno della settimana
    List<DisponibilitaMedico> findByMedicoIdAndGiornoSettimana(Long idMedico, String giornoSettimana);

    // Disponibilità per più medici (usato per generare slot)
    List<DisponibilitaMedico> findByMedicoIdIn(List<Long> idMedici);
}
