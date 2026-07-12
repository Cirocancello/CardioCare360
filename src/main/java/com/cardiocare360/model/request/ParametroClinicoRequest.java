package com.cardiocare360.model.request;

import java.time.LocalDateTime;

public class ParametroClinicoRequest {

    private String tipo;
    private String valore;

    private Integer pressioneSistolica;
    private Integer pressioneDiastolica;
    private Integer battiti;
    private Integer glicemia;
    private Integer saturazione;
    private Double peso;
    private Double temperatura;

    private LocalDateTime dataRilevazione;

    // GETTER & SETTER
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getValore() {
        return valore;
    }

    public void setValore(String valore) {
        this.valore = valore;
    }

    public Integer getPressioneSistolica() {
        return pressioneSistolica;
    }

    public void setPressioneSistolica(Integer pressioneSistolica) {
        this.pressioneSistolica = pressioneSistolica;
    }

    public Integer getPressioneDiastolica() {
        return pressioneDiastolica;
    }

    public void setPressioneDiastolica(Integer pressioneDiastolica) {
        this.pressioneDiastolica = pressioneDiastolica;
    }

    public Integer getBattiti() {
        return battiti;
    }

    public void setBattiti(Integer battiti) {
        this.battiti = battiti;
    }

    public Integer getGlicemia() {
        return glicemia;
    }

    public void setGlicemia(Integer glicemia) {
        this.glicemia = glicemia;
    }

    public Integer getSaturazione() {
        return saturazione;
    }

    public void setSaturazione(Integer saturazione) {
        this.saturazione = saturazione;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public Double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(Double temperatura) {
        this.temperatura = temperatura;
    }

    public LocalDateTime getDataRilevazione() {
        return dataRilevazione;
    }

    public void setDataRilevazione(LocalDateTime dataRilevazione) {
        this.dataRilevazione = dataRilevazione;
    }

    // ---------------------------------------------------------
    // VALIDAZIONI DI SICUREZZA
    // ---------------------------------------------------------

    public boolean isValid() {
        return hasAtLeastOneParameter() && isDataRilevazioneValida();
    }

    public boolean hasAtLeastOneParameter() {
        return pressioneSistolica != null ||
               pressioneDiastolica != null ||
               battiti != null ||
               glicemia != null ||
               saturazione != null ||
               peso != null ||
               temperatura != null ||
               (valore != null && !valore.isBlank());
    }

    public boolean isDataRilevazioneValida() {
        return dataRilevazione != null;
    }

    public boolean isTipoValido() {
        return tipo == null || !tipo.isBlank();
    }
}
