package com.cardiocare360.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cardiocare360.model.entity.Paziente;
import com.cardiocare360.model.request.PazienteUpdateDTO;
import com.cardiocare360.model.response.PazienteDTO;
import com.cardiocare360.repository.PazienteRepository;
import com.cardiocare360.service.PazienteService;

import java.time.LocalDate;

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

        // 🔵 Aggiornamento sicuro: solo se il campo NON è null
        if (request.getNome() != null) p.setNome(request.getNome());
        if (request.getCognome() != null) p.setCognome(request.getCognome());
        if (request.getCodiceFiscale() != null) p.setCodiceFiscale(request.getCodiceFiscale());
        if (request.getTelefono() != null) p.setTelefono(request.getTelefono());
        if (request.getIndirizzo() != null) p.setIndirizzo(request.getIndirizzo());
        if (request.getLuogoNascita() != null) p.setLuogoNascita(request.getLuogoNascita());

        // 🔵 Conversione sicura della data
        if (request.getDataNascita() != null) {
            try {
                p.setDataNascita(LocalDate.parse(request.getDataNascita()));
            } catch (Exception e) {
                throw new RuntimeException("Formato data non valido (yyyy-MM-dd)");
            }
        }

        pazienteRepository.save(p);

        return new PazienteDTO(p);
    }
}
