package com.cardiocare360.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifica")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Notifica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ⭐ UTENTE
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "utente_id", nullable = false)
    @JsonIgnoreProperties({
            "password",
            "ruolo",
            "appuntamenti",
            "terapie",
            "referti",
            "notifiche",
            "parametri",
            "conversazioni"
    })
    private Utente utente;

    @Column(nullable = false, length = 255)
    private String titolo;

    @Column(nullable = false, length = 1000)
    private String messaggio;

    @Column(nullable = false)
    private boolean letto = false;

    @Column(name = "data_creazione", nullable = false)
    private LocalDateTime dataCreazione = LocalDateTime.now();

    // ⭐ APPUNTAMENTO (opzionale)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appuntamento_id")
    @JsonIgnoreProperties({
            "paziente",
            "medico",
            "slot",
            "notifiche",
            "terapia"
    })
    private Appuntamento appuntamento;

    // ⭐ PARAMETRO CLINICO (opzionale)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parametro_id")
    @JsonIgnoreProperties({
            "paziente"
    })
    private ParametroClinico parametroClinico;

    public Notifica() {}

    // GETTER & SETTER

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
