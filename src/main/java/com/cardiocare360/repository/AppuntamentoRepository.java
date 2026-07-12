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

    @Query("SELECT a FROM Appuntamento a JOIN FETCH a.medico WHERE a.paziente.id = :idPaziente")
    List<Appuntamento> findByPazienteId(@Param("idPaziente") Long idPaziente);

    List<Appuntamento> findByMedicoId(Long idMedico);

    List<Appuntamento> findByMedicoIdAndDataAppuntamento(Long idMedico, LocalDate data);

    boolean existsByMedicoIdAndDataAppuntamentoAndOraAppuntamento(
            Long idMedico, LocalDate data, LocalTime ora);

    boolean existsByPazienteIdAndDataAppuntamentoAndOraAppuntamento(
            Long idPaziente, LocalDate data, LocalTime ora);

    Optional<Appuntamento> findByIdAndPazienteId(Long id, Long idPaziente);

    Optional<Appuntamento> findByIdAndMedicoId(Long id, Long idMedico);

    // ⭐ Appuntamenti futuri del medico
    boolean existsByMedicoIdAndDataAppuntamentoAfter(Long medicoId, LocalDate data);

    List<Appuntamento> findByMedicoIdAndDataAppuntamentoAfter(Long medicoId, LocalDate oggi);

    // ⭐ Appuntamenti futuri del paziente
    List<Appuntamento> findByPazienteIdAndDataAppuntamentoAfter(Long idPaziente, LocalDate oggi);

    // ⭐ Filtri per stato
    List<Appuntamento> findByMedicoIdAndStato(Long idMedico, Appuntamento.StatoAppuntamento stato);

    // ⭐ Appuntamenti non usati per terapia
    @Query("""
        SELECT a
        FROM Appuntamento a
        WHERE a.medico.id = :medicoId
          AND a.stato IN ('CONFERMATO', 'PRENOTATO', 'COMPLETATO')
          AND a.id NOT IN (
                SELECT t.appuntamento.id
                FROM Terapia t
          )
        ORDER BY a.dataAppuntamento
    """)
    List<Appuntamento> findAppuntamentiNonUsati(@Param("medicoId") Long medicoId);
}
