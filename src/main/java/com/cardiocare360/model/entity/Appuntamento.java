package com.cardiocare360.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "appuntamento")
public class Appuntamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "paziente_id", nullable = false)
    private Paziente paziente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    @OneToOne(optional = false)
    @JoinColumn(name = "slot_id", nullable = false, unique = true)
    private SlotOrario slot;

    @Column(nullable = false, length = 20)
    private String stato; // es: "PRENOTATO", "COMPLETATO", "ANNULLATO"

    @Column(name = "data_creazione", nullable = false)
    private LocalDateTime dataCreazione = LocalDateTime.now();

    // Costruttore vuoto richiesto da JPA
    public Appuntamento() {}

    // Getter e Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Paziente getPaziente() { return paziente; }
    public void setPaziente(Paziente paziente) { this.paziente = paziente; }

    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { this.medico = medico; }

    public SlotOrario getSlot() { return slot; }
    public void setSlot(SlotOrario slot) { this.slot = slot; }

    public String getStato() { return stato; }
    public void setStato(String stato) { this.stato = stato; }

    public LocalDateTime getDataCreazione() { return dataCreazione; }
    public void setDataCreazione(LocalDateTime dataCreazione) { this.dataCreazione = dataCreazione; }
}
