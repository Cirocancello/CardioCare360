package com.cardiocare360.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "medico")
@PrimaryKeyJoinColumn(name = "id") // usa la stessa PK di Utente
@JsonIgnoreProperties({"notifiche"})
public class Medico extends Utente {

    @Column(nullable = false, length = 100)
    private String specializzazione;

    @Column(name = "numero_licenza", nullable = false, unique = true, length = 50)
    private String numeroLicenza;

    @OneToMany(mappedBy = "medico", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("medico")
    private List<Notifica> notifiche;

    public Medico() {}

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

    public List<Notifica> getNotifiche() {
        return notifiche;
    }

    public void setNotifiche(List<Notifica> notifiche) {
        this.notifiche = notifiche;
    }
}
