package com.cardiocare360.model.dto;

public class MedicoDTO {

    private Long id;
    private String nomeCompleto;
    private String email;
    private String specializzazione;
    private String numeroLicenza;

    public MedicoDTO() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSpecializzazione() {
        return specializzazione;
    }

    public void setSpecializzazione(String specializzazione) {
        this.specializzazione = specializzazione;
    }

    public String getNumeroLicenza() {
        return numeroLicenza;
    }

    public void setNumeroLicenza(String numeroLicenza) {
        this.numeroLicenza = numeroLicenza;
    }
}
