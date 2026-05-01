package com.cardiocare360.service.impl;

import com.cardiocare360.model.dto.MedicoDTO;
import com.cardiocare360.model.dto.MedicoUpdateDTO;
import com.cardiocare360.model.entity.Medico;
import com.cardiocare360.repository.MedicoRepository;
import com.cardiocare360.service.MedicoService;
import org.springframework.stereotype.Service;

@Service
public class MedicoServiceImpl implements MedicoService {

    private final MedicoRepository medicoRepository;

    public MedicoServiceImpl(MedicoRepository medicoRepository) {
        this.medicoRepository = medicoRepository;
    }

    @Override
    public MedicoDTO getMedicoById(Long id) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medico non trovato"));

        return convertToDTO(medico);
    }

    @Override
    public MedicoDTO updateMedico(Long id, MedicoUpdateDTO updateDTO) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medico non trovato"));

        // Campi ereditati da Utente
        if (updateDTO.getNome() != null) {
            medico.setNome(updateDTO.getNome());
        }
        if (updateDTO.getCognome() != null) {
            medico.setCognome(updateDTO.getCognome());
        }

        // Campi specifici di Medico
        if (updateDTO.getSpecializzazione() != null) {
            medico.setSpecializzazione(updateDTO.getSpecializzazione());
        }
        if (updateDTO.getNumeroLicenza() != null) {
            medico.setNumeroLicenza(updateDTO.getNumeroLicenza());
        }

        medicoRepository.save(medico);

        return convertToDTO(medico);
    }

    private MedicoDTO convertToDTO(Medico medico) {
        MedicoDTO dto = new MedicoDTO();
        dto.setId(medico.getId());
        dto.setNomeCompleto(medico.getNome() + " " + medico.getCognome());
        dto.setEmail(medico.getEmail());
        dto.setSpecializzazione(medico.getSpecializzazione());
        dto.setNumeroLicenza(medico.getNumeroLicenza());
        return dto;
    }
}
