package com.cardiocare360.service.impl;

import com.cardiocare360.model.entity.DisponibilitaMedico;
import com.cardiocare360.model.entity.SlotOrario;
import com.cardiocare360.repository.DisponibilitaMedicoRepository;
import com.cardiocare360.repository.SlotOrarioRepository;
import com.cardiocare360.service.SlotOrarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SlotOrarioServiceImpl implements SlotOrarioService {

    private final DisponibilitaMedicoRepository disponibilitaRepo;
    private final SlotOrarioRepository slotRepo;

    // ⭐ Mappa sicura e definitiva per convertire DayOfWeek → "LUN", "MAR", ...
    private static final Map<DayOfWeek, String> MAPPA_GIORNI = Map.of(
            DayOfWeek.MONDAY, "LUN",
            DayOfWeek.TUESDAY, "MAR",
            DayOfWeek.WEDNESDAY, "MER",
            DayOfWeek.THURSDAY, "GIO",
            DayOfWeek.FRIDAY, "VEN",
            DayOfWeek.SATURDAY, "SAB",
            DayOfWeek.SUNDAY, "DOM"
    );

    @Override
    public List<SlotOrario> generaSlotPerData(Long idMedico, LocalDate data) {

        // ⭐ Conversione sicura e coerente con il DB
        String giornoSettimana = MAPPA_GIORNI.get(data.getDayOfWeek());

        // ⭐ Recupero disponibilità (ora funziona SEMPRE)
        DisponibilitaMedico disp = disponibilitaRepo
                .findByMedicoIdAndGiornoSettimana(idMedico, giornoSettimana)
                .orElseThrow(() -> new RuntimeException("Il medico non è disponibile in questo giorno"));

        LocalTime start = disp.getOraInizio();
        LocalTime end = disp.getOraFine();

        List<SlotOrario> slots = new ArrayList<>();

        // ⭐ Generazione slot da 30 minuti
        while (start.isBefore(end)) {
            SlotOrario slot = new SlotOrario();
            slot.setDisponibilita(disp);
            slot.setInizio(LocalDateTime.of(data, start));
            slot.setFine(LocalDateTime.of(data, start.plusMinutes(30)));
            slot.setPrenotato(false);

            slots.add(slot);
            start = start.plusMinutes(30);
        }

        return slotRepo.saveAll(slots);
    }

    @Override
    public List<SlotOrario> getSlotDisponibili(Long idMedico, LocalDate data) {
        List<SlotOrario> slots = generaSlotPerData(idMedico, data);
        return slots.stream().filter(s -> !s.isPrenotato()).toList();
    }

    @Override
    public SlotOrario prenotaSlot(Long idSlot) {
        SlotOrario slot = slotRepo.findById(idSlot)
                .orElseThrow(() -> new RuntimeException("Slot non trovato"));

        if (slot.isPrenotato()) {
            throw new RuntimeException("Slot già prenotato");
        }

        slot.setPrenotato(true);
        return slotRepo.save(slot);
    }

    @Override
    public void liberaSlot(Long idSlot) {
        SlotOrario slot = slotRepo.findById(idSlot)
                .orElseThrow(() -> new RuntimeException("Slot non trovato"));

        slot.setPrenotato(false);
        slotRepo.save(slot);
    }
}
