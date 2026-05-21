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
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SlotOrarioServiceImpl implements SlotOrarioService {

    private final DisponibilitaMedicoRepository disponibilitaRepo;
    private final SlotOrarioRepository slotRepo;

    private static final Map<DayOfWeek, String> MAPPA_GIORNI = Map.of(
            DayOfWeek.MONDAY, "LUNEDI",
            DayOfWeek.TUESDAY, "MARTEDI",
            DayOfWeek.WEDNESDAY, "MERCOLEDI",
            DayOfWeek.THURSDAY, "GIOVEDI",
            DayOfWeek.FRIDAY, "VENERDI",
            DayOfWeek.SATURDAY, "SABATO",
            DayOfWeek.SUNDAY, "DOMENICA"
    );

    @Override
    public List<SlotOrario> generaSlotPerData(Long idMedico, LocalDate data) {

        String giornoSettimana = MAPPA_GIORNI.get(data.getDayOfWeek());

        // 🔥 Recupera TUTTE le disponibilità del medico per quel giorno
        List<DisponibilitaMedico> disponibilitaList =
                disponibilitaRepo.findByMedicoIdAndGiornoSettimana(idMedico, giornoSettimana);

        if (disponibilitaList.isEmpty()) {
            throw new RuntimeException("Il medico non è disponibile in questo giorno");
        }

        List<SlotOrario> tuttiGliSlot = new ArrayList<>();

        for (DisponibilitaMedico disp : disponibilitaList) {

            // 🔥 Se gli slot esistono già → li riutilizziamo
            List<SlotOrario> slotEsistenti = slotRepo.findByDisponibilitaIdAndInizioBetween(
                    disp.getId(),
                    data.atStartOfDay(),
                    data.atTime(23, 59)
            );

            if (!slotEsistenti.isEmpty()) {
                tuttiGliSlot.addAll(slotEsistenti);
                continue;
            }

            // 🔧 Generazione slot per questa fascia oraria
            LocalTime start = disp.getOraInizio();
            LocalTime end = disp.getOraFine();

            while (start.isBefore(end)) {
                SlotOrario slot = new SlotOrario();
                slot.setDisponibilita(disp);
                slot.setInizio(LocalDateTime.of(data, start));
                slot.setFine(LocalDateTime.of(data, start.plusMinutes(30)));
                slot.setPrenotato(false);

                tuttiGliSlot.add(slot);
                start = start.plusMinutes(30);
            }
        }

        // Salva solo gli slot nuovi
        List<SlotOrario> salvati = slotRepo.saveAll(tuttiGliSlot);

        // Ordina per orario
        salvati.sort(Comparator.comparing(SlotOrario::getInizio));

        return salvati;
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
