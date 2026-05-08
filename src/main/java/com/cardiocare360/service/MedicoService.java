package com.cardiocare360.service;

import com.cardiocare360.model.request.MedicoUpdateDTO;
import com.cardiocare360.model.response.MedicoResponse;

public interface MedicoService {

    MedicoResponse getMedicoById(Long id);

    MedicoResponse updateMedico(Long id, MedicoUpdateDTO updateDTO);
}
