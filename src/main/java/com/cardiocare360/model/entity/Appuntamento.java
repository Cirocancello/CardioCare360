package com.cardiocare360.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "appuntamento")
@JsonIgnoreProperties({"notifiche"}) // 🔥 evita loop Appuntamento → Notifica → Appuntamento
public class Appuntamento {

    public enum StatoAppuntamento {
        PRENOTATO,
        COMPLETATO,
        ANNULLATO,
        CONFERMATO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "paziente_id", nullable = false)
    @JsonIgnoreProperties({"notifiche"})
    private Paziente paziente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "medico_id", nullable = false)
    @JsonIgnoreProperties({"notifiche"})
    private Medico medico;

    @Column(name = "data_appuntamento", nullable = false)
    private LocalDate dataAppuntamento;

    @Column(name = "ora_appuntamento", nullable = false)
    private LocalTime oraAppuntamento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatoAppuntamento stato;

    @Column(columnDefinition = "TEXT")
    private String note;

    // ⭐ NUOVO CAMPO: tipo di visita
    @Column(name = "tipo_visita")
    private String tipoVisita;

    // ⭐ Relazione con Notifica (necessaria!)
    @OneToMany(mappedBy = "appuntamento", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("appuntamento")
    private List<Notifica> notifiche;

    public Appuntamento() {}

    // Getter e Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Paziente getPaziente() { return paziente; }
    public void setPaziente(Paziente paziente) { this.paziente = paziente; }

    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { this.medico = medico; }

    public LocalDate getDataAppuntamento() { return dataAppuntamento; }
    public void setDataAppuntamento(LocalDate dataAppuntamento) { this.dataAppuntamento = dataAppuntamento; }

    public LocalTime getOraAppuntamento() { return oraAppuntamento; }
    public void setOraAppuntamento(LocalTime oraAppuntamento) { this.oraAppuntamento = oraAppuntamento; }

    public StatoAppuntamento getStato() { return stato; }
    public void setStato(StatoAppuntamento stato) { this.stato = stato; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getTipoVisita() { return tipoVisita; }
    public void setTipoVisita(String tipoVisita) { this.tipoVisita = tipoVisita; }

    public List<Notifica> getNotifiche() { return notifiche; }
    public void setNotifiche(List<Notifica> notifiche) { this.notifiche = notifiche; }
}
