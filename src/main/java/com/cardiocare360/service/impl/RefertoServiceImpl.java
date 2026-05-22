package com.cardiocare360.service.impl;

import com.cardiocare360.model.entity.Esame;
import com.cardiocare360.model.entity.Medico;
import com.cardiocare360.model.entity.Referto;
import com.cardiocare360.model.response.RefertoDTO;
import com.cardiocare360.repository.EsameRepository;
import com.cardiocare360.repository.MedicoRepository;
import com.cardiocare360.repository.RefertoRepository;
import com.cardiocare360.service.RefertoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
public class RefertoServiceImpl implements RefertoService {

    @Autowired
    private RefertoRepository refertoRepository;

    @Autowired
    private EsameRepository esameRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    private final String BASE_PATH = "C:/cardiocare360/uploads/referti/";

    @Override
    public RefertoDTO uploadReferto(Long esameId,
                                    Long medicoId,
                                    String noteMedico,
                                    MultipartFile file) {

        Esame esame = esameRepository.findById(esameId)
                .orElseThrow(() -> new RuntimeException("Esame non trovato"));

        Medico medico = medicoRepository.findById(medicoId)
                .orElseThrow(() -> new RuntimeException("Medico non trovato"));

        // Se esiste già un referto per questo esame, lo aggiorniamo
        Referto referto = refertoRepository.findByEsame_Id(esameId);
        if (referto == null) {
            referto = new Referto();
            referto.setEsame(esame);
        }

        referto.setMedico(medico);
        referto.setNoteMedico(noteMedico);
        referto.setFilePath(salvaFile(file));

        // Aggiorna stato esame
        esame.setStato(Esame.StatoEsame.REFERTATO);
        esameRepository.save(esame);

        Referto saved = refertoRepository.save(referto);
        return convertToRefertoDTO(saved);
    }

    @Override
    public RefertoDTO getRefertoByEsame(Long esameId) {
        Referto referto = refertoRepository.findByEsame_Id(esameId);
        if (referto == null) throw new RuntimeException("Referto non trovato");
        return convertToRefertoDTO(referto);
    }

    @Override
    public byte[] downloadFile(Long refertoId) {
        Referto referto = refertoRepository.findById(refertoId)
                .orElseThrow(() -> new RuntimeException("Referto non trovato"));

        try {
            return Files.readAllBytes(new File(referto.getFilePath()).toPath());
        } catch (IOException e) {
            throw new RuntimeException("Errore durante il download del file");
        }
    }

    private String salvaFile(MultipartFile file) {
        try {
            File directory = new File(BASE_PATH);
            if (!directory.exists()) directory.mkdirs();

            String filePath = BASE_PATH + System.currentTimeMillis() + "_" + file.getOriginalFilename();
            file.transferTo(new File(filePath));
            return filePath;

        } catch (IOException e) {
            throw new RuntimeException("Errore durante il salvataggio del file: " + e.getMessage());
        }
    }

    private RefertoDTO convertToRefertoDTO(Referto referto) {
        RefertoDTO dto = new RefertoDTO();
        dto.setId(referto.getId());
        dto.setEsameId(referto.getEsame().getId());
        dto.setMedicoId(referto.getMedico().getId());
        dto.setNomeMedico(referto.getMedico().getNome());
        dto.setCognomeMedico(referto.getMedico().getCognome());
        dto.setNoteMedico(referto.getNoteMedico());
        dto.setFilePath(referto.getFilePath());
        dto.setDataCreazione(referto.getDataCreazione()); // 🔥 CORRETTO
        return dto;
    }
}
