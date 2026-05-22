package com.cardiocare360.model.response;

import java.time.LocalDateTime;

public class RefertoDTO {

    private Long id;
    private Long esameId;

    private Long medicoId;
    private String nomeMedico;
    private String cognomeMedico;

    private String noteMedico;

    private String filePath;
    private LocalDateTime dataCreazione; // 🔥 CORRETTO

    // Getter e Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEsameId() { return esameId; }
    public void setEsameId(Long esameId) { this.esameId = esameId; }

    public Long getMedicoId() { return medicoId; }
    public void setMedicoId(Long medicoId) { this.medicoId = medicoId; }

    public String getNomeMedico() { return nomeMedico; }
    public void setNomeMedico(String nomeMedico) { this.nomeMedico = nomeMedico; }

    public String getCognomeMedico() { return cognomeMedico; }
    public void setCognomeMedico(String cognomeMedico) { this.cognomeMedico = cognomeMedico; }

    public String getNoteMedico() { return noteMedico; }
    public void setNoteMedico(String noteMedico) { this.noteMedico = noteMedico; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public LocalDateTime getDataCreazione() { return dataCreazione; }
    public void setDataCreazione(LocalDateTime dataCreazione) { this.dataCreazione = dataCreazione; }
}
