package com.cardiocare360.service;

import com.cardiocare360.model.entity.Terapia;

import java.util.List;

public interface TerapiaService {

    Terapia creaTerapia(Long pazienteId,
                        Long medicoId,
                        Long farmacoId,
                        Long appuntamentoId,
                        String dosaggio,
                        String note,
                        String dataInizio,
                        String dataFine);

    Terapia getTerapiaById(Long id);

    List<Terapia> getTerapiePaziente(Long pazienteId);

    List<Terapia> getTerapieMedico(Long medicoId);
}
