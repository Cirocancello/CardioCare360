package com.cardiocare360.service;

import com.cardiocare360.model.response.EsameDTO;
import java.util.List;

public interface EsameService {

    // Creazione / prenotazione esame
    EsameDTO creaEsame(EsameDTO dto);

    // Lista esami del paziente
    List<EsameDTO> getEsamiPaziente(Long idPaziente);

    // Lista esami del medico (per refertazione)
    List<EsameDTO> getEsamiMedico(Long idMedico);

    // Dettaglio esame
    EsameDTO getEsameById(Long id);

    // Aggiornamento stato (PRENOTATO → COMPLETATO → REFERTATO)
    EsameDTO aggiornaStatoEsame(Long idEsame, String nuovoStato);

    // Eliminazione esame
    void eliminaEsame(Long idEsame);
}
