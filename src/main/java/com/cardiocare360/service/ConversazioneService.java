package com.cardiocare360.service;

import com.cardiocare360.model.entity.Conversazione;
import com.cardiocare360.model.entity.Medico;
import com.cardiocare360.model.entity.Paziente;
import com.cardiocare360.repository.ConversazioneRepository;
import com.cardiocare360.repository.MedicoRepository;
import com.cardiocare360.repository.PazienteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ConversazioneService {

    private final ConversazioneRepository conversazioneRepository;
    private final PazienteRepository pazienteRepository;
    private final MedicoRepository medicoRepository;

    public ConversazioneService(ConversazioneRepository conversazioneRepository,
                                PazienteRepository pazienteRepository,
                                MedicoRepository medicoRepository) {
        this.conversazioneRepository = conversazioneRepository;
        this.pazienteRepository = pazienteRepository;
        this.medicoRepository = medicoRepository;
    }

    // 🔹 Recupera o crea una conversazione tra paziente e medico
    public Conversazione getOrCreateConversazione(Long pazienteId, Long medicoId) {

        return conversazioneRepository
                .findByPazienteIdAndMedicoId(pazienteId, medicoId)
                .orElseGet(() -> {

                    Paziente paziente = pazienteRepository.findById(pazienteId)
                            .orElseThrow(() -> new RuntimeException("Paziente non trovato"));

                    Medico medico = medicoRepository.findById(medicoId)
                            .orElseThrow(() -> new RuntimeException("Medico non trovato"));

                    Conversazione nuova = new Conversazione();
                    nuova.setPaziente(paziente);
                    nuova.setMedico(medico);
                    nuova.setDataCreazione(LocalDateTime.now());
                    nuova.setUltimoAggiornamento(LocalDateTime.now());
                    nuova.setUltimoMessaggio(null);

                    return conversazioneRepository.save(nuova);
                });
    }

    // 🔹 Aggiorna ultimo messaggio e timestamp
    public void aggiornaUltimoMessaggio(Long conversazioneId, String testo) {
        Conversazione conv = conversazioneRepository.findById(conversazioneId)
                .orElseThrow(() -> new RuntimeException("Conversazione non trovata"));

        conv.setUltimoMessaggio(testo);
        conv.setUltimoAggiornamento(LocalDateTime.now());

        conversazioneRepository.save(conv);
    }

    // 🔹 Lista conversazioni del paziente
    public List<Conversazione> getConversazioniPaziente(Long pazienteId) {
        return conversazioneRepository
                .findByPazienteIdOrderByUltimoAggiornamentoDesc(pazienteId);
    }

    // 🔹 Lista conversazioni del medico
    public List<Conversazione> getConversazioniMedico(Long medicoId) {
        return conversazioneRepository
                .findByMedicoIdOrderByUltimoAggiornamentoDesc(medicoId);
    }

    // 🔹 Recupera conversazione specifica
    public Conversazione getConversazione(Long id) {
        return conversazioneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conversazione non trovata"));
    }
}
