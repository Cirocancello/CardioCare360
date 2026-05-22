package com.cardiocare360.repository;

import com.cardiocare360.model.entity.Referto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefertoRepository extends JpaRepository<Referto, Long> {

    // Recupera il referto associato a un esame
    Referto findByEsame_Id(Long esameId);

    // Controlla se esiste un referto per un esame
    boolean existsByEsame_Id(Long esameId);
}
