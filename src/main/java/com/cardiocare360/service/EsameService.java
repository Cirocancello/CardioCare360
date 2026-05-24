package com.cardiocare360.service;

import com.cardiocare360.model.response.DisponibilitaEsameResponse;
import com.cardiocare360.model.response.EsameDTO;
import com.cardiocare360.model.response.RefertoDTO;
import java.util.List;

public interface EsameService {

    EsameDTO creaEsame(EsameDTO dto);

    List<EsameDTO> getEsamiPaziente(Long idPaziente);

    List<EsameDTO> getEsamiMedico(Long idMedico);

    EsameDTO getEsameById(Long id);

    EsameDTO aggiornaStatoEsame(Long idEsame, String nuovoStato);

    void eliminaEsame(Long idEsame);

    // ⭐ Metodo mancante per ottenere il referto
    RefertoDTO getRefertoByEsame(Long idEsame);
    
    // 🔥 Nuovo metodo per calcolare la prossima disponibilità
    DisponibilitaEsameResponse calcolaProssimaDisponibilita(String tipoEsame);
}
