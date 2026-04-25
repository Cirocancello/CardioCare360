package com.cardiocare360.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "medico")
public class Medico {

    @Id
    private Long id; // stessa PK dell'utente

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Utente utente;

    @Column(nullable = false, length = 100)
    private String specializzazione;

    @Column(name = "numero_licenza", nullable = false, unique = true, length = 50)
    private String numeroLicenza;

    // Costruttore vuoto richiesto da JPA
    public Medico() {}

    // Getter e Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Utente getUtente() { return utente; }
    public void setUtente(Utente utente) { this.utente = utente; }

    public String getSpecializzazione() { return specializzazione; }
    public void setSpecializzazione(String specializzazione) { this.specializzazione = specializzazione; }

    public String getNumeroLicenza() { return numeroLicenza; }
    public void setNumeroLicenza(String numeroLicenza) { this.numeroLicenza = numeroLicenza; }
}
