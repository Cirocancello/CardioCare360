package com.cardiocare360.repository;

import com.cardiocare360.model.entity.ParametroClinico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParametroClinicoRepository extends JpaRepository<ParametroClinico, Long> {
    // Puoi aggiungere metodi custom se servono, es:
    // List<ParametroClinico> findByPazienteId(Long pazienteId);
}
