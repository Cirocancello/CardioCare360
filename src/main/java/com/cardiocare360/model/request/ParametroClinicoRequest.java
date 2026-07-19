package com.cardiocare360.model.request;

import java.time.LocalDateTime;

public class ParametroClinicoRequest {

    // Tipo logico (es. "PRESSIONE", "GLICEMIA")
    private String tipo;

    // 🔥 Campi specifici dei parametri vitali
    private Double pressioneSistolica;
    private Double pressioneDiastolica;
    private Double battiti;
    private Double glicemia;
    private Double saturazione;
    private Double peso;
    private Double temperatura;

    // Data rilevazione (opzionale)
    private LocalDateTime dataRilevazione;

    public ParametroClinicoRequest() {}

    // GETTER & SETTER
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public Double getPressioneSistolica() { return pressioneSistolica; }
    public void setPressioneSistolica(Double pressioneSistolica) { this.pressioneSistolica = pressioneSistolica; }

    public Double getPressioneDiastolica() { return pressioneDiastolica; }
    public void setPressioneDiastolica(Double pressioneDiastolica) { this.pressioneDiastolica = pressioneDiastolica; }

    public Double getBattiti() { return battiti; }
    public void setBattiti(Double battiti) { this.battiti = battiti; }

    public Double getGlicemia() { return glicemia; }
    public void setGlicemia(Double glicemia) { this.glicemia = glicemia; }

    public Double getSaturazione() { return saturazione; }
    public void setSaturazione(Double saturazione) { this.saturazione = saturazione; }

    public Double getPeso() { return peso; }
    public void setPeso(Double peso) { this.peso = peso; }

    public Double getTemperatura() { return temperatura; }
    public void setTemperatura(Double temperatura) { this.temperatura = temperatura; }

    public LocalDateTime getDataRilevazione() { return dataRilevazione; }
    public void setDataRilevazione(LocalDateTime dataRilevazione) { this.dataRilevazione = dataRilevazione; }
}
