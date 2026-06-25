package com.cardiocare360.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "terapia")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Terapia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ⭐ PAZIENTE
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paziente_id", nullable = false)
    @JsonIgnoreProperties({"appuntamenti", "esami", "parametri", "conversazioni"})
    private Paziente paziente;

    // ⭐ MEDICO
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medico_id", nullable = false)
    @JsonIgnoreProperties({"pazienti", "visite", "esami"})
    private Medico medico;

    // ⭐ FARMACO
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "farmaco_id", nullable = false)
    private Farmaco farmaco;

    // ⭐ APPUNTAMENTO
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "appuntamento_id", nullable = false, unique = true)
    @JsonIgnoreProperties({"terapia", "notifiche"})
    private Appuntamento appuntamento;

    @Column(nullable = false, length = 255)
    private String dosaggio;

    @Column(length = 500)
    private String note;

    @Column(name = "data_inizio", nullable = false)
    private LocalDate dataInizio;

    @Column(name = "data_fine")
    private LocalDate dataFine;

    @Column(name = "data_creazione", nullable = false)
    private LocalDateTime dataCreazione = LocalDateTime.now();

    public Terapia() {}

    // Getter e Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Paziente getPaziente() { return paziente; }
    public void setPaziente(Paziente paziente) { this.paziente = paziente; }

    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { this.medico = medico; }

    public Farmaco getFarmaco() { return farmaco; }
    public void setFarmaco(Farmaco farmaco) { this.farmaco = farmaco; }

    public Appuntamento getAppuntamento() { return appuntamento; }
    public void setAppuntamento(Appuntamento appuntamento) { this.appuntamento = appuntamento; }

    public String getDosaggio() { return dosaggio; }
    public void setDosaggio(String dosaggio) { this.dosaggio = dosaggio; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public LocalDate getDataInizio() { return dataInizio; }
    public void setDataInizio(LocalDate dataInizio) { this.dataInizio = dataInizio; }

    public LocalDate getDataFine() { return dataFine; }
    public void setDataFine(LocalDate dataFine) { this.dataFine = dataFine; }

    public LocalDateTime getDataCreazione() { return dataCreazione; }
    public void setDataCreazione(LocalDateTime dataCreazione) { this.dataCreazione = dataCreazione; }
}
