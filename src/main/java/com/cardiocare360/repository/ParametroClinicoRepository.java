package com.cardiocare360.repository;

import com.cardiocare360.model.entity.ParametroClinico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParametroClinicoRepository extends JpaRepository<ParametroClinico, Long> {

    // Storico parametri del paziente (ordinati per data decrescente)
    List<ParametroClinico> findByPazienteIdOrderByDataRilevazioneDesc(Long pazienteId);

    // Tutti i parametri del paziente
    List<ParametroClinico> findByPazienteId(Long pazienteId);

    // Ultimo parametro registrato
    ParametroClinico findTopByPazienteIdOrderByDataRilevazioneDesc(Long pazienteId);
}
