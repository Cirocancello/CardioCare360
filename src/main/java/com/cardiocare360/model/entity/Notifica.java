package com.cardiocare360.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifica")
public class Notifica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "utente_id", nullable = false)
    private Utente utente;

    @Column(nullable = false, length = 255)
    private String titolo;

    @Column(nullable = false, length = 1000)
    private String messaggio;

    @Column(nullable = false)
    private boolean letto = false;

    @Column(name = "data_creazione", nullable = false)
    private LocalDateTime dataCreazione = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "appuntamento_id")
    private Appuntamento appuntamento; // opzionale

    @ManyToOne
    @JoinColumn(name = "parametro_id")
    private ParametroClinico parametroClinico; // opzionale

    public Notifica() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Utente getUtente() { return utente; }
    public void setUtente(Utente utente) { this.utente = utente; }

    public String getTitolo() { return titolo; }
    public void setTitolo(String titolo) { this.titolo = titolo; }

    public String getMessaggio() { return messaggio; }
    public void setMessaggio(String messaggio) { this.messaggio = messaggio; }

    public boolean isLetto() { return letto; }
    public void setLetto(boolean letto) { this.letto = letto; }

    public LocalDateTime getDataCreazione() { return dataCreazione; }
    public void setDataCreazione(LocalDateTime dataCreazione) { this.dataCreazione = dataCreazione; }

    public Appuntamento getAppuntamento() { return appuntamento; }
    public void setAppuntamento(Appuntamento appuntamento) { this.appuntamento = appuntamento; }

    public ParametroClinico getParametroClinico() { return parametroClinico; }
    public void setParametroClinico(ParametroClinico parametroClinico) { this.parametroClinico = parametroClinico; }
}
