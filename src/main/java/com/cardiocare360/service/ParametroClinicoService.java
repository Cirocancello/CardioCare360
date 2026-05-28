package com.cardiocare360.service;

import com.cardiocare360.model.request.ParametroClinicoRequest;
import com.cardiocare360.model.entity.ParametroClinico;
import com.cardiocare360.model.entity.Paziente;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
@Transactional
public class ParametroClinicoService {

    @PersistenceContext
    private EntityManager entityManager;

    public ParametroClinico inserisciParametro(Long pazienteId, ParametroClinicoRequest request) {

        Paziente paziente = entityManager.find(Paziente.class, pazienteId);
        if (paziente == null) {
            throw new RuntimeException("Paziente non trovato");
        }

        ParametroClinico parametro = new ParametroClinico();
        parametro.setPaziente(paziente);
        parametro.setTipo(request.getTipo());
        parametro.setValore(request.getValore());
        parametro.setDataRilevazione(
                request.getDataRilevazione() != null ? request.getDataRilevazione() : java.time.LocalDateTime.now()
        );

        // ⭐ Valorizzazione automatica dei campi aggiuntivi
        switch (request.getTipo().toUpperCase()) {
            case "PRESSIONE" -> {
                parametro.setNome("Pressione arteriosa");
                parametro.setUnitaMisura("mmHg");
            }
            case "GLICEMIA" -> {
                parametro.setNome("Glicemia");
                parametro.setUnitaMisura("mg/dL");
            }
            case "TEMPERATURA" -> {
                parametro.setNome("Temperatura corporea");
                parametro.setUnitaMisura("°C");
            }
            case "SATURAZIONE" -> {
                parametro.setNome("Saturazione ossigeno");
                parametro.setUnitaMisura("%");
            }
            default -> {
                parametro.setNome(request.getTipo());
                parametro.setUnitaMisura(null);
            }
        }

        return entityManager.merge(parametro);
    }
}
