package com.cardiocare360.repository;

import com.cardiocare360.model.entity.Referto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RefertoRepository extends JpaRepository<Referto, Long> {

    List<Referto> findByPaziente_Id(Long pazienteId);

    List<Referto> findByMedico_Id(Long medicoId);

    List<Referto> findByPaziente_IdOrderByDataCreazioneDesc(Long pazienteId);

    List<Referto> findByDataCreazioneBetween(LocalDateTime start, LocalDateTime end);
}
