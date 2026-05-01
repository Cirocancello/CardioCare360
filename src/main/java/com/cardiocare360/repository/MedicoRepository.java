package com.cardiocare360.repository;

import com.cardiocare360.model.entity.Medico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedicoRepository extends JpaRepository<Medico, Long> {

    Optional<Medico> findByEmail(String email);
}
