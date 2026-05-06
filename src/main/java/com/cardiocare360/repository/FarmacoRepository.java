package com.cardiocare360.repository;

import com.cardiocare360.model.entity.Farmaco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FarmacoRepository extends JpaRepository<Farmaco, Long> {
}
