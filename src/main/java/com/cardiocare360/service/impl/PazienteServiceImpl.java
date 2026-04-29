package com.cardiocare360.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cardiocare360.model.dto.PazienteDTO;
import com.cardiocare360.model.dto.PazienteUpdateDTO;
import com.cardiocare360.model.entity.Paziente;
import com.cardiocare360.repository.PazienteRepository;
import com.cardiocare360.service.PazienteService;

@Service
public class PazienteServiceImpl implements PazienteService {

    @Autowired
    private PazienteRepository pazienteRepository;

    @Override
    public PazienteDTO getProfilo(Long idPaziente) {
        Paziente p = pazienteRepository.findById(idPaziente)
                .orElseThrow(() -> new RuntimeException("Paziente non trovato"));

        return new PazienteDTO(p);
    }

    @Override
    public PazienteDTO aggiornaProfilo(Long idPaziente, PazienteUpdateDTO request) {
        Paziente p = pazienteRepository.findById(idPaziente)
                .orElseThrow(() -> new RuntimeException("Paziente non trovato"));

        // 🔵 Campi di UTENTE (superclasse)
        p.setNome(request.getNome());
        p.setCognome(request.getCognome());

        // 🔵 Campi di PAZIENTE (sottoclasse)
        p.setCodiceFiscale(request.getCodiceFiscale());
        p.setDataNascita(request.getDataNascita());
        p.setTelefono(request.getTelefono());
        p.setIndirizzo(request.getIndirizzo());

        pazienteRepository.save(p);

        return new PazienteDTO(p);
    }
}
