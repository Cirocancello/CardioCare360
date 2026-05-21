package com.cardiocare360.controller;

import com.cardiocare360.model.request.MedicoUpdateDTO;
import com.cardiocare360.model.response.MedicoResponse;
import com.cardiocare360.service.MedicoService;
import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/medici")
public class MedicoController {

    private final MedicoService medicoService;

    public MedicoController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    @GetMapping("/{id}")
    public MedicoResponse getMedico(@PathVariable Long id) {
        return medicoService.getMedicoById(id);
    }

    @PutMapping("/{id}")
    public MedicoResponse updateMedico(@PathVariable Long id,
                                       @RequestBody MedicoUpdateDTO updateDTO) {
        return medicoService.updateMedico(id, updateDTO);
    }
    
    @GetMapping("/visita/{specializzazione}")
    public List<MedicoResponse> getMediciBySpecializzazione(@PathVariable String specializzazione) {
        return medicoService.getMediciBySpecializzazione(specializzazione);
    }

}
