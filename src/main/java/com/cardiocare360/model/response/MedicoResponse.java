package com.cardiocare360.model.response;

public class MedicoResponse {

    private Long id;
    private String nomeCompleto;
    private String email;
    private String specializzazione;
    private String numeroLicenza;

    public MedicoResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNomeCompleto() { return nomeCompleto; }
    public void setNomeCompleto(String nomeCompleto) { this.nomeCompleto = nomeCompleto; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSpecializzazione() { return specializzazione; }
    public void setSpecializzazione(String specializzazione) { this.specializzazione = specializzazione; }

    public String getNumeroLicenza() { return numeroLicenza; }
    public void setNumeroLicenza(String numeroLicenza) { this.numeroLicenza = numeroLicenza; }

    // ---------------------------------------------------------
    // VALIDAZIONI DI SICUREZZA
    // ---------------------------------------------------------

    public boolean isValid() {
        return id != null && id > 0 &&
               nomeCompleto != null && !nomeCompleto.isBlank() &&
               email != null && !email.isBlank() &&
               specializzazione != null && !specializzazione.isBlank() &&
               numeroLicenza != null && !numeroLicenza.isBlank();
    }

    public boolean hasBasicInfo() {
        return nomeCompleto != null && !nomeCompleto.isBlank();
    }
}
