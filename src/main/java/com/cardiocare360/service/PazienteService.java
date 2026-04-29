package com.cardiocare360.service;

import com.cardiocare360.model.dto.PazienteDTO;
import com.cardiocare360.model.dto.PazienteUpdateDTO;

public interface PazienteService {

    PazienteDTO getProfilo(Long idPaziente);

    PazienteDTO aggiornaProfilo(Long idPaziente, PazienteUpdateDTO request);
}
