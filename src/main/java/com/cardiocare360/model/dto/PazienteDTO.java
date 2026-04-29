package com.cardiocare360.model.dto;

import java.time.LocalDate;

import com.cardiocare360.model.entity.Paziente;

public class PazienteDTO {

    private Long id;
    private String nome;
    private String cognome;
    private String email;

    private String codiceFiscale;
    private LocalDate dataNascita;

    private String telefono;
    private String indirizzo;

    public PazienteDTO() {}
    
    public PazienteDTO(Paziente p) {
        this.setId(p.getId());
        this.setNome(p.getNome());
        this.setCognome(p.getCognome());
        this.setEmail(p.getEmail());
        this.setCodiceFiscale(p.getCodiceFiscale());
        this.setDataNascita(p.getDataNascita());
        this.setTelefono(p.getTelefono());
        this.setIndirizzo(p.getIndirizzo());
    }


    // Getter e Setter
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

    public LocalDate getDataNascita() { return dataNascita; }
    public void setDataNascita(LocalDate dataNascita) { this.dataNascita = dataNascita; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getIndirizzo() { return indirizzo; }
    public void setIndirizzo(String indirizzo) { this.indirizzo = indirizzo; }
    
    
}
