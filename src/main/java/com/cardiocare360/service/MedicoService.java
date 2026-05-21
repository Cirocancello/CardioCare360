package com.cardiocare360.service;

import java.util.List;

import com.cardiocare360.model.request.MedicoUpdateDTO;
import com.cardiocare360.model.response.MedicoResponse;

public interface MedicoService {

    MedicoResponse getMedicoById(Long id);

    MedicoResponse updateMedico(Long id, MedicoUpdateDTO updateDTO);
    
    public List<MedicoResponse> getMediciBySpecializzazione(String specializzazione);

}
