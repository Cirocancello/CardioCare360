package com.cardiocare360.repository;

import com.cardiocare360.model.entity.Esame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface EsameRepository extends JpaRepository<Esame, Long> {

    // Lista esami del paziente
    List<Esame> findByPazienteId(Long idPaziente);

    // Lista esami del medico (per refertazione)
    List<Esame> findByMedicoId(Long idMedico);

    // Controllo esame specifico del paziente
    boolean existsByIdAndPazienteId(Long idEsame, Long idPaziente);

    // Controllo esame specifico del medico
    boolean existsByIdAndMedicoId(Long idEsame, Long idMedico);

    // 🔥 Controllo se uno slot (data + ora) è già occupato
    boolean existsByDataEsameAndOraEsame(LocalDate dataEsame, LocalTime oraEsame);
    
    List<Esame> findByMedicoIdAndStato(Long idMedico, Esame.StatoEsame stato);

}
