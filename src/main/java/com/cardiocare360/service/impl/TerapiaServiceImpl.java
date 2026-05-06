package com.cardiocare360.service.impl;

import com.cardiocare360.model.entity.*;
import com.cardiocare360.repository.*;
import com.cardiocare360.service.TerapiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TerapiaServiceImpl implements TerapiaService {

    @Autowired
    private TerapiaRepository terapiaRepository;

    @Autowired
    private PazienteRepository pazienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private FarmacoRepository farmacoRepository;

    @Autowired
    private AppuntamentoRepository appuntamentoRepository;

    @Override
    public Terapia creaTerapia(Long pazienteId,
                               Long medicoId,
                               Long farmacoId,
                               Long appuntamentoId,
                               String dosaggio,
                               String note,
                               String dataInizio,
                               String dataFine) {

        Paziente paziente = pazienteRepository.findById(pazienteId)
                .orElseThrow(() -> new RuntimeException("Paziente non trovato"));

        Medico medico = medicoRepository.findById(medicoId)
                .orElseThrow(() -> new RuntimeException("Medico non trovato"));

        Farmaco farmaco = farmacoRepository.findById(farmacoId)
                .orElseThrow(() -> new RuntimeException("Farmaco non trovato"));

        Appuntamento appuntamento = appuntamentoRepository.findById(appuntamentoId)
                .orElseThrow(() -> new RuntimeException("Appuntamento non trovato"));

        Terapia terapia = new Terapia();
        terapia.setPaziente(paziente);
        terapia.setMedico(medico);
        terapia.setFarmaco(farmaco);
        terapia.setAppuntamento(appuntamento);
        terapia.setDosaggio(dosaggio);
        terapia.setNote(note);
        terapia.setDataInizio(LocalDate.parse(dataInizio));

        if (dataFine != null && !dataFine.isEmpty()) {
            terapia.setDataFine(LocalDate.parse(dataFine));
        }

        return terapiaRepository.save(terapia);
    }

    @Override
    public Terapia getTerapiaById(Long id) {
        return terapiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Terapia non trovata"));
    }

    @Override
    public List<Terapia> getTerapiePaziente(Long pazienteId) {
        return terapiaRepository.findByPaziente_Id(pazienteId);
    }

    @Override
    public List<Terapia> getTerapieMedico(Long medicoId) {
        return terapiaRepository.findByMedico_Id(medicoId);
    }
}
