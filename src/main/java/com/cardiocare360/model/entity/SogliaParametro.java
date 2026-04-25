package com.cardiocare360.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "soglia_parametro")
public class SogliaParametro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "paziente_id", nullable = false)
    private Paziente paziente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "parametro_id", nullable = false)
    private ParametroClinico parametro;

    @Column(name = "valore_min", length = 50)
    private String valoreMin;

    @Column(name = "valore_max", length = 50)
    private String valoreMax;

    public SogliaParametro() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Paziente getPaziente() { return paziente; }
    public void setPaziente(Paziente paziente) { this.paziente = paziente; }

    public ParametroClinico getParametro() { return parametro; }
    public void setParametro(ParametroClinico parametro) { this.parametro = parametro; }

    public String getValoreMin() { return valoreMin; }
    public void setValoreMin(String valoreMin) { this.valoreMin = valoreMin; }

    public String getValoreMax() { return valoreMax; }
    public void setValoreMax(String valoreMax) { this.valoreMax = valoreMax; }
}
