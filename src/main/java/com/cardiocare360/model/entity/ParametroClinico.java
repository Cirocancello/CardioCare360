package com.cardiocare360.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "parametro_clinico")
public class ParametroClinico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "paziente_id", nullable = false)
    private Paziente paziente;

    @Column(nullable = false, length = 100)
    private String tipo; // es: PRESSIONE, BATTITO, GLICEMIA

    @Column(nullable = false, length = 50)
    private String valore; // es: "120/80", "85 bpm", "95 mg/dL"

    @Column(name = "data_rilevazione", nullable = false)
    private LocalDateTime dataRilevazione = LocalDateTime.now();

    // Costruttore vuoto richiesto da JPA
    public ParametroClinico() {}

    // Getter e Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Paziente getPaziente() { return paziente; }
    public void setPaziente(Paziente paziente) { this.paziente = paziente; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getValore() { return valore; }
    public void setValore(String valore) { this.valore = valore; }

    public LocalDateTime getDataRilevazione() { return dataRilevazione; }
    public void setDataRilevazione(LocalDateTime dataRilevazione) { this.dataRilevazione = dataRilevazione; }
}
