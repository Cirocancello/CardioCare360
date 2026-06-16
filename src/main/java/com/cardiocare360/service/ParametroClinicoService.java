package com.cardiocare360.service;

import com.cardiocare360.model.entity.ParametroClinico;
import com.cardiocare360.model.entity.Paziente;
import com.cardiocare360.model.request.ParametroClinicoRequest;
import com.cardiocare360.model.response.ParametriRecentiDTO;
import com.cardiocare360.model.response.ParametroClinicoStoricoDTO;
import com.cardiocare360.repository.ParametroClinicoRepository;
import com.cardiocare360.repository.PazienteRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ParametroClinicoService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ParametroClinicoRepository parametroRepo;

    @Autowired
    private PazienteRepository pazienteRepo;

    // ============================================================
    // 1) INSERIMENTO PARAMETRI (PAZIENTE)
    // ============================================================
    public List<ParametroClinico> inserisciParametro(Long pazienteId, ParametroClinicoRequest request) {

        Paziente paziente = entityManager.find(Paziente.class, pazienteId);
        if (paziente == null) {
            throw new RuntimeException("Paziente non trovato");
        }

        List<ParametroClinico> parametri = new ArrayList<>();

        LocalDateTime data = request.getDataRilevazione() != null
                ? request.getDataRilevazione()
                : LocalDateTime.now();

        // PRESSIONE
        if (request.getPressioneSistolica() != null && request.getPressioneDiastolica() != null) {
            ParametroClinico pressione = new ParametroClinico();
            pressione.setPaziente(paziente);
            pressione.setTipo("PRESSIONE");
            pressione.setNome("Pressione arteriosa");
            pressione.setValore(request.getPressioneSistolica() + "/" + request.getPressioneDiastolica());
            pressione.setUnitaMisura("mmHg");
            pressione.setDataRilevazione(data);
            parametri.add(pressione);
        }

        // BATTITI
        if (request.getBattiti() != null) {
            ParametroClinico battiti = new ParametroClinico();
            battiti.setPaziente(paziente);
            battiti.setTipo("BATTITI");
            battiti.setNome("Frequenza cardiaca");
            battiti.setValore(String.valueOf(request.getBattiti()));
            battiti.setUnitaMisura("bpm");
            battiti.setDataRilevazione(data);
            parametri.add(battiti);
        }

        // GLICEMIA
        if (request.getGlicemia() != null) {
            ParametroClinico glicemia = new ParametroClinico();
            glicemia.setPaziente(paziente);
            glicemia.setTipo("GLICEMIA");
            glicemia.setNome("Glicemia");
            glicemia.setValore(String.valueOf(request.getGlicemia()));
            glicemia.setUnitaMisura("mg/dL");
            glicemia.setDataRilevazione(data);
            parametri.add(glicemia);
        }

        // SATURAZIONE
        if (request.getSaturazione() != null) {
            ParametroClinico saturazione = new ParametroClinico();
            saturazione.setPaziente(paziente);
            saturazione.setTipo("SATURAZIONE");
            saturazione.setNome("Saturazione ossigeno");
            saturazione.setValore(String.valueOf(request.getSaturazione()));
            saturazione.setUnitaMisura("%");
            saturazione.setDataRilevazione(data);
            parametri.add(saturazione);
        }

        // PESO
        if (request.getPeso() != null) {
            ParametroClinico peso = new ParametroClinico();
            peso.setPaziente(paziente);
            peso.setTipo("PESO");
            peso.setNome("Peso corporeo");
            peso.setValore(String.valueOf(request.getPeso()));
            peso.setUnitaMisura("kg");
            peso.setDataRilevazione(data);
            parametri.add(peso);
        }

        // TEMPERATURA
        if (request.getTemperatura() != null) {
            ParametroClinico temperatura = new ParametroClinico();
            temperatura.setPaziente(paziente);
            temperatura.setTipo("TEMPERATURA");
            temperatura.setNome("Temperatura corporea");
            temperatura.setValore(String.valueOf(request.getTemperatura()));
            temperatura.setUnitaMisura("°C");
            temperatura.setDataRilevazione(data);
            parametri.add(temperatura);
        }

        // Salva tutti i parametri
        for (ParametroClinico p : parametri) {
            entityManager.persist(p);
        }

        return parametri;
    }

    // ============================================================
    // 2) PARAMETRI RECENTI (MEDICO)
    // ============================================================
    public List<ParametriRecentiDTO> getParametriRecentiByMedico(Long idMedico) {

        List<Paziente> pazienti = pazienteRepo.findByMedico_Id(idMedico);
        List<ParametriRecentiDTO> lista = new ArrayList<>();

        for (Paziente p : pazienti) {

            ParametroClinico ultimo = parametroRepo
                    .findTopByPazienteIdOrderByDataRilevazioneDesc(p.getId());

            if (ultimo == null) {
                lista.add(new ParametriRecentiDTO(
                        p.getId(),
                        p.getNome(),
                        p.getCognome(),
                        null,
                        null, null, null, null, null, null,
                        "NODATA"
                ));
                continue;
            }

            Integer sistolica = null;
            Integer diastolica = null;
            Integer battiti = null;
            Integer glicemia = null;
            Integer saturazione = null;
            Double temperatura = null;

            switch (ultimo.getTipo()) {
                case "PRESSIONE" -> {
                    String[] parts = ultimo.getValore().split("/");
                    sistolica = Integer.parseInt(parts[0]);
                    diastolica = Integer.parseInt(parts[1]);
                }
                case "BATTITI" -> battiti = Integer.parseInt(ultimo.getValore());
                case "GLICEMIA" -> glicemia = Integer.parseInt(ultimo.getValore());
                case "SATURAZIONE" -> saturazione = Integer.parseInt(ultimo.getValore());
                case "TEMPERATURA" -> temperatura = Double.parseDouble(ultimo.getValore());
            }

            String stato = calcolaStato(sistolica, diastolica, battiti, glicemia, saturazione, temperatura);

            lista.add(new ParametriRecentiDTO(
                    p.getId(),
                    p.getNome(),
                    p.getCognome(),
                    ultimo.getDataRilevazione().toString(),
                    sistolica,
                    diastolica,
                    battiti,
                    glicemia,
                    saturazione,
                    temperatura,
                    stato
            ));
        }

        return lista;
    }

    // ============================================================
    // 3) CALCOLO STATO CLINICO
    // ============================================================
    private String calcolaStato(Integer sist, Integer dias, Integer battiti,
                                Integer glicemia, Integer saturazione, Double temperatura) {

        if (sist != null && sist > 160) return "DANGER";
        if (dias != null && dias > 100) return "DANGER";
        if (battiti != null && battiti > 120) return "DANGER";
        if (glicemia != null && glicemia > 200) return "DANGER";
        if (saturazione != null && saturazione < 90) return "DANGER";
        if (temperatura != null && temperatura > 39) return "DANGER";

        return "OK";
    }

    // ============================================================
    // 4) STORICO PARAMETRI (MEDICO) — VERSIONE DTO
    // ============================================================
    public List<ParametroClinicoStoricoDTO> getStoricoParametriByPaziente(Long idPaziente) {

        List<ParametroClinico> lista = parametroRepo
                .findByPazienteIdOrderByDataRilevazioneDesc(idPaziente);

        List<ParametroClinicoStoricoDTO> dtoList = new ArrayList<>();

        for (ParametroClinico p : lista) {
            dtoList.add(new ParametroClinicoStoricoDTO(
                    p.getId(),
                    p.getTipo(),
                    p.getNome(),
                    p.getValore(),
                    p.getUnitaMisura(),
                    p.getDataRilevazione().toString()
            ));
        }

        return dtoList;
    }
}
