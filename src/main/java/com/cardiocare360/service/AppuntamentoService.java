package com.cardiocare360.service;

import java.time.LocalDate;
import java.util.List;

import com.cardiocare360.model.response.AppuntamentoDTO;

public interface AppuntamentoService {

    // ⭐ CREAZIONE
    AppuntamentoDTO creaAppuntamento(AppuntamentoDTO dto, Long idPaziente);

    // ⭐ LETTURA
    List<AppuntamentoDTO> getAppuntamentiPaziente(Long idPaziente);
    List<AppuntamentoDTO> getAppuntamentiMedico(Long idMedico);
    List<AppuntamentoDTO> getAppuntamentiFuturiPaziente(Long idPaziente);
    List<AppuntamentoDTO> getAppuntamentiFuturiMedico(Long idMedico);

    AppuntamentoDTO getAppuntamentoById(Long id, Long idUtente);

    // ⭐ AGGIORNAMENTO
    AppuntamentoDTO aggiornaAppuntamento(Long id, AppuntamentoDTO dto, Long idUtente);

    // ⭐ CAMBIO STATO
    AppuntamentoDTO aggiornaStato(Long idAppuntamento, String nuovoStato, Long idUtente);
    AppuntamentoDTO confermaAppuntamento(Long idAppuntamento, Long idMedico);
    AppuntamentoDTO completaAppuntamento(Long idAppuntamento, Long idMedico);
    AppuntamentoDTO annullaAppuntamento(Long idAppuntamento, Long idUtente);

    // ⭐ ELIMINAZIONE (solo admin)
    boolean eliminaAppuntamento(Long idAppuntamento, Long idUtente);

    // ⭐ UTILITY
    Long getIdUtenteByEmail(String email);
    Long getIdMedicoByEmail(String email);

    List<String> getOrariOccupati(Long idMedico, LocalDate data);

    // ⭐ TERAPIE
    List<AppuntamentoDTO> getAppuntamentiDisponibili(Long idMedico);
}
