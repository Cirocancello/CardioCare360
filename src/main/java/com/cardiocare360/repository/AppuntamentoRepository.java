package com.cardiocare360.repository;

import com.cardiocare360.model.entity.Appuntamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface AppuntamentoRepository extends JpaRepository<Appuntamento, Long> {

    // Appuntamenti del paziente con medico incluso
    @Query("SELECT a FROM Appuntamento a JOIN FETCH a.medico WHERE a.paziente.id = :idPaziente")
    List<Appuntamento> findByPazienteId(@Param("idPaziente") Long idPaziente);

    // Appuntamenti del medico
    List<Appuntamento> findByMedicoId(Long idMedico);

    // Appuntamenti del medico in una data
    List<Appuntamento> findByMedicoIdAndDataAppuntamento(Long idMedico, LocalDate data);

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
