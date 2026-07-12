package com.cardiocare360.model.response;

import java.time.LocalDate;
import com.cardiocare360.model.entity.Paziente;

public class PazienteDTO {

    private Long id;
    private String nome;
    private String cognome;
    private String email;

    private String codiceFiscale;
    private String luogoNascita;
    private LocalDate dataNascita;

    private String telefono;
    private String indirizzo;

    public PazienteDTO() {}

    public PazienteDTO(Paziente p) {
        this.id = p.getId();
        this.nome = p.getNome();
        this.cognome = p.getCognome();
        this.email = p.getEmail();
        this.codiceFiscale = p.getCodiceFiscale();
        this.luogoNascita = p.getLuogoNascita();
        this.dataNascita = p.getDataNascita();
        this.telefono = p.getTelefono();
        this.indirizzo = p.getIndirizzo();
    }

    // GETTER & SETTER
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCognome() { return cognome; }
    public void setCognome(String cognome) { this.cognome = cognome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCodiceFiscale() { return codiceFiscale; }
    public void setCodiceFiscale(String codiceFiscale) { this.codiceFiscale = codiceFiscale; }

    public String getLuogoNascita() { return luogoNascita; }
    public void setLuogoNascita(String luogoNascita) { this.luogoNascita = luogoNascita; }

    public LocalDate getDataNascita() { return dataNascita; }
    public void setDataNascita(LocalDate dataNascita) { this.dataNascita = dataNascita; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getIndirizzo() { return indirizzo; }
    public void setIndirizzo(String indirizzo) { this.indirizzo = indirizzo; }

    // ---------------------------------------------------------
    // VALIDAZIONI DI SICUREZZA
    // ---------------------------------------------------------

    public boolean isValid() {
        return id != null && id > 0 &&
               nome != null && !nome.isBlank() &&
               cognome != null && !cognome.isBlank() &&
               email != null && !email.isBlank() &&
               codiceFiscale != null && !codiceFiscale.isBlank() &&
               luogoNascita != null && !luogoNascita.isBlank() &&
               dataNascita != null &&
               telefono != null && !telefono.isBlank() &&
               indirizzo != null && !indirizzo.isBlank();
    }

    public boolean hasBasicInfo() {
        return nome != null && cognome != null;
    }
}
