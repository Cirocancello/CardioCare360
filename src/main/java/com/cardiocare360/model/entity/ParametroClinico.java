package com.cardiocare360.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "parametro_clinico")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ParametroClinico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ⭐ PAZIENTE (relazione figlio → padre)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paziente_id", nullable = false)
    @JsonIgnoreProperties({"appuntamenti", "esami", "parametri", "conversazioni"})
    private Paziente paziente;

    // Nome leggibile del parametro (es. "Pressione arteriosa")
    @Column(nullable = false, length = 100)
    private String nome;

    // Tipo logico (es. "PRESSIONE", "GLICEMIA")
    @Column(nullable = false, length = 100)
    private String tipo;

    // Valore inserito dal paziente (es. "120/80")
    @Column(nullable = false, length = 50)
    private String valore;

    // Unità di misura (es. "mmHg", "mg/dL")
    @Column(name = "unita_misura", length = 50)
    private String unitaMisura;

    @Column(name = "data_rilevazione", nullable = false)
    private LocalDateTime dataRilevazione = LocalDateTime.now();

    public ParametroClinico() {}

    // Getter e Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Paziente getPaziente() { return paziente; }
    public void setPaziente(Paziente paziente) { this.paziente = paziente; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getValore() { return valore; }
    public void setValore(String valore) { this.valore = valore; }

    public String getUnitaMisura() { return unitaMisura; }
    public void setUnitaMisura(String unitaMisura) { this.unitaMisura = unitaMisura; }

    public LocalDateTime getDataRilevazione() { return dataRilevazione; }
    public void setDataRilevazione(LocalDateTime dataRilevazione) { this.dataRilevazione = dataRilevazione; }
}
