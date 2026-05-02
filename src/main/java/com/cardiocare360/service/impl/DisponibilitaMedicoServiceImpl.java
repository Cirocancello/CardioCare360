package com.cardiocare360.service.impl;

import com.cardiocare360.model.entity.DisponibilitaMedico;
import com.cardiocare360.model.entity.Medico;
import com.cardiocare360.repository.DisponibilitaMedicoRepository;
import com.cardiocare360.repository.MedicoRepository;
import com.cardiocare360.service.DisponibilitaMedicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DisponibilitaMedicoServiceImpl implements DisponibilitaMedicoService {

    private final DisponibilitaMedicoRepository disponibilitaRepo;
    private final MedicoRepository medicoRepo;

    @Override
    public DisponibilitaMedico creaDisponibilita(Long idMedico, String giornoSettimana, String oraInizio, String oraFine) {

        // Controllo disponibilità già esistente
        if (disponibilitaRepo.existsByMedicoIdAndGiornoSettimana(idMedico, giornoSettimana)) {
            throw new RuntimeException("Disponibilità già presente per questo giorno");
        }

        Medico medico = medicoRepo.findById(idMedico)
                .orElseThrow(() -> new RuntimeException("Medico non trovato"));

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
    public DisponibilitaMedico getDisponibilitaByMedicoAndGiorno(Long idMedico, String giornoSettimana) {
        return disponibilitaRepo.findByMedicoIdAndGiornoSettimana(idMedico, giornoSettimana)
                .orElseThrow(() -> new RuntimeException("Disponibilità non trovata"));
    }
}
