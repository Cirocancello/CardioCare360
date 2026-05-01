package com.cardiocare360.model.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "appuntamento")
public class Appuntamento {

    public enum StatoAppuntamento {
        PRENOTATO,
        COMPLETATO,
        ANNULLATO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "paziente_id", nullable = false)
    private Paziente paziente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    @Column(name = "data_appuntamento", nullable = false)
    private LocalDate dataAppuntamento;

    @Column(name = "ora_appuntamento", nullable = false)
    private LocalTime oraAppuntamento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatoAppuntamento stato;

    @Column(columnDefinition = "TEXT")
    private String note;

    public Appuntamento() {}

    // Getter e Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Paziente getPaziente() { return paziente; }
    public void setPaziente(Paziente paziente) { this.paziente = paziente; }

    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { this.medico = medico; }

    public LocalDate getDataAppuntamento() { return dataAppuntamento; }
    public void setDataAppuntamento(LocalDate dataAppuntamento) { this.dataAppuntamento = dataAppuntamento; }

    public LocalTime getOraAppuntamento() { return oraAppuntamento; }
    public void setOraAppuntamento(LocalTime oraAppuntamento) { this.oraAppuntamento = oraAppuntamento; }

    public StatoAppuntamento getStato() { return stato; }
    public void setStato(StatoAppuntamento stato) { this.stato = stato; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
