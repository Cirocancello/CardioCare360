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

            if (file.isEmpty()) {
                throw new RuntimeException("Il file è vuoto");
            }

            if (!"application/pdf".equalsIgnoreCase(file.getContentType())) {
                throw new RuntimeException("Il file caricato non è un PDF valido");
            }

            // 1️⃣ Recupera entità
            Esame esame = esameRepository.findById(esameId)
                    .orElseThrow(() -> new RuntimeException("Esame non trovato"));

            Medico medico = medicoRepository.findById(medicoId)
                    .orElseThrow(() -> new RuntimeException("Medico non trovato"));

            // 2️⃣ Percorso sicuro
            String uploadDir = System.getProperty("user.dir") + "/uploads/referti/";
            Path uploadPath = Paths.get(uploadDir);
            Files.createDirectories(uploadPath);

            // 3️⃣ Salvataggio file
            String fileName = "referto_" + esameId + ".pdf";
            Path filePath = uploadPath.resolve(fileName);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("File salvato in: " + filePath.toAbsolutePath());

            // 4️⃣ Crea referto
            Referto referto = new Referto();
            referto.setEsame(esame);
            referto.setMedico(medico);
            referto.setPaziente(esame.getPaziente());
            referto.setTitolo("Referto Esame " + esame.getTipoEsame());
            referto.setDescrizione("Documento PDF del referto caricato dal medico");
            referto.setDiagnosi("In attesa di diagnosi");
            referto.setNoteMedico(noteMedico);
            referto.setFilePath(filePath.toString());
            referto.setDataCreazione(LocalDateTime.now());
            referto.setDataReferto(LocalDateTime.now());

            refertoRepository.save(referto);

            // 5️⃣ Aggiorna stato esame (CORRETTO)
            esame.setStato(Esame.StatoEsame.REFERTATO);
            esameRepository.save(esame);

            // 6️⃣ Ritorna DTO
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
            throw new RuntimeException("Referto non trovato per questo esame");
        }

        // Prende l’ultimo referto
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

        try {
            Path path = Paths.get(referto.getFilePath());
            System.out.println("Download path: " + path);
            System.out.println("File esiste? " + Files.exists(path));

            return Files.readAllBytes(path);

        } catch (IOException e) {
            throw new RuntimeException("Errore nel download del file PDF", e);
        }
    }

    // 🔍 PREVIEW PDF (usa SEMPRE l’ultimo referto)
    @Override
    public byte[] previewFile(Long esameId) {
        try {
            Referto referto = refertoRepository.findByEsame_Id(esameId)
                    .stream()
                    .reduce((first, second) -> second) // prende l’ultimo
                    .orElseThrow(() -> new RuntimeException("Referto non trovato per questo esame"));

            Path path = Paths.get(referto.getFilePath());
            System.out.println("Preview path: " + path);
            System.out.println("File esiste? " + Files.exists(path));

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
