package com.cardiocare360.repository;

import com.cardiocare360.model.entity.Paziente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PazienteRepository extends JpaRepository<Paziente, Long> {

    Optional<Paziente> findByCodiceFiscale(String codiceFiscale);

    Optional<Paziente> findByEmail(String email);
}
