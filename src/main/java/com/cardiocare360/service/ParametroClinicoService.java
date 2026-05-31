package com.cardiocare360.service;

import com.cardiocare360.model.request.ParametroClinicoRequest;

import com.cardiocare360.model.entity.ParametroClinico;
import com.cardiocare360.model.entity.Paziente;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
@Transactional
public class ParametroClinicoService {

    @PersistenceContext
    private EntityManager entityManager;

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


}
