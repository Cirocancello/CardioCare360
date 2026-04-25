package com.cardiocare360.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "storico_parametri")
public class StoricoParametri {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "paziente_id", nullable = false)
    private Paziente paziente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "parametro_id", nullable = false)
    private ParametroClinico parametro;

    @Column(nullable = false, length = 50)
    private String valore;

    @Column(name = "data_rilevazione", nullable = false)
    private LocalDateTime dataRilevazione = LocalDateTime.now();

    public StoricoParametri() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Paziente getPaziente() { return paziente; }
    public void setPaziente(Paziente paziente) { this.paziente = paziente; }

    public ParametroClinico getParametro() { return parametro; }
    public void setParametro(ParametroClinico parametro) { this.parametro = parametro; }

    public String getValore() { return valore; }
    public void setValore(String valore) { this.valore = valore; }

    public LocalDateTime getDataRilevazione() { return dataRilevazione; }
    public void setDataRilevazione(LocalDateTime dataRilevazione) { this.dataRilevazione = dataRilevazione; }
}
