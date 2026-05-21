package com.cardiocare360.service;

import java.time.LocalDate;
import java.util.List;

import com.cardiocare360.model.response.AppuntamentoDTO;

public interface AppuntamentoService {

    AppuntamentoDTO creaAppuntamento(AppuntamentoDTO dto, Long idPaziente);

    List<AppuntamentoDTO> getAppuntamentiPaziente(Long idPaziente);

    List<AppuntamentoDTO> getAppuntamentiMedico(Long idMedico);

    AppuntamentoDTO aggiornaStato(Long idAppuntamento, String nuovoStato, Long idUtente);

    boolean eliminaAppuntamento(Long idAppuntamento, Long idUtente);

    // 🔥 Metodi aggiunti per il controller
    Long getIdUtenteByEmail(String email);

    Long getIdMedicoByEmail(String email);

    // 🔥 NUOVO METODO — orari occupati del medico
    List<String> getOrariOccupati(Long idMedico, LocalDate data);
}
