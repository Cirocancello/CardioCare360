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
    private LocalDateTime dataCreazione; 
    private String titolo;
    private String descrizione;
    private String diagnosi;
    private LocalDateTime dataReferto;

    // ✅ COSTRUTTORE VUOTO NECESSARIO
    public RefertoDTO() {}

    // 🔥 COSTRUTTORE OPZIONALE (puoi tenerlo)
    public RefertoDTO(Long id, Long esameId, Long medicoId,
                      String titolo, String descrizione, String diagnosi,
                      String noteMedico, String filePath,
                      LocalDateTime dataCreazione) {

        this.id = id;
        this.esameId = esameId;
        this.medicoId = medicoId;
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.diagnosi = diagnosi;
        this.noteMedico = noteMedico;
        this.filePath = filePath;
        this.dataCreazione = dataCreazione;
    }

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

    public String getTitolo() { return titolo; }
    public void setTitolo(String titolo) { this.titolo = titolo; }

    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

    public String getDiagnosi() { return diagnosi; }
    public void setDiagnosi(String diagnosi) { this.diagnosi = diagnosi; }

    public LocalDateTime getDataReferto() { return dataReferto; }
    public void setDataReferto(LocalDateTime dataReferto) { this.dataReferto = dataReferto; }
}
