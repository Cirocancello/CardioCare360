package com.cardiocare360.repository;

import com.cardiocare360.model.entity.Terapia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TerapiaRepository extends JpaRepository<Terapia, Long> {

    List<Terapia> findByPazienteId(Long pazienteId);

    List<Terapia> findByMedicoId(Long medicoId);
}
