package com.cardiocare360.service;

import com.cardiocare360.model.entity.Parametro;
import com.cardiocare360.model.entity.ParametroClinico;
import com.cardiocare360.model.entity.Paziente;
import com.cardiocare360.model.entity.SogliaParametro;
import com.cardiocare360.model.request.ParametroClinicoRequest;
import com.cardiocare360.repository.ParametroClinicoRepository;
import com.cardiocare360.repository.ParametroRepository;
import com.cardiocare360.repository.PazienteRepository;
import com.cardiocare360.repository.SogliaParametroRepository;

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

    @Autowired
    private SogliaParametroRepository sogliaParametroRepository;

    @Autowired
    private ParametroRepository parametroRepository;

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

     // PRESSIONE SISTOLICA
        if (request.getPressioneSistolica() != null) {
            ParametroClinico sis = new ParametroClinico();
            sis.setPaziente(paziente);
            sis.setTipo("PRESSIONE_SIS");
            sis.setNome("Pressione sistolica");
            sis.setPressioneSistolica(request.getPressioneSistolica());
            sis.setUnitaMisura("mmHg");
            sis.setDataRilevazione(data);
            parametri.add(sis);
        }

        // PRESSIONE DIASTOLICA
        if (request.getPressioneDiastolica() != null) {
            ParametroClinico dia = new ParametroClinico();
            dia.setPaziente(paziente);
            dia.setTipo("PRESSIONE_DIA");
            dia.setNome("Pressione diastolica");
            dia.setPressioneDiastolica(request.getPressioneDiastolica());
            dia.setUnitaMisura("mmHg");
            dia.setDataRilevazione(data);
            parametri.add(dia);
        }


        // BATTITI
        if (request.getBattiti() != null) {
            ParametroClinico battiti = new ParametroClinico();
            battiti.setPaziente(paziente);
            battiti.setTipo("BATTITI");
            battiti.setNome("Frequenza cardiaca");
            battiti.setBattiti(request.getBattiti().doubleValue());
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
            glicemia.setGlicemia(request.getGlicemia().doubleValue());
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
            saturazione.setSaturazione(request.getSaturazione().doubleValue());
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
            peso.setPeso(request.getPeso());
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
            temperatura.setTemperatura(request.getTemperatura());
            temperatura.setUnitaMisura("°C");
            temperatura.setDataRilevazione(data);
            parametri.add(temperatura);
        }

        // Salva tutti i parametri + calcolo alert
        for (ParametroClinico p : parametri) {
            entityManager.persist(p);

            String alert = checkParametroFuoriSoglia(p);
            p.setAlert(alert);
        }

        return parametri;
    }

    // ============================================================
    // 6) CHECK SOGLIA (VERSIONE CORRETTA)
    // ============================================================
    public String checkParametroFuoriSoglia(ParametroClinico parametroClinico) {

        String tipo = parametroClinico.getTipo();

        Parametro parametro = parametroRepository.findByTipo(tipo);
        if (parametro == null) {
            return null;
        }

        SogliaParametro soglia = sogliaParametroRepository.findByPazienteIdAndParametroId(
                parametroClinico.getPaziente().getId(),
                parametro.getId()
        );

        if (soglia == null) {
            return null;
        }

        Double valore = null;

        switch (tipo) {

            case "PRESSIONE_SIS" -> {
                valore = parametroClinico.getPressioneSistolica();
                if (valore < soglia.getValoreMin() || valore > soglia.getValoreMax()) {
                    return "Pressione sistolica fuori soglia";
                }
                return null;
            }

            case "PRESSIONE_DIA" -> {
                valore = parametroClinico.getPressioneDiastolica();
                if (valore < soglia.getValoreMin() || valore > soglia.getValoreMax()) {
                    return "Pressione diastolica fuori soglia";
                }
                return null;
            }

            case "BATTITI" -> valore = parametroClinico.getBattiti();
            case "GLICEMIA" -> valore = parametroClinico.getGlicemia();
            case "SATURAZIONE" -> valore = parametroClinico.getSaturazione();
            case "PESO" -> valore = parametroClinico.getPeso();
            case "TEMPERATURA" -> valore = parametroClinico.getTemperatura();
        }

        if (valore == null) return null;

        // Messaggi personalizzati per ogni parametro
        if (valore < soglia.getValoreMin() || valore > soglia.getValoreMax()) {

            return switch (tipo) {
                case "BATTITI" -> "Battiti fuori soglia";
                case "GLICEMIA" -> "Glicemia fuori soglia";
                case "SATURAZIONE" -> "Saturazione fuori soglia";
                case "PESO" -> "Peso fuori soglia";
                case "TEMPERATURA" -> "Temperatura fuori soglia";
                default -> "Parametro fuori soglia";
            };
        }

        return null;
    }


}
