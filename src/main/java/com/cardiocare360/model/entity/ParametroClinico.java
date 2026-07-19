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

    // 🔥 NUOVI CAMPI NUMERICI (corretti)
    @Column(name = "pressione_sistolica")
    private Double pressioneSistolica;

    @Column(name = "pressione_diastolica")
    private Double pressioneDiastolica;

    @Column(name = "battiti")
    private Double battiti;

    @Column(name = "glicemia")
    private Double glicemia;

    @Column(name = "saturazione")
    private Double saturazione;

    @Column(name = "peso")
    private Double peso;

    @Column(name = "temperatura")
    private Double temperatura;

    // Unità di misura (es. "mmHg", "mg/dL")
    @Column(name = "unita_misura", length = 50)
    private String unitaMisura;

    @Column(name = "data_rilevazione", nullable = false)
    private LocalDateTime dataRilevazione = LocalDateTime.now();

    @Column(name = "alert")
    private String alert;

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

    public Double getPressioneSistolica() { return pressioneSistolica; }
    public void setPressioneSistolica(Double pressioneSistolica) { this.pressioneSistolica = pressioneSistolica; }

    public Double getPressioneDiastolica() { return pressioneDiastolica; }
    public void setPressioneDiastolica(Double pressioneDiastolica) { this.pressioneDiastolica = pressioneDiastolica; }

    public Double getBattiti() { return battiti; }
    public void setBattiti(Double battiti) { this.battiti = battiti; }

    public Double getGlicemia() { return glicemia; }
    public void setGlicemia(Double glicemia) { this.glicemia = glicemia; }

    public Double getSaturazione() { return saturazione; }
    public void setSaturazione(Double saturazione) { this.saturazione = saturazione; }

    public Double getPeso() { return peso; }
    public void setPeso(Double peso) { this.peso = peso; }

    public Double getTemperatura() { return temperatura; }
    public void setTemperatura(Double temperatura) { this.temperatura = temperatura; }

    public String getUnitaMisura() { return unitaMisura; }
    public void setUnitaMisura(String unitaMisura) { this.unitaMisura = unitaMisura; }

    public LocalDateTime getDataRilevazione() { return dataRilevazione; }
    public void setDataRilevazione(LocalDateTime dataRilevazione) { this.dataRilevazione = dataRilevazione; }

    public String getAlert() { return alert; }
    public void setAlert(String alert) { this.alert = alert; }
}
