package com.cardiocare360.service;

import com.cardiocare360.model.dto.MedicoDTO;
import com.cardiocare360.model.dto.MedicoUpdateDTO;

public interface MedicoService {

    MedicoDTO getMedicoById(Long id);

    MedicoDTO updateMedico(Long id, MedicoUpdateDTO updateDTO);
}
