package com.cardiocare360.repository;

import com.cardiocare360.model.entity.Parametro;
import com.cardiocare360.model.entity.ParametroClinico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParametroClinicoRepository extends JpaRepository<ParametroClinico, Long> {

    // 🔹 Storico parametri del paziente (ordinati per data decrescente)
    List<ParametroClinico> findByPazienteIdOrderByDataRilevazioneDesc(Long pazienteId);

    // 🔹 Tutti i parametri del paziente (senza ordinamento)
    List<ParametroClinico> findByPazienteId(Long pazienteId);

    // 🔹 Ultimo parametro registrato (per dashboard medico)
    ParametroClinico findTopByPazienteIdOrderByDataRilevazioneDesc(Long pazienteId);

    public interface ParametroRepository extends JpaRepository<Parametro, Integer> {
        Parametro findByTipo(String tipo);
    }

   
    // 🔹 (Opzionale) Se vuoi filtrare anche per medico:
    // List<ParametroClinico> findByPazienteIdAndMedicoIdOrderByDataRilevazioneDesc(Long pazienteId, Long medicoId);
}
