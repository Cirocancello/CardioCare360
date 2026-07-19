package com.cardiocare360.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cardiocare360.model.entity.Parametro;

public interface ParametroRepository extends JpaRepository<Parametro, Long> {

    Parametro findByTipo(String tipo);
}
