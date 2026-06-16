package com.cardiocare360.repository;

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

    // ❌ RIMOSSO: metodo sbagliato, nome campo errato e firma non valida
    // List<ParametroClinico> findByPazienteIdOrderByDataRegistrazioneDesc(Long idPaziente, Long idMedico);

    // 🔹 (Opzionale) Se vuoi filtrare anche per medico:
    // List<ParametroClinico> findByPazienteIdAndMedicoIdOrderByDataRilevazioneDesc(Long pazienteId, Long medicoId);
}
