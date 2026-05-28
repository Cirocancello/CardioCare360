package com.cardiocare360.model.request;

import java.time.LocalDateTime;

public class ParametroClinicoRequest {

    private String tipo;
    private String valore;
    private LocalDateTime dataRilevazione;

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getValore() { return valore; }
    public void setValore(String valore) { this.valore = valore; }

    public LocalDateTime getDataRilevazione() { return dataRilevazione; }
    public void setDataRilevazione(LocalDateTime dataRilevazione) { this.dataRilevazione = dataRilevazione; }
}
