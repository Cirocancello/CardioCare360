package com.cardiocare360.service;

import com.cardiocare360.model.request.PazienteUpdateDTO;
import com.cardiocare360.model.response.PazienteDTO;

public interface PazienteService {

    PazienteDTO getProfilo(Long idPaziente);

    PazienteDTO aggiornaProfilo(Long idPaziente, PazienteUpdateDTO request);
}
