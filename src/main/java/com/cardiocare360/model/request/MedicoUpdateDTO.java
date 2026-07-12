package com.cardiocare360.model.request;

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

    // ---------------------------------------------------------
    // VALIDAZIONI DI SICUREZZA
    // ---------------------------------------------------------

    public boolean isValid() {
        return isSpecializzazioneValida() && isNumeroLicenzaValida();
    }

    public boolean isNomeValido() {
        return nome != null && !nome.isBlank();
    }

    public boolean isCognomeValido() {
        return cognome != null && !cognome.isBlank();
    }

    public boolean isSpecializzazioneValida() {
        return specializzazione != null && !specializzazione.isBlank();
    }

    public boolean isNumeroLicenzaValida() {
        return numeroLicenza != null && !numeroLicenza.isBlank();
    }
}
