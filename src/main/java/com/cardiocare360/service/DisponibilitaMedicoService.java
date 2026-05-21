package com.cardiocare360.service;

import com.cardiocare360.model.entity.DisponibilitaMedico;

import java.util.List;

public interface DisponibilitaMedicoService {

    DisponibilitaMedico creaDisponibilita(Long idMedico, String giornoSettimana, String oraInizio, String oraFine);

    DisponibilitaMedico modificaDisponibilita(Long idDisponibilita, String oraInizio, String oraFine);

    void eliminaDisponibilita(Long idDisponibilita);

    List<DisponibilitaMedico> getDisponibilitaMedico(Long idMedico);

    List<DisponibilitaMedico> getDisponibilitaByMedici(List<Long> idMedici);

    List<String> generaDateDisponibili(Long idMedico);
    
    List<String> generaDateDisponibiliPerSpecializzazione(String specializzazione);
    
    List<DisponibilitaMedico> getDisponibilitaByMedicoAndGiorno(Long idMedico, String giornoSettimana);


}
