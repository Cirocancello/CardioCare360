package com.cardiocare360.repository;

import com.cardiocare360.model.entity.ParametroClinico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParametroClinicoRepository extends JpaRepository<ParametroClinico, Long> {

    // Parametri del paziente (ordinati per data decrescente)
    List<ParametroClinico> findByPazienteIdOrderByDataRilevazioneDesc(Long pazienteId);

    // Metodo richiesto dal PazienteServiceImpl
    List<ParametroClinico> findByPazienteId(Long pazienteId);
}
