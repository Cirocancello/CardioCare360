package com.cardiocare360.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "amministratore")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Admin {

    @Id
    private Long id; // stessa PK dell'utente

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    @JsonIgnoreProperties({
            "password",
            "notifiche",
            "appuntamenti",
            "esami",
            "parametri",
            "conversazioni"
    })
    private Utente utente;

    @Column(nullable = false, length = 50)
    private String livello;

    public Admin() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Utente getUtente() { return utente; }
    public void setUtente(Utente utente) { this.utente = utente; }

    public String getLivello() { return livello; }
    public void setLivello(String livello) { this.livello = livello; }
}
