package com.cardiocare360.model.response;

import java.time.LocalDateTime;

public class MessaggioDTO {

    private Long id;
    private Long mittenteId;
    private Long destinatarioId;
    private Long appuntamentoId; // opzionale
    private String contenuto;
    private LocalDateTime dataInvio;
    private boolean letto;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getMittenteId() { return mittenteId; }
    public void setMittenteId(Long mittenteId) { this.mittenteId = mittenteId; }

    public Long getDestinatarioId() { return destinatarioId; }
    public void setDestinatarioId(Long destinatarioId) { this.destinatarioId = destinatarioId; }

    public Long getAppuntamentoId() { return appuntamentoId; }
    public void setAppuntamentoId(Long appuntamentoId) { this.appuntamentoId = appuntamentoId; }

    public String getContenuto() { return contenuto; }
    public void setContenuto(String contenuto) { this.contenuto = contenuto; }

    public LocalDateTime getDataInvio() { return dataInvio; }
    public void setDataInvio(LocalDateTime dataInvio) { this.dataInvio = dataInvio; }

    public boolean isLetto() { return letto; }
    public void setLetto(boolean letto) { this.letto = letto; }

    // ---------------------------------------------------------
    // VALIDAZIONI DI SICUREZZA
    // ---------------------------------------------------------

    public boolean isValid() {
        return id != null && id > 0 &&
               mittenteId != null && mittenteId > 0 &&
               destinatarioId != null && destinatarioId > 0 &&
               contenuto != null && !contenuto.isBlank() &&
               dataInvio != null;
    }

    public boolean hasAppuntamento() {
        return appuntamentoId != null && appuntamentoId > 0;
    }
}
