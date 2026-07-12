package com.cardiocare360.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "appuntamento")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paziente_id", nullable = false)
    @JsonIgnoreProperties({"appuntamenti", "esami", "parametri", "conversazioni"})
    private Paziente paziente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medico_id", nullable = false)
    @JsonIgnoreProperties({"pazienti", "visite", "esami"})
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

    @Column(name = "tipo_visita")
    private String tipoVisita;

    @OneToMany(mappedBy = "appuntamento")
    @JsonIgnoreProperties({"appuntamento", "utente", "parametroClinico"})
    private List<Notifica> notifiche;

    @OneToMany(mappedBy = "appuntamento")
    @JsonIgnoreProperties({"appuntamento", "paziente", "medico", "farmaco"})
    private List<Terapia> terapie;

    public Appuntamento() {}

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

    public List<Terapia> getTerapie() { return terapie; }
    public void setTerapie(List<Terapia> terapie) { this.terapie = terapie; }
}
