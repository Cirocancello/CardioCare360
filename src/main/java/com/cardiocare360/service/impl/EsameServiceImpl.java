package com.cardiocare360.service.impl;

import com.cardiocare360.model.entity.Esame;
import com.cardiocare360.model.entity.Medico;
import com.cardiocare360.model.entity.Paziente;
import com.cardiocare360.model.response.EsameDTO;
import com.cardiocare360.repository.EsameRepository;
import com.cardiocare360.repository.MedicoRepository;
import com.cardiocare360.repository.PazienteRepository;
import com.cardiocare360.service.EsameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EsameServiceImpl implements EsameService {

    @Autowired
    private EsameRepository esameRepository;

    @Autowired
    private PazienteRepository pazienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

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

        dto.setRefertoPresente(esame.getReferto() != null);

        return dto;
    }
}
