package com.cardiocare360.repository;

import com.cardiocare360.model.entity.Appuntamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface AppuntamentoRepository extends JpaRepository<Appuntamento, Long> {

    // Appuntamenti del paziente
    List<Appuntamento> findByPazienteId(Long idPaziente);

    // Appuntamenti del medico
    List<Appuntamento> findByMedicoId(Long idMedico);

    // Controllo sovrapposizione medico
    boolean existsByMedicoIdAndDataAppuntamentoAndOraAppuntamento(
            Long idMedico, LocalDate data, LocalTime ora);

    // Controllo sovrapposizione paziente
    boolean existsByPazienteIdAndDataAppuntamentoAndOraAppuntamento(
            Long idPaziente, LocalDate data, LocalTime ora);

    // Controllo proprietà appuntamento (paziente)
    Optional<Appuntamento> findByIdAndPazienteId(Long id, Long idPaziente);

    // Controllo proprietà appuntamento (medico)
    Optional<Appuntamento> findByIdAndMedicoId(Long id, Long idMedico);
}
