package com.cardiocare360.repository;

import com.cardiocare360.model.entity.Terapia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Repository
public interface TerapiaRepository extends JpaRepository<Terapia, Long> {

    List<Terapia> findByPazienteId(Long pazienteId);

    List<Terapia> findByMedicoId(Long medicoId);

    // 🔥 Serve per impedire duplicati (vincolo UNIQUE)
    boolean existsByAppuntamento_Id(Long appuntamentoId);

    // 🔥 Serve per filtrare gli appuntamenti nel frontend
    @Query("select t.appuntamento.id from Terapia t")
    List<Long> findAllAppuntamentoIdsWithTerapia();
}
