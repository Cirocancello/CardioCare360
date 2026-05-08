package com.cardiocare360.model.mapper;

import com.cardiocare360.model.entity.Notifica;
import com.cardiocare360.model.entity.Appuntamento;
import com.cardiocare360.model.entity.ParametroClinico;
import com.cardiocare360.model.entity.Utente;
import com.cardiocare360.model.response.NotificaDTO;

public class NotificaMapper {

    // -------------------------
    // ENTITY → DTO
    // -------------------------
    public static NotificaDTO toDTO(Notifica n) {
        if (n == null) return null;

        NotificaDTO dto = new NotificaDTO();
        dto.setId(n.getId());
        dto.setTitolo(n.getTitolo());
        dto.setMessaggio(n.getMessaggio());
        dto.setLetto(n.isLetto());
        dto.setDataCreazione(n.getDataCreazione());

        dto.setUtenteId(n.getUtente() != null ? n.getUtente().getId() : null);
        dto.setAppuntamentoId(n.getAppuntamento() != null ? n.getAppuntamento().getId() : null);
        dto.setParametroClinicoId(n.getParametroClinico() != null ? n.getParametroClinico().getId() : null);

        return dto;
    }

    // -------------------------
    // DTO → ENTITY (opzionale)
    // -------------------------
    public static Notifica toEntity(
            NotificaDTO dto,
            Utente utente,
            Appuntamento appuntamento,
            ParametroClinico parametroClinico
    ) {
        if (dto == null) return null;

        Notifica n = new Notifica();
        n.setTitolo(dto.getTitolo());
        n.setMessaggio(dto.getMessaggio());
        n.setLetto(dto.isLetto());
        n.setDataCreazione(dto.getDataCreazione());

        n.setUtente(utente);
        n.setAppuntamento(appuntamento);
        n.setParametroClinico(parametroClinico);

        return n;
    }
}
