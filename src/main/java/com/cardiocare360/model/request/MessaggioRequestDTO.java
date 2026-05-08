package com.cardiocare360.model.request;

public class MessaggioRequestDTO {

    private Long mittenteId;
    private Long destinatarioId;
    private Long appuntamentoId; // opzionale
    private String contenuto;

    public Long getMittenteId() {
        return mittenteId;
    }

    public void setMittenteId(Long mittenteId) {
        this.mittenteId = mittenteId;
    }

    public Long getDestinatarioId() {
        return destinatarioId;
    }

    public void setDestinatarioId(Long destinatarioId) {
        this.destinatarioId = destinatarioId;
    }

    public Long getAppuntamentoId() {
        return appuntamentoId;
    }

    public void setAppuntamentoId(Long appuntamentoId) {
        this.appuntamentoId = appuntamentoId;
    }

    public String getContenuto() {
        return contenuto;
    }

    public void setContenuto(String contenuto) {
        this.contenuto = contenuto;
    }
}
