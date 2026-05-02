package com.cardiocare360.service;

import com.cardiocare360.model.entity.DisponibilitaMedico;

import java.util.List;

public interface DisponibilitaMedicoService {

    // giornoSettimana = "LUN", "MAR", "MER", ecc.
    DisponibilitaMedico creaDisponibilita(Long idMedico, String giornoSettimana, String oraInizio, String oraFine);

    DisponibilitaMedico modificaDisponibilita(Long idDisponibilita, String oraInizio, String oraFine);

    void eliminaDisponibilita(Long idDisponibilita);

    List<DisponibilitaMedico> getDisponibilitaMedico(Long idMedico);

    DisponibilitaMedico getDisponibilitaByMedicoAndGiorno(Long idMedico, String giornoSettimana);
}
