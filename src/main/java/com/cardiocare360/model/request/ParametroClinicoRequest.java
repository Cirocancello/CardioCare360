package com.cardiocare360.model.request;

import java.time.LocalDateTime;

public class ParametroClinicoRequest {

    // Campo opzionale: se non arriva, il service lo deduce automaticamente
    private String tipo;

    // Valore generico (non usato per i parametri vitali classici)
    private String valore;

    // Campi specifici dei parametri vitali
    private Integer pressioneSistolica;
    private Integer pressioneDiastolica;
    private Integer battiti;
    private Integer glicemia;
    private Integer saturazione;
    private Double peso;
    private Double temperatura;

    // Data rilevazione (opzionale)
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
}
