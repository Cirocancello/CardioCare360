package com.cardiocare360.model.response;

import java.time.LocalDateTime;

public class NotificaDTO {

    private Long id;
    private String titolo;
    private String messaggio;
    private boolean letto;
    private LocalDateTime dataCreazione;

    private Long utenteId;
    private Long appuntamentoId;
    private Long parametroClinicoId;

    public NotificaDTO() {}

    // GETTER & SETTER
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitolo() { return titolo; }
    public void setTitolo(String titolo) { this.titolo = titolo; }

    public String getMessaggio() { return messaggio; }
    public void setMessaggio(String messaggio) { this.messaggio = messaggio; }

    public boolean isLetto() { return letto; }
    public void setLetto(boolean letto) { this.letto = letto; }

    public LocalDateTime getDataCreazione() { return dataCreazione; }
    public void setDataCreazione(LocalDateTime dataCreazione) { this.dataCreazione = dataCreazione; }

    public Long getUtenteId() { return utenteId; }
    public void setUtenteId(Long utenteId) { this.utenteId = utenteId; }

    public Long getAppuntamentoId() { return appuntamentoId; }
    public void setAppuntamentoId(Long appuntamentoId) { this.appuntamentoId = appuntamentoId; }

    public Long getParametroClinicoId() { return parametroClinicoId; }
    public void setParametroClinicoId(Long parametroClinicoId) { this.parametroClinicoId = parametroClinicoId; }

    // ---------------------------------------------------------
    // VALIDAZIONI DI SICUREZZA
    // ---------------------------------------------------------

    public boolean isValid() {
        return id != null && id > 0 &&
               utenteId != null && utenteId > 0 &&
               titolo != null && !titolo.isBlank() &&
               messaggio != null && !messaggio.isBlank() &&
               dataCreazione != null;
    }

    public boolean hasAppuntamento() {
        return appuntamentoId != null && appuntamentoId > 0;
    }

    public boolean hasParametroClinico() {
        return parametroClinicoId != null && parametroClinicoId > 0;
    }
}
