package com.cardiocare360.repository;

import com.cardiocare360.model.entity.Paziente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PazienteRepository extends JpaRepository<Paziente, Long> {
}
