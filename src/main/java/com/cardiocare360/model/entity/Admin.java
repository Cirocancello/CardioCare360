package com.cardiocare360.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "amministratore")
public class Admin {

    @Id
    private Long id; // stessa PK dell'utente

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Utente utente;

    @Column(nullable = false, length = 50)
    private String livello;

    // Costruttore vuoto richiesto da JPA
    public Admin() {}

    // Getter e Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Utente getUtente() { return utente; }
    public void setUtente(Utente utente) { this.utente = utente; }

    public String getLivello() { return livello; }
    public void setLivello(String livello) { this.livello = livello; }
}
