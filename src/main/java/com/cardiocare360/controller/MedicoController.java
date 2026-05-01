package com.cardiocare360.controller;

import com.cardiocare360.model.dto.MedicoDTO;
import com.cardiocare360.model.dto.MedicoUpdateDTO;
import com.cardiocare360.model.response.MedicoResponse;
import com.cardiocare360.service.MedicoService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/medico")
public class MedicoController {

    private final MedicoService medicoService;

    public MedicoController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    @GetMapping("/{id}")
    public MedicoResponse getMedico(@PathVariable Long id) {
        MedicoDTO dto = medicoService.getMedicoById(id);

        MedicoResponse response = new MedicoResponse();
        response.setId(dto.getId());
        response.setNomeCompleto(dto.getNomeCompleto());
        response.setEmail(dto.getEmail());
        response.setSpecializzazione(dto.getSpecializzazione());
        response.setNumeroLicenza(dto.getNumeroLicenza());

        return response;
    }

    @PutMapping("/{id}")
    public MedicoResponse updateMedico(@PathVariable Long id,
                                       @RequestBody MedicoUpdateDTO updateDTO) {

        MedicoDTO dto = medicoService.updateMedico(id, updateDTO);

        MedicoResponse response = new MedicoResponse();
        response.setId(dto.getId());
        response.setNomeCompleto(dto.getNomeCompleto());
        response.setEmail(dto.getEmail());
        response.setSpecializzazione(dto.getSpecializzazione());
        response.setNumeroLicenza(dto.getNumeroLicenza());

        return response;
    }
}
