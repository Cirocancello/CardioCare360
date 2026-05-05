package com.cardiocare360.service.impl;

import com.cardiocare360.model.entity.Medico;
import com.cardiocare360.model.entity.Paziente;
import com.cardiocare360.model.entity.Referto;
import com.cardiocare360.repository.MedicoRepository;
import com.cardiocare360.repository.PazienteRepository;
import com.cardiocare360.repository.RefertoRepository;
import com.cardiocare360.service.RefertoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Service
public class RefertoServiceImpl implements RefertoService {

    @Autowired
    private PazienteRepository pazienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private RefertoRepository refertoRepository;

    // Percorso reale e funzionante
    private final String BASE_PATH = "C:/cardiocare360/uploads/referti/";

    		

    @Override
    public Referto creaReferto(Long pazienteId,
                               Long medicoId,
                               String titolo,
                               String descrizione,
                               String diagnosi,
                               MultipartFile file) {

        Paziente paziente = pazienteRepository.findById(pazienteId)
                .orElseThrow(() -> new RuntimeException("Paziente non trovato"));

        Medico medico = medicoRepository.findById(medicoId)
                .orElseThrow(() -> new RuntimeException("Medico non trovato"));

        Referto referto = new Referto();
        referto.setPaziente(paziente);
        referto.setMedico(medico);
        referto.setTitolo(titolo);
        referto.setDescrizione(descrizione);
        referto.setDiagnosi(diagnosi);

        // Salvataggio file
        String filePath = salvaFile(file);
        referto.setFilePath(filePath);

        return refertoRepository.save(referto);
    }

    private String salvaFile(MultipartFile file) {
        try {
            File directory = new File(BASE_PATH);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String filePath = BASE_PATH + System.currentTimeMillis() + "_" + file.getOriginalFilename();
            File dest = new File(filePath);

            // DEBUG: percorso reale
            System.out.println("Salvataggio in: " + dest.getAbsolutePath());

            file.transferTo(dest);

            return filePath;

        } catch (IOException e) {
            e.printStackTrace(); // MOSTRA L'ERRORE REALE
            throw new RuntimeException("Errore durante il salvataggio del file: " + e.getMessage());
        }
    }


    @Override
    public Referto getRefertoById(Long id) {
        return refertoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Referto non trovato"));
    }

    @Override
    public List<Referto> getRefertiPaziente(Long pazienteId) {
        return refertoRepository.findByPaziente_Id(pazienteId);
    }

    @Override
    public List<Referto> getRefertiMedico(Long medicoId) {
        return refertoRepository.findByMedico_Id(medicoId);
    }

    @Override
    public byte[] downloadFile(Long refertoId) {
        Referto referto = getRefertoById(refertoId);

        try {
            return Files.readAllBytes(new File(referto.getFilePath()).toPath());
        } catch (IOException e) {
            throw new RuntimeException("Errore durante il download del file");
        }
    }
}
