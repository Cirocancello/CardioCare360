package com.cardiocare360.service.impl;

import com.cardiocare360.model.entity.Referto;
import com.cardiocare360.model.entity.Esame;
import com.cardiocare360.model.entity.Medico;
import com.cardiocare360.repository.RefertoRepository;
import com.cardiocare360.repository.EsameRepository;
import com.cardiocare360.repository.MedicoRepository;
import com.cardiocare360.service.RefertoService;
import com.cardiocare360.model.response.RefertoDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RefertoServiceImpl implements RefertoService {

    @Autowired
    private RefertoRepository refertoRepository;

    @Autowired
    private EsameRepository esameRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    // 🔥 UPLOAD REFERTO
    @Override
    public RefertoDTO uploadReferto(Long esameId, Long medicoId, String noteMedico, MultipartFile file) {
        try {
            System.out.println("=== UPLOAD REFERTO ===");

            // 🔹 Controllo file
            if (file != null && !file.isEmpty() &&
                    !"application/pdf".equalsIgnoreCase(file.getContentType())) {
                throw new RuntimeException("Il file caricato non è un PDF valido");
            }

            // 1️⃣ Recupera entità
            Esame esame = esameRepository.findById(esameId)
                    .orElseThrow(() -> new RuntimeException("Esame non trovato"));

            Medico medico = medicoRepository.findById(medicoId)
                    .orElseThrow(() -> new RuntimeException("Medico non trovato"));

            // 2️⃣ Percorso salvataggio
            String uploadDir = System.getProperty("user.dir") + "/uploads/referti/";
            Files.createDirectories(Paths.get(uploadDir));

            String filePathString = null;

            // 3️⃣ Salva file se presente
            if (file != null && !file.isEmpty()) {
                String fileName = "referto_" + esameId + ".pdf";
                Path filePath = Paths.get(uploadDir).resolve(fileName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                filePathString = filePath.toString();
            }

            // 4️⃣ Crea referto
            Referto referto = new Referto();
            referto.setEsame(esame);
            referto.setMedico(medico);
            referto.setPaziente(esame.getPaziente());
            referto.setTitolo("Referto Esame " + esame.getTipoEsame());
            referto.setDescrizione("Documento PDF del referto caricato dal medico");
            referto.setDiagnosi("In attesa di diagnosi");
            referto.setNoteMedico(noteMedico);
            referto.setFilePath(filePathString);
            referto.setDataCreazione(LocalDateTime.now());
            referto.setDataReferto(LocalDateTime.now());

            refertoRepository.save(referto);

            // 5️⃣ Aggiorna stato esame
            esame.setStato(Esame.StatoEsame.REFERTATO);
            esameRepository.save(esame);

            return new RefertoDTO(
                    referto.getId(),
                    esame.getId(),
                    medico.getId(),
                    referto.getTitolo(),
                    referto.getDescrizione(),
                    referto.getDiagnosi(),
                    referto.getNoteMedico(),
                    referto.getFilePath(),
                    referto.getDataCreazione()
            );

        } catch (Exception e) {
            throw new RuntimeException("Errore durante il salvataggio del referto: " + e.getMessage(), e);
        }
    }

    // 🔍 Recupera ultimo referto per esame
    @Override
    public RefertoDTO getRefertoByEsame(Long esameId) {
        List<Referto> referti = refertoRepository.findByEsame_Id(esameId);

        if (referti.isEmpty()) {
            return null; // NON lanciare eccezione
        }

        Referto referto = referti.get(referti.size() - 1);

        return new RefertoDTO(
                referto.getId(),
                referto.getEsame().getId(),
                referto.getMedico().getId(),
                referto.getTitolo(),
                referto.getDescrizione(),
                referto.getDiagnosi(),
                referto.getNoteMedico(),
                referto.getFilePath(),
                referto.getDataCreazione()
        );
    }

    // 🔥 DOWNLOAD PDF
    @Override
    public byte[] downloadFile(Long refertoId) {
        Referto referto = refertoRepository.findById(refertoId)
                .orElseThrow(() -> new RuntimeException("Referto non trovato"));

        if (referto.getFilePath() == null) {
            throw new RuntimeException("Il referto non contiene alcun file PDF");
        }

        try {
            Path path = Paths.get(referto.getFilePath());
            if (!Files.exists(path)) {
                throw new RuntimeException("File PDF non trovato sul server");
            }

            return Files.readAllBytes(path);

        } catch (IOException e) {
            throw new RuntimeException("Errore nel download del file PDF", e);
        }
    }

    // 🔍 PREVIEW PDF
    @Override
    public byte[] previewFile(Long esameId) {
        Referto referto = refertoRepository.findByEsame_Id(esameId)
                .stream()
                .reduce((first, second) -> second)
                .orElse(null);

        if (referto == null || referto.getFilePath() == null) {
            throw new RuntimeException("Nessun referto disponibile per questo esame");
        }

        try {
            Path path = Paths.get(referto.getFilePath());
            if (!Files.exists(path)) {
                throw new RuntimeException("File PDF non trovato sul server");
            }

            return Files.readAllBytes(path);

        } catch (IOException e) {
            throw new RuntimeException("Errore nella preview del file PDF", e);
        }
    }

    @Override
    public RefertoDTO generaPdfReferto(Long esameId) {
        throw new UnsupportedOperationException("generaPdfReferto non implementato");
    }
}
