package com.cardiocare360.model.response;

public class ParametroClinicoStoricoDTO {

    private Long id;
    private String tipo;
    private String nome;
    private String valore;
    private String unitaMisura;
    private String dataRilevazione;

    public ParametroClinicoStoricoDTO() {}

    public ParametroClinicoStoricoDTO(Long id, String tipo, String nome,
                                      String valore, String unitaMisura,
                                      String dataRilevazione) {
        this.id = id;
        this.tipo = tipo;
        this.nome = nome;
        this.valore = valore;
        this.unitaMisura = unitaMisura;
        this.dataRilevazione = dataRilevazione;
    }

    public Long getId() { return id; }
    public String getTipo() { return tipo; }
    public String getNome() { return nome; }
    public String getValore() { return valore; }
    public String getUnitaMisura() { return unitaMisura; }
    public String getDataRilevazione() { return dataRilevazione; }

    public void setId(Long id) { this.id = id; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setNome(String nome) { this.nome = nome; }
    public void setValore(String valore) { this.valore = valore; }
    public void setUnitaMisura(String unitaMisura) { this.unitaMisura = unitaMisura; }
    public void setDataRilevazione(String dataRilevazione) { this.dataRilevazione = dataRilevazione; }

    // ---------------------------------------------------------
    // VALIDAZIONI DI SICUREZZA
    // ---------------------------------------------------------
    public boolean isValid() {
        return id != null && id > 0 &&
               tipo != null && !tipo.isBlank() &&
               nome != null && !nome.isBlank() &&
               valore != null && !valore.isBlank() &&
               dataRilevazione != null && !dataRilevazione.isBlank();
    }

    public boolean hasUnitaMisura() {
        return unitaMisura != null && !unitaMisura.isBlank();
    }
}
