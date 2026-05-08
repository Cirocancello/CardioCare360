package com.cardiocare360.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "parametro_clinico")
@JsonIgnoreProperties({"notifiche"}) // 🔥 evita loop Parametro → Notifica → Parametro
public class ParametroClinico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "paziente_id", nullable = false)
    @JsonIgnoreProperties({"notifiche"})
    private Paziente paziente;

    @Column(nullable = false, length = 100)
    private String tipo; // es: PRESSIONE, BATTITO, GLICEMIA

    @Column(nullable = false, length = 50)
    private String valore; // es: "120/80", "85 bpm", "95 mg/dL"

    @Column(name = "data_rilevazione", nullable = false)
    private LocalDateTime dataRilevazione = LocalDateTime.now();

    // ⭐ Relazione con Notifica (necessaria!)
    @OneToMany(mappedBy = "parametroClinico", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("parametroClinico")
    private List<Notifica> notifiche;

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

    public List<Notifica> getNotifiche() { return notifiche; }
    public void setNotifiche(List<Notifica> notifiche) { this.notifiche = notifiche; }
}
