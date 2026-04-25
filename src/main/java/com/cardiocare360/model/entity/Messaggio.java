package com.cardiocare360.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messaggio")
public class Messaggio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "mittente_id", nullable = false)
    private Utente mittente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "destinatario_id", nullable = false)
    private Utente destinatario;

    @ManyToOne
    @JoinColumn(name = "appuntamento_id")
    private Appuntamento appuntamento; // opzionale

    @Column(nullable = false, length = 1000)
    private String contenuto;

    @Column(name = "data_invio", nullable = false)
    private LocalDateTime dataInvio = LocalDateTime.now();

    @Column(nullable = false)
    private boolean letto = false;

    public Messaggio() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Utente getMittente() { return mittente; }
    public void setMittente(Utente mittente) { this.mittente = mittente; }

    public Utente getDestinatario() { return destinatario; }
    public void setDestinatario(Utente destinatario) { this.destinatario = destinatario; }

    public Appuntamento getAppuntamento() { return appuntamento; }
    public void setAppuntamento(Appuntamento appuntamento) { this.appuntamento = appuntamento; }

    public String getContenuto() { return contenuto; }
    public void setContenuto(String contenuto) { this.contenuto = contenuto; }

    public LocalDateTime getDataInvio() { return dataInvio; }
    public void setDataInvio(LocalDateTime dataInvio) { this.dataInvio = dataInvio; }

    public boolean isLetto() { return letto; }
    public void setLetto(boolean letto) { this.letto = letto; }
}
