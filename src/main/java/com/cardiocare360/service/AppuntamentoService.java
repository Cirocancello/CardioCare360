package com.cardiocare360.service;

import com.cardiocare360.model.dto.AppuntamentoDTO;

import java.util.List;

public interface AppuntamentoService {

    AppuntamentoDTO creaAppuntamento(AppuntamentoDTO dto, Long idPaziente);

    List<AppuntamentoDTO> getAppuntamentiPaziente(Long idPaziente);

    List<AppuntamentoDTO> getAppuntamentiMedico(Long idMedico);

    AppuntamentoDTO aggiornaStato(Long idAppuntamento, String nuovoStato, Long idUtente);

    boolean eliminaAppuntamento(Long idAppuntamento, Long idUtente);

    // 🔥 Metodi aggiunti per il controller
    Long getIdUtenteByEmail(String email);

    Long getIdMedicoByEmail(String email);
}
