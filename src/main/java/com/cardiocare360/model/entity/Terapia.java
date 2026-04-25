package com.cardiocare360.model.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "terapia")
public class Terapia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "paziente_id", nullable = false)
    private Paziente paziente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    @ManyToOne(optional = false)
    @JoinColumn(name = "farmaco_id", nullable = false)
    private Farmaco farmaco;

    @OneToOne(optional = false)
    @JoinColumn(name = "appuntamento_id", nullable = false, unique = true)
    private Appuntamento appuntamento;

    @Column(nullable = false, length = 255)
    private String dosaggio;

    @Column(name = "data_inizio", nullable = false)
    private LocalDate dataInizio;

    @Column(name = "data_fine")
    private LocalDate dataFine;

    @Column(name = "data_creazione", nullable = false)
    private LocalDateTime dataCreazione = LocalDateTime.now();

    // Costruttore vuoto richiesto da JPA
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

    public LocalDate getDataInizio() { return dataInizio; }
    public void setDataInizio(LocalDate dataInizio) { this.dataInizio = dataInizio; }

    public LocalDate getDataFine() { return dataFine; }
    public void setDataFine(LocalDate dataFine) { this.dataFine = dataFine; }

    public LocalDateTime getDataCreazione() { return dataCreazione; }
    public void setDataCreazione(LocalDateTime dataCreazione) { this.dataCreazione = dataCreazione; }
}
