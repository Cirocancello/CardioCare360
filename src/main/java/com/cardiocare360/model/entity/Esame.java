package com.cardiocare360.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "esame")
@JsonIgnoreProperties({"referti"})
public class Esame {

    public enum StatoEsame {
        PRENOTATO,
        ESEGUITO,
        REFERTATO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "paziente_id", nullable = false)
    private Paziente paziente;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    @Column(name = "tipo_esame", nullable = false)
    private String tipoEsame;

    @Column(name = "data_esame", nullable = false)
    private LocalDate dataEsame;

    @Column(name = "ora_esame", nullable = false)
    private LocalTime oraEsame;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatoEsame stato;

    @Column(columnDefinition = "TEXT")
    private String note;

    // Relazione corretta: un esame può avere più referti
    @OneToMany(mappedBy = "esame")
    private List<Referto> referti;

    public Esame() {}

    // Getter e Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Paziente getPaziente() { return paziente; }
    public void setPaziente(Paziente paziente) { this.paziente = paziente; }

    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { this.medico = medico; }

    public String getTipoEsame() { return tipoEsame; }
    public void setTipoEsame(String tipoEsame) { this.tipoEsame = tipoEsame; }

    public LocalDate getDataEsame() { return dataEsame; }
    public void setDataEsame(LocalDate dataEsame) { this.dataEsame = dataEsame; }

    public LocalTime getOraEsame() { return oraEsame; }
    public void setOraEsame(LocalTime oraEsame) { this.oraEsame = oraEsame; }

    public StatoEsame getStato() { return stato; }
    public void setStato(StatoEsame stato) { this.stato = stato; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public List<Referto> getReferti() { return referti; }
    public void setReferti(List<Referto> referti) { this.referti = referti; }
}
