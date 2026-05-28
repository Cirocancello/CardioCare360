package com.cardiocare360.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "conversazione")
@JsonIgnoreProperties({"messaggi"})
public class Conversazione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔹 Paziente coinvolto nella conversazione
    @ManyToOne(optional = false)
    @JoinColumn(name = "paziente_id", nullable = false)
    private Paziente paziente;

    // 🔹 Medico coinvolto nella conversazione
    @ManyToOne(optional = false)
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    // 🔹 Data creazione conversazione
    @Column(nullable = false)
    private LocalDateTime dataCreazione;

    // 🔹 Ultimo messaggio (testo)
    @Column(name = "ultimo_messaggio")
    private String ultimoMessaggio;

    // 🔹 Timestamp ultimo aggiornamento
    @Column(name = "ultimo_aggiornamento")
    private LocalDateTime ultimoAggiornamento;

    // 🔹 Lista messaggi associati
    @OneToMany(mappedBy = "conversazione", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("conversazione")
    private List<Messaggio> messaggi;

    public Conversazione() {}

    // Getter & Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Paziente getPaziente() { return paziente; }
    public void setPaziente(Paziente paziente) { this.paziente = paziente; }

    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { this.medico = medico; }

    public LocalDateTime getDataCreazione() { return dataCreazione; }
    public void setDataCreazione(LocalDateTime dataCreazione) { this.dataCreazione = dataCreazione; }

    public String getUltimoMessaggio() { return ultimoMessaggio; }
    public void setUltimoMessaggio(String ultimoMessaggio) { this.ultimoMessaggio = ultimoMessaggio; }

    public LocalDateTime getUltimoAggiornamento() { return ultimoAggiornamento; }
    public void setUltimoAggiornamento(LocalDateTime ultimoAggiornamento) { this.ultimoAggiornamento = ultimoAggiornamento; }

    public List<Messaggio> getMessaggi() { return messaggi; }
    public void setMessaggi(List<Messaggio> messaggi) { this.messaggi = messaggi; }
}
