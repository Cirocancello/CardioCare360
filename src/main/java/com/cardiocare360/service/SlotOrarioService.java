package com.cardiocare360.service;

import com.cardiocare360.model.entity.SlotOrario;

import java.time.LocalDate;
import java.util.List;

public interface SlotOrarioService {

    List<SlotOrario> generaSlotPerData(Long idMedico, LocalDate data);

    List<SlotOrario> getSlotDisponibili(Long idMedico, LocalDate data);

    SlotOrario prenotaSlot(Long idSlot);

    void liberaSlot(Long idSlot);
}
