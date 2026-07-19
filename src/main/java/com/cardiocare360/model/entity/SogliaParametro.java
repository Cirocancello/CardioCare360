package com.cardiocare360.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "soglia_parametro")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class SogliaParametro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ⭐ PAZIENTE
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paziente_id", nullable = false)
    @JsonIgnoreProperties({"appuntamenti", "esami", "parametri", "conversazioni"})
    private Paziente paziente;

    // ⭐ PARAMETRO (NUOVA ENTITÀ)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "parametro_id", referencedColumnName = "id", nullable = false)
    private Parametro parametro;

    @Column(name = "valore_min", nullable = false)
    private Double valoreMin;

    @Column(name = "valore_max", nullable = false)
    private Double valoreMax;

    public SogliaParametro() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Paziente getPaziente() { return paziente; }
    public void setPaziente(Paziente paziente) { this.paziente = paziente; }

    public Parametro getParametro() { return parametro; }
    public void setParametro(Parametro parametro) { this.parametro = parametro; }

    public Double getValoreMin() { return valoreMin; }
    public void setValoreMin(Double valoreMin) { this.valoreMin = valoreMin; }

    public Double getValoreMax() { return valoreMax; }
    public void setValoreMax(Double valoreMax) { this.valoreMax = valoreMax; }
}
