package com.cardiocare360.model.request;

public class NotificaRequestDTO {

    private Long utenteId;            // ID dell'utente destinatario
    private Long appuntamentoId;      // opzionale
    private Long parametroClinicoId;  // opzionale

    private String titolo;
    private String messaggio;
    private boolean letto;

    public NotificaRequestDTO() {}

    // GETTER & SETTER

    public Long getUtenteId() {
        return utenteId;
    }

    public void setUtenteId(Long utenteId) {
        this.utenteId = utenteId;
    }

    public Long getAppuntamentoId() {
        return appuntamentoId;
    }

    public void setAppuntamentoId(Long appuntamentoId) {
        this.appuntamentoId = appuntamentoId;
    }

    public Long getParametroClinicoId() {
        return parametroClinicoId;
    }

    public void setParametroClinicoId(Long parametroClinicoId) {
        this.parametroClinicoId = parametroClinicoId;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getMessaggio() {
        return messaggio;
    }

    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }

    public boolean isLetto() {
        return letto;
    }

    public void setLetto(boolean letto) {
        this.letto = letto;
    }

    // ---------------------------------------------------------
    // VALIDAZIONI DI SICUREZZA
    // ---------------------------------------------------------

    public boolean isValid() {
        return isUtenteValido()
                && isTitoloValido()
                && isMessaggioValido();
    }

    public boolean isUtenteValido() {
        return utenteId != null && utenteId > 0;
    }

    public boolean isTitoloValido() {
        return titolo != null && !titolo.isBlank();
    }

    public boolean isMessaggioValido() {
        return messaggio != null && !messaggio.isBlank();
    }

    public boolean isAppuntamentoValido() {
        return appuntamentoId == null || appuntamentoId > 0;
    }

    public boolean isParametroClinicoValido() {
        return parametroClinicoId == null || parametroClinicoId > 0;
    }
}
