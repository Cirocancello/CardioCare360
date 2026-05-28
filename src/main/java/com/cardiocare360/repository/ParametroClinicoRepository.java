package com.cardiocare360.repository;

import com.cardiocare360.model.entity.ParametroClinico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParametroClinicoRepository extends JpaRepository<ParametroClinico, Long> {

    List<ParametroClinico> findByPazienteIdOrderByDataRilevazioneDesc(Long pazienteId);
    
    
}
