package com.cardiocare360.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "disponibilita_medico")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class DisponibilitaMedico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ⭐ MEDICO
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medico_id", nullable = false)
    @JsonIgnoreProperties({"pazienti", "visite", "esami", "notifiche"})
    private Medico medico;

    @Column(name = "giorno_settimana", nullable = false, length = 10)
    private String giornoSettimana; // es: "LUN", "MAR", "MER"

    @Column(name = "ora_inizio", nullable = false)
    private LocalTime oraInizio;

    @Column(name = "ora_fine", nullable = false)
    private LocalTime oraFine;

    public DisponibilitaMedico() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { this.medico = medico; }

    public String getGiornoSettimana() { return giornoSettimana; }
    public void setGiornoSettimana(String giornoSettimana) { this.giornoSettimana = giornoSettimana; }

    public LocalTime getOraInizio() { return oraInizio; }
    public void setOraInizio(LocalTime oraInizio) { this.oraInizio = oraInizio; }

    public LocalTime getOraFine() { return oraFine; }
    public void setOraFine(LocalTime oraFine) { this.oraFine = oraFine; }
}
