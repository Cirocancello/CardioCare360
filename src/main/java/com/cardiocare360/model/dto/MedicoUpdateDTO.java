package com.cardiocare360.model.dto;

public class MedicoUpdateDTO {

    private String nome;
    private String cognome;
    private String specializzazione;
    private String numeroLicenza;

    public MedicoUpdateDTO() {}

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
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
