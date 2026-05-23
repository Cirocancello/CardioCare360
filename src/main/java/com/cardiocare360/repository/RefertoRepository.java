package com.cardiocare360.repository;

import com.cardiocare360.model.entity.Referto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RefertoRepository extends JpaRepository<Referto, Long> {

    // Tutti i referti associati a un esame
    List<Referto> findByEsame_Id(Long esameId);

    // Controlla se esistono referti per un esame
    boolean existsByEsame_Id(Long esameId);
}
