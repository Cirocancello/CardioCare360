package com.cardiocare360.service.impl;

import com.cardiocare360.model.entity.Medico;
import com.cardiocare360.model.request.MedicoUpdateDTO;
import com.cardiocare360.model.response.MedicoResponse;
import com.cardiocare360.repository.MedicoRepository;
import com.cardiocare360.service.MedicoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicoServiceImpl implements MedicoService {

    private final MedicoRepository medicoRepository;

    public MedicoServiceImpl(MedicoRepository medicoRepository) {
        this.medicoRepository = medicoRepository;
    }

    @Override
    public MedicoResponse getMedicoById(Long id) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medico non trovato"));

        return convertToResponse(medico);
    }

    @Override
    public MedicoResponse updateMedico(Long id, MedicoUpdateDTO updateDTO) {
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

        return convertToResponse(medico);
    }

    @Override
    public List<MedicoResponse> getMediciBySpecializzazione(String specializzazione) {

        List<Medico> medici = medicoRepository.findBySpecializzazioneIgnoreCase(specializzazione);

        return medici.stream()
                .map(this::convertToResponse)
                .toList();
    }

    private MedicoResponse convertToResponse(Medico medico) {
        MedicoResponse response = new MedicoResponse();
        response.setId(medico.getId());
        response.setNomeCompleto(medico.getNome() + " " + medico.getCognome());
        response.setEmail(medico.getEmail());
        response.setSpecializzazione(medico.getSpecializzazione());
        response.setNumeroLicenza(medico.getNumeroLicenza());
        return response;
    }
}
