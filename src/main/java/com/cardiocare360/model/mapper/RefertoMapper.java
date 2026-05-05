package com.cardiocare360.model.mapper;

import com.cardiocare360.model.dto.RefertoRequest;
import com.cardiocare360.model.dto.RefertoResponse;
import com.cardiocare360.model.entity.Referto;
import com.cardiocare360.model.entity.Medico;
import com.cardiocare360.model.entity.Paziente;

public class RefertoMapper {

    // DTO → Entity
    public static Referto toEntity(RefertoRequest dto, Paziente paziente, Medico medico) {
        Referto referto = new Referto();
        referto.setPaziente(paziente);
        referto.setMedico(medico);
        referto.setTitolo(dto.getTitolo());
        referto.setDescrizione(dto.getDescrizione());
        referto.setDiagnosi(dto.getDiagnosi());
        return referto;
    }

    // Entity → DTO
    public static RefertoResponse toResponse(Referto ref) {
        RefertoResponse dto = new RefertoResponse();
        dto.setId(ref.getId());
        dto.setPazienteId(ref.getPaziente().getId());
        dto.setMedicoId(ref.getMedico().getId());
        dto.setTitolo(ref.getTitolo());
        dto.setDescrizione(ref.getDescrizione());
        dto.setDiagnosi(ref.getDiagnosi());
        dto.setFilePath(ref.getFilePath());
        dto.setDataCreazione(ref.getDataCreazione());
        dto.setDataReferto(ref.getDataReferto());
        return dto;
    }
}
