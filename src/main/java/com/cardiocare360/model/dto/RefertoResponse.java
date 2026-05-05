package com.cardiocare360.model.dto;

import java.time.LocalDateTime;

public class RefertoResponse {

    private Long id;
    private Long pazienteId;
    private Long medicoId;
    private String titolo;
    private String descrizione;
    private String diagnosi;
    private String filePath;
    private LocalDateTime dataCreazione;
    private LocalDateTime dataReferto;

    // Getter e Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPazienteId() { return pazienteId; }
    public void setPazienteId(Long pazienteId) { this.pazienteId = pazienteId; }

    public Long getMedicoId() { return medicoId; }
    public void setMedicoId(Long medicoId) { this.medicoId = medicoId; }

    public String getTitolo() { return titolo; }
    public void setTitolo(String titolo) { this.titolo = titolo; }

    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

    public String getDiagnosi() { return diagnosi; }
    public void setDiagnosi(String diagnosi) { this.diagnosi = diagnosi; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public LocalDateTime getDataCreazione() { return dataCreazione; }
    public void setDataCreazione(LocalDateTime dataCreazione) { this.dataCreazione = dataCreazione; }

    public LocalDateTime getDataReferto() { return dataReferto; }
    public void setDataReferto(LocalDateTime dataReferto) { this.dataReferto = dataReferto; }
}
