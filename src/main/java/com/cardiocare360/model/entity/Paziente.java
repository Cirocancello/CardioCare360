package com.cardiocare360.model.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "paziente")
public class Paziente {

    @Id
    private Long id; // stessa PK dell'utente

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Utente utente;

    @Column(name = "codice_fiscale", nullable = false, unique = true, length = 16)
    private String codiceFiscale;

    @Column(name = "data_nascita", nullable = false)
    private LocalDate dataNascita;

    @Column(length = 20)
    private String telefono;

    @Column(length = 255)
    private String indirizzo;

    // Costruttore vuoto richiesto da JPA
    public Paziente() {}

    // Getter e Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Utente getUtente() { return utente; }
    public void setUtente(Utente utente) { this.utente = utente; }

    public String getCodiceFiscale() { return codiceFiscale; }
    public void setCodiceFiscale(String codiceFiscale) { this.codiceFiscale = codiceFiscale; }

    public LocalDate getDataNascita() { return dataNascita; }
    public void setDataNascita(LocalDate dataNascita) { this.dataNascita = dataNascita; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getIndirizzo() { return indirizzo; }
    public void setIndirizzo(String indirizzo) { this.indirizzo = indirizzo; }
}
