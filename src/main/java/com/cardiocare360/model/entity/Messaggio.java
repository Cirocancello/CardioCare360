package com.cardiocare360.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messaggio")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Messaggio {

    public enum Mittente {
        PAZIENTE,
        MEDICO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ⭐ CONVERSAZIONE (relazione figlio → padre)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "conversazione_id", nullable = false)
    @JsonIgnoreProperties({"paziente", "medico", "messaggi"})
    private Conversazione conversazione;

    // ⭐ MITTENTE
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Mittente mittente;

    // ⭐ TESTO DEL MESSAGGIO
    @Column(nullable = false, columnDefinition = "TEXT")
    private String testo;

    // ⭐ TIMESTAMP (formattato per React)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Europe/Rome")
    @Column(nullable = false)
    private LocalDateTime timestamp;

    // ⭐ STATO LETTURA
    @Column(nullable = false)
    private boolean letto = false;

    public Messaggio() {}

    // GETTER & SETTER
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Conversazione getConversazione() { return conversazione; }
    public void setConversazione(Conversazione conversazione) { this.conversazione = conversazione; }

    public Mittente getMittente() { return mittente; }
    public void setMittente(Mittente mittente) { this.mittente = mittente; }

    public String getTesto() { return testo; }
    public void setTesto(String testo) { this.testo = testo; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public boolean isLetto() { return letto; }
    public void setLetto(boolean letto) { this.letto = letto; }
}
