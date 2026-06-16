package com.cardiocare360.model.response;

public class ParametriRecentiDTO {

    private Long idPaziente;
    private String nome;
    private String cognome;

    private String dataUltimaRilevazione;

    private Integer pressioneSistolica;
    private Integer pressioneDiastolica;
    private Integer battiti;
    private Integer glicemia;
    private Integer saturazione;
    private Double temperatura;

    private String stato; // OK, WARNING, DANGER

    public ParametriRecentiDTO() {}

    public ParametriRecentiDTO(Long idPaziente, String nome, String cognome,
                                    String dataUltimaRilevazione,
                                    Integer pressioneSistolica, Integer pressioneDiastolica,
                                    Integer battiti, Integer glicemia, Integer saturazione,
                                    Double temperatura, String stato) {
        this.idPaziente = idPaziente;
        this.nome = nome;
        this.cognome = cognome;
        this.dataUltimaRilevazione = dataUltimaRilevazione;
        this.pressioneSistolica = pressioneSistolica;
        this.pressioneDiastolica = pressioneDiastolica;
        this.battiti = battiti;
        this.glicemia = glicemia;
        this.saturazione = saturazione;
        this.temperatura = temperatura;
        this.stato = stato;
    }

    public Long getIdPaziente() { return idPaziente; }
    public void setIdPaziente(Long idPaziente) { this.idPaziente = idPaziente; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCognome() { return cognome; }
    public void setCognome(String cognome) { this.cognome = cognome; }

    public String getDataUltimaRilevazione() { return dataUltimaRilevazione; }
    public void setDataUltimaRilevazione(String dataUltimaRilevazione) { this.dataUltimaRilevazione = dataUltimaRilevazione; }

    public Integer getPressioneSistolica() { return pressioneSistolica; }
    public void setPressioneSistolica(Integer pressioneSistolica) { this.pressioneSistolica = pressioneSistolica; }

    public Integer getPressioneDiastolica() { return pressioneDiastolica; }
    public void setPressioneDiastolica(Integer pressioneDiastolica) { this.pressioneDiastolica = pressioneDiastolica; }

    public Integer getBattiti() { return battiti; }
    public void setBattiti(Integer battiti) { this.battiti = battiti; }

    public Integer getGlicemia() { return glicemia; }
    public void setGlicemia(Integer glicemia) { this.glicemia = glicemia; }

    public Integer getSaturazione() { return saturazione; }
    public void setSaturazione(Integer saturazione) { this.saturazione = saturazione; }

    public Double getTemperatura() { return temperatura; }
    public void setTemperatura(Double temperatura) { this.temperatura = temperatura; }

    public String getStato() { return stato; }
    public void setStato(String stato) { this.stato = stato; }
}
