package com.cardiocare360.model.entity;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "disponibilita_medico")
public class DisponibilitaMedico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    @Column(nullable = false, length = 20)
    private String giorno; // es: "LUNEDI", "MARTEDI", ecc.

    @Column(name = "ora_inizio", nullable = false)
    private LocalTime oraInizio;

    @Column(name = "ora_fine", nullable = false)
    private LocalTime oraFine;

    // Costruttore vuoto richiesto da JPA
    public DisponibilitaMedico() {}

    // Getter e Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { this.medico = medico; }

    public String getGiorno() { return giorno; }
    public void setGiorno(String giorno) { this.giorno = giorno; }

    public LocalTime getOraInizio() { return oraInizio; }
    public void setOraInizio(LocalTime oraInizio) { this.oraInizio = oraInizio; }

    public LocalTime getOraFine() { return oraFine; }
    public void setOraFine(LocalTime oraFine) { this.oraFine = oraFine; }
}
