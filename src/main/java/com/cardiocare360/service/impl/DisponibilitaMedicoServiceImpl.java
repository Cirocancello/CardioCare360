package com.cardiocare360.service.impl;

import com.cardiocare360.model.entity.DisponibilitaMedico;
import com.cardiocare360.model.entity.Medico;
import com.cardiocare360.repository.DisponibilitaMedicoRepository;
import com.cardiocare360.repository.MedicoRepository;
import com.cardiocare360.service.DisponibilitaMedicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DisponibilitaMedicoServiceImpl implements DisponibilitaMedicoService {

    private final DisponibilitaMedicoRepository disponibilitaRepo;
    private final MedicoRepository medicoRepo;

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
    public DisponibilitaMedico creaDisponibilita(Long idMedico, String giornoSettimana, String oraInizio, String oraFine) {
        Medico medico = medicoRepo.findById(idMedico)
                .orElseThrow(() -> new RuntimeException("Medico non trovato"));

        boolean esisteDuplicato = disponibilitaRepo.findByMedicoIdAndGiornoSettimana(idMedico, giornoSettimana)
                .stream()
                .anyMatch(d -> d.getOraInizio().equals(LocalTime.parse(oraInizio))
                        && d.getOraFine().equals(LocalTime.parse(oraFine)));

        if (esisteDuplicato) {
            throw new RuntimeException("Questa fascia oraria è già presente per il medico in quel giorno");
        }

        DisponibilitaMedico disp = new DisponibilitaMedico();
        disp.setMedico(medico);
        disp.setGiornoSettimana(giornoSettimana);
        disp.setOraInizio(LocalTime.parse(oraInizio));
        disp.setOraFine(LocalTime.parse(oraFine));

        return disponibilitaRepo.save(disp);
    }

    @Override
    public DisponibilitaMedico modificaDisponibilita(Long idDisponibilita, String oraInizio, String oraFine) {
        DisponibilitaMedico disp = disponibilitaRepo.findById(idDisponibilita)
                .orElseThrow(() -> new RuntimeException("Disponibilità non trovata"));

        disp.setOraInizio(LocalTime.parse(oraInizio));
        disp.setOraFine(LocalTime.parse(oraFine));

        return disponibilitaRepo.save(disp);
    }

    @Override
    public void eliminaDisponibilita(Long idDisponibilita) {
        disponibilitaRepo.deleteById(idDisponibilita);
    }

    @Override
    public List<DisponibilitaMedico> getDisponibilitaMedico(Long idMedico) {
        return disponibilitaRepo.findByMedicoId(idMedico);
    }

    @Override
    public List<DisponibilitaMedico> getDisponibilitaByMedicoAndGiorno(Long idMedico, String giornoSettimana) {
        List<DisponibilitaMedico> disponibilita = disponibilitaRepo.findByMedicoIdAndGiornoSettimana(idMedico, giornoSettimana);

        if (disponibilita.isEmpty()) {
            throw new RuntimeException("Nessuna disponibilità trovata per il medico in questo giorno");
        }

        return disponibilita;
    }

    @Override
    public List<DisponibilitaMedico> getDisponibilitaByMedici(List<Long> idMedici) {
        return disponibilitaRepo.findByMedicoIdIn(idMedici);
    }

    @Override
    public List<String> generaDateDisponibili(Long idMedico) {
        List<DisponibilitaMedico> disponibilita = disponibilitaRepo.findByMedicoId(idMedico);

        if (disponibilita.isEmpty()) {
            return List.of();
        }

        Set<String> giorniDisponibili = disponibilita.stream()
                .map(DisponibilitaMedico::getGiornoSettimana)
                .collect(Collectors.toSet());

        List<String> dateGenerate = new ArrayList<>();
        LocalDate oggi = LocalDate.now();
        LocalDate limite = oggi.plusDays(30);

        for (LocalDate data = oggi; data.isBefore(limite); data = data.plusDays(1)) {
            String giornoFormattato = MAPPA_GIORNI.get(data.getDayOfWeek());
            if (giorniDisponibili.contains(giornoFormattato)) {
                dateGenerate.add(data.toString());
            }
        }

        return dateGenerate;
    }

    @Override
    public List<String> generaDateDisponibiliPerSpecializzazione(String specializzazione) {
        List<Medico> medici = medicoRepo.findBySpecializzazione(specializzazione);

        if (medici.isEmpty()) {
            return List.of();
        }

        Medico medico = medici.stream()
                .filter(m -> !disponibilitaRepo.findByMedicoId(m.getId()).isEmpty())
                .findFirst()
                .orElse(null);

        if (medico == null) {
            return List.of();
        }

        return generaDateDisponibili(medico.getId());
    }

    @Override
    public DisponibilitaMedico salva(DisponibilitaMedico disponibilita) {
        return disponibilitaRepo.save(disponibilita);
    }
}
