package com.cardiocare360.service;

import java.util.List;

import com.cardiocare360.model.entity.Appuntamento;
import com.cardiocare360.model.entity.Terapia;
import com.cardiocare360.model.entity.ParametroClinico;
import com.cardiocare360.model.entity.Esame;
import com.cardiocare360.model.request.PazienteUpdateDTO;
import com.cardiocare360.model.response.PazienteDTO;

public interface PazienteService {

    // 🔵 Profilo paziente
    PazienteDTO getProfilo(Long idPaziente);

    // 🔵 Aggiornamento profilo
    PazienteDTO aggiornaProfilo(Long idPaziente, PazienteUpdateDTO request);

    // 🔵 Lista pazienti
    List<PazienteDTO> getAllPazienti();

    // 🔵 Storico visite
    List<Appuntamento> getVisiteByPaziente(Long idPaziente);

    // 🔵 Terapie attive
    List<Terapia> getTerapieByPaziente(Long idPaziente);

    // 🔵 Parametri clinici
    List<ParametroClinico> getParametriByPaziente(Long idPaziente);

    // 🔵 Storico esami
    List<Esame> getEsamiByPaziente(Long idPaziente);
    
    // Crea paziente
    PazienteDTO creaPaziente(PazienteUpdateDTO request);

}
