package com.cardiocare360.service.impl;

import com.cardiocare360.model.entity.Esame;
import com.cardiocare360.model.entity.Medico;
import com.cardiocare360.model.entity.Paziente;
import com.cardiocare360.model.entity.Referto;
import com.cardiocare360.model.response.DisponibilitaEsameResponse;
import com.cardiocare360.model.response.EsameDTO;
import com.cardiocare360.model.response.RefertoDTO;
import com.cardiocare360.repository.EsameRepository;
import com.cardiocare360.repository.MedicoRepository;
import com.cardiocare360.repository.PazienteRepository;
import com.cardiocare360.repository.RefertoRepository;
import com.cardiocare360.service.EsameService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class EsameServiceImpl implements EsameService {

    @Autowired
    private EsameRepository esameRepository;

    @Autowired
    private PazienteRepository pazienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private RefertoRepository refertoRepository;

    @Override
    public EsameDTO creaEsame(EsameDTO dto) {
        Esame esame = new Esame();

        Paziente paziente = pazienteRepository.findById(dto.getIdPaziente())
                .orElseThrow(() -> new RuntimeException("Paziente non trovato"));

        Medico medico = medicoRepository.findById(dto.getIdMedico())
                .orElseThrow(() -> new RuntimeException("Medico non trovato"));

        esame.setPaziente(paziente);
        esame.setMedico(medico);
        esame.setTipoEsame(dto.getTipoEsame());
        esame.setDataEsame(dto.getDataEsame());
        esame.setOraEsame(dto.getOraEsame());
        esame.setStato(Esame.StatoEsame.PRENOTATO);
        esame.setNote(dto.getNote());

        esameRepository.save(esame);

        return convertToDTO(esame);
    }

    @Override
    public List<EsameDTO> getEsamiPaziente(Long idPaziente) {
        return esameRepository.findByPazienteId(idPaziente)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public List<EsameDTO> getEsamiMedico(Long idMedico) {
        return esameRepository.findByMedicoId(idMedico)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public EsameDTO getEsameById(Long id) {
        Esame esame = esameRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Esame non trovato"));
        return convertToDTO(esame);
    }

    @Override
    public EsameDTO aggiornaStatoEsame(Long idEsame, String nuovoStato) {
        Esame esame = esameRepository.findById(idEsame)
                .orElseThrow(() -> new RuntimeException("Esame non trovato"));

        esame.setStato(Esame.StatoEsame.valueOf(nuovoStato));
        esameRepository.save(esame);

        return convertToDTO(esame);
    }

    @Override
    public void eliminaEsame(Long idEsame) {
        if (!esameRepository.existsById(idEsame)) {
            throw new RuntimeException("Esame non trovato");
        }
        esameRepository.deleteById(idEsame);
    }

    // ⭐ Recupero referto dell’esame (ultimo referto)
    @Override
    public RefertoDTO getRefertoByEsame(Long idEsame) {

        List<Referto> referti = refertoRepository.findByEsame_Id(idEsame);

        if (referti.isEmpty()) {
            throw new RuntimeException("Referto non presente per questo esame");
        }

        // Prendi l’ultimo referto
        Referto referto = referti.get(referti.size() - 1);

        RefertoDTO dto = new RefertoDTO();
        dto.setId(referto.getId());
        dto.setEsameId(idEsame);

        dto.setMedicoId(referto.getMedico().getId());
        dto.setNomeMedico(referto.getMedico().getNome());
        dto.setCognomeMedico(referto.getMedico().getCognome());

        dto.setNoteMedico(referto.getNoteMedico());
        dto.setFilePath(referto.getFilePath());
        dto.setDataCreazione(referto.getDataCreazione());

        // 🔥 NUOVI CAMPI DEL REFERTO
        dto.setTitolo(referto.getTitolo());
        dto.setDescrizione(referto.getDescrizione());
        dto.setDiagnosi(referto.getDiagnosi());
        dto.setDataReferto(referto.getDataReferto());

        return dto;
    }

    private EsameDTO convertToDTO(Esame esame) {
        EsameDTO dto = new EsameDTO();

        dto.setId(esame.getId());
        dto.setIdPaziente(esame.getPaziente().getId());
        dto.setIdMedico(esame.getMedico().getId());

        dto.setNomeMedico(esame.getMedico().getNome());
        dto.setCognomeMedico(esame.getMedico().getCognome());
        dto.setSpecializzazioneMedico(esame.getMedico().getSpecializzazione());

        dto.setTipoEsame(esame.getTipoEsame());
        dto.setDataEsame(esame.getDataEsame());
        dto.setOraEsame(esame.getOraEsame());
        dto.setStato(esame.getStato().name());
        dto.setNote(esame.getNote());

        // 🔥 Esame ha referti? (1 → N)
        dto.setRefertoPresente(
                esame.getReferti() != null && !esame.getReferti().isEmpty()
        );

        return dto;
    }
    
    @Override
    public DisponibilitaEsameResponse calcolaProssimaDisponibilita(String tipoEsame) {

        // 1. Durata esame (in minuti)
        int durata = switch (tipoEsame.toUpperCase()) {
            case "ECG" -> 15;
            case "HOLTER" -> 30;
            case "ECOCARDIOGRAMMA" -> 40;
            default -> 20;
        };

        // 2. Orari disponibili (esempio: 9:00 - 18:00)
        LocalTime start = LocalTime.of(9, 0);
        LocalTime end = LocalTime.of(18, 0);

        // 3. Data di partenza = oggi
        LocalDate data = LocalDate.now();

        while (true) {
            LocalTime orario = start;

            while (!orario.plusMinutes(durata).isAfter(end)) {

                boolean occupato = esameRepository.existsByDataEsameAndOraEsame(
                        data,
                        orario                );

                if (!occupato) {
                    return new DisponibilitaEsameResponse(
                            data.toString(),
                            orario.toString()
                    );
                }

                orario = orario.plusMinutes(durata);
            }

            data = data.plusDays(1);
        }
    }
    
    @Override
    public List<EsameDTO> getEsamiDaRefertare(Long idMedico) {
        return esameRepository.findByMedicoIdAndStato(idMedico, Esame.StatoEsame.ESEGUITO)

                .stream()
                .map(this::convertToDTO)
                .toList();
    }


}
