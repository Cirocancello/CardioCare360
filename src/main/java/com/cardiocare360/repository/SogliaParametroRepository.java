package com.cardiocare360.repository;

import com.cardiocare360.model.entity.SogliaParametro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SogliaParametroRepository extends JpaRepository<SogliaParametro, Long> {

    SogliaParametro findByPazienteIdAndParametroId(Long pazienteId, Integer parametroId);
}
