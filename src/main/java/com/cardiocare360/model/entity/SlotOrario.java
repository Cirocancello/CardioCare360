package com.cardiocare360.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "slot_orario")
public class SlotOrario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "disponibilita_id", nullable = false)
    private DisponibilitaMedico disponibilita;

    @Column(name = "inizio", nullable = false)
    private LocalDateTime inizio;

    @Column(name = "fine", nullable = false)
    private LocalDateTime fine;

    @Column(nullable = false)
    private boolean prenotato = false;

    // Costruttore vuoto richiesto da JPA
    public SlotOrario() {}

    // Getter e Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public DisponibilitaMedico getDisponibilita() { return disponibilita; }
    public void setDisponibilita(DisponibilitaMedico disponibilita) { this.disponibilita = disponibilita; }

    public LocalDateTime getInizio() { return inizio; }
    public void setInizio(LocalDateTime inizio) { this.inizio = inizio; }

    public LocalDateTime getFine() { return fine; }
    public void setFine(LocalDateTime fine) { this.fine = fine; }

    public boolean isPrenotato() { return prenotato; }
    public void setPrenotato(boolean prenotato) { this.prenotato = prenotato; }
}
