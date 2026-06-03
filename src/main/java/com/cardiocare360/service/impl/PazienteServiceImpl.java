package com.cardiocare360.service.impl;

import com.cardiocare360.model.entity.Appuntamento;
import com.cardiocare360.model.entity.Esame;
import com.cardiocare360.model.entity.ParametroClinico;
import com.cardiocare360.model.entity.Paziente;
import com.cardiocare360.model.entity.Terapia;
import com.cardiocare360.model.request.PazienteUpdateDTO;
import com.cardiocare360.model.response.PazienteDTO;
import com.cardiocare360.repository.AppuntamentoRepository;
import com.cardiocare360.repository.EsameRepository;
import com.cardiocare360.repository.ParametroClinicoRepository;
import com.cardiocare360.repository.PazienteRepository;
import com.cardiocare360.repository.TerapiaRepository;
import com.cardiocare360.service.PazienteService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PazienteServiceImpl implements PazienteService {

    private final PazienteRepository pazienteRepository;
    private final AppuntamentoRepository appuntamentoRepository;
    private final TerapiaRepository terapiaRepository;
    private final ParametroClinicoRepository parametroClinicoRepository;
    private final EsameRepository esameRepository;

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

        if (request.getNome() != null) p.setNome(request.getNome());
        if (request.getCognome() != null) p.setCognome(request.getCognome());
        if (request.getCodiceFiscale() != null) p.setCodiceFiscale(request.getCodiceFiscale());
        if (request.getTelefono() != null) p.setTelefono(request.getTelefono());
        if (request.getIndirizzo() != null) p.setIndirizzo(request.getIndirizzo());
        if (request.getLuogoNascita() != null) p.setLuogoNascita(request.getLuogoNascita());

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

    @Override
    public List<PazienteDTO> getAllPazienti() {
        return pazienteRepository.findAll()
                .stream()
                .map(PazienteDTO::new)
                .toList();
    }

    // 🔵 VISITE
    @Override
    public List<Appuntamento> getVisiteByPaziente(Long idPaziente) {
        return appuntamentoRepository.findByPazienteId(idPaziente);
    }

    // 🔵 TERAPIE
    @Override
    public List<Terapia> getTerapieByPaziente(Long idPaziente) {
        return terapiaRepository.findByPazienteId(idPaziente);
    }

    // 🔵 PARAMETRI CLINICI
    @Override
    public List<ParametroClinico> getParametriByPaziente(Long idPaziente) {
        return parametroClinicoRepository.findByPazienteId(idPaziente);
    }

    // 🔵 ESAMI
    @Override
    public List<Esame> getEsamiByPaziente(Long idPaziente) {
        return esameRepository.findByPazienteId(idPaziente);
    }
}
