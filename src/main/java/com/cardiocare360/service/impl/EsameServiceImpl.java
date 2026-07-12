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

    // ---------------------------------------------------------
    // CREA ESAME (blindato)
    // ---------------------------------------------------------
    @Override
    public EsameDTO creaEsame(EsameDTO dto) {

        // 🔒 Validazioni input
        if (dto.getTipoEsame() == null || dto.getTipoEsame().isBlank()) {
            throw new RuntimeException("Il tipo di esame è obbligatorio");
        }

        if (dto.getDataEsame() == null) {
            throw new RuntimeException("La data dell'esame è obbligatoria");
        }

        if (dto.getDataEsame().isBefore(LocalDate.now())) {
            throw new RuntimeException("La data dell'esame non può essere nel passato");
        }

        if (dto.getOraEsame() == null) {
            throw new RuntimeException("L'orario dell'esame è obbligatorio");
        }

        if (dto.getNote() != null && dto.getNote().length() > 2000) {
            throw new RuntimeException("Le note non possono superare 2000 caratteri");
        }

        // 🔒 Controllo slot occupato
        boolean slotOccupato = esameRepository.existsByDataEsameAndOraEsame(
                dto.getDataEsame(),
                dto.getOraEsame()
        );

        if (slotOccupato) {
            throw new RuntimeException("Lo slot selezionato è già occupato");
        }

        // 🔒 Paziente e medico devono esistere
        Paziente paziente = pazienteRepository.findById(dto.getIdPaziente())
                .orElseThrow(() -> new RuntimeException("Paziente non trovato"));

        Medico medico = medicoRepository.findById(dto.getIdMedico())
                .orElseThrow(() -> new RuntimeException("Medico non trovato"));

        // 🔒 Creazione esame
        Esame esame = new Esame();
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

    // ---------------------------------------------------------
    // LISTA ESAMI PAZIENTE
    // ---------------------------------------------------------
    @Override
    public List<EsameDTO> getEsamiPaziente(Long idPaziente) {
        return esameRepository.findByPazienteId(idPaziente)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // ---------------------------------------------------------
    // LISTA ESAMI MEDICO
    // ---------------------------------------------------------
    @Override
    public List<EsameDTO> getEsamiMedico(Long idMedico) {
        return esameRepository.findByMedicoId(idMedico)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // ---------------------------------------------------------
    // DETTAGLIO ESAME
    // ---------------------------------------------------------
    @Override
    public EsameDTO getEsameById(Long id) {
        Esame esame = esameRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Esame non trovato"));
        return convertToDTO(esame);
    }

    // ---------------------------------------------------------
    // AGGIORNA STATO (blindato)
    // ---------------------------------------------------------
    @Override
    public EsameDTO aggiornaStatoEsame(Long idEsame, String nuovoStato) {

        if (nuovoStato == null || nuovoStato.isBlank()) {
            throw new RuntimeException("Lo stato è obbligatorio");
        }

        Esame.StatoEsame stato;
        try {
            stato = Esame.StatoEsame.valueOf(nuovoStato);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Stato esame non valido");
        }

        Esame esame = esameRepository.findById(idEsame)
                .orElseThrow(() -> new RuntimeException("Esame non trovato"));

        esame.setStato(stato);
        esameRepository.save(esame);

        return convertToDTO(esame);
    }

    // ---------------------------------------------------------
    // ELIMINA ESAME (blindato)
    // ---------------------------------------------------------
    @Override
    public void eliminaEsame(Long idEsame) {

        Esame esame = esameRepository.findById(idEsame)
                .orElseThrow(() -> new RuntimeException("Esame non trovato"));

        // 🔒 Non eliminare esami refertati
        if (esame.getStato() == Esame.StatoEsame.REFERTATO) {
            throw new RuntimeException("Non è possibile eliminare un esame già refertato");
        }

        esameRepository.deleteById(idEsame);
    }

    // ---------------------------------------------------------
    // RECUPERA REFERTO (blindato)
    // ---------------------------------------------------------
    @Override
    public RefertoDTO getRefertoByEsame(Long idEsame) {

        List<Referto> referti = refertoRepository.findByEsame_Id(idEsame);

        if (referti.isEmpty()) {
            throw new RuntimeException("Referto non presente per questo esame");
        }

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

        dto.setTitolo(referto.getTitolo());
        dto.setDescrizione(referto.getDescrizione());
        dto.setDiagnosi(referto.getDiagnosi());
        dto.setDataReferto(referto.getDataReferto());

        return dto;
    }

    // ---------------------------------------------------------
    // CONVERSIONE DTO
    // ---------------------------------------------------------
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

        dto.setRefertoPresente(
                esame.getReferti() != null && !esame.getReferti().isEmpty()
        );

        return dto;
    }

    // ---------------------------------------------------------
    // CALCOLO PROSSIMA DISPONIBILITÀ
    // ---------------------------------------------------------
    @Override
    public DisponibilitaEsameResponse calcolaProssimaDisponibilita(String tipoEsame) {

        if (tipoEsame == null || tipoEsame.isBlank()) {
            throw new RuntimeException("Il tipo di esame è obbligatorio");
        }

        int durata = switch (tipoEsame.toUpperCase()) {
            case "ECG" -> 15;
            case "HOLTER" -> 30;
            case "ECOCARDIOGRAMMA" -> 40;
            default -> 20;
        };

        LocalTime start = LocalTime.of(9, 0);
        LocalTime end = LocalTime.of(18, 0);

        LocalDate data = LocalDate.now();

        while (true) {
            LocalTime orario = start;

            while (!orario.plusMinutes(durata).isAfter(end)) {

                boolean occupato = esameRepository.existsByDataEsameAndOraEsame(
                        data,
                        orario
                );

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

    // ---------------------------------------------------------
    // ESAMI DA REFERTARE
    // ---------------------------------------------------------
    @Override
    public List<EsameDTO> getEsamiDaRefertare(Long idMedico) {
        return esameRepository.findByMedicoIdAndStato(idMedico, Esame.StatoEsame.ESEGUITO)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }
}
