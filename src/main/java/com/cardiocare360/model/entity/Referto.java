package com.cardiocare360.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "referto")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Referto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ⭐ ESAME
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "esame_id", nullable = false)
    @JsonIgnoreProperties({"referti", "paziente", "medico"})
    private Esame esame;

    // ⭐ MEDICO
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medico_id", nullable = false)
    @JsonIgnoreProperties({"pazienti", "visite", "esami"})
    private Medico medico;

    // ⭐ PAZIENTE
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paziente_id", nullable = false)
    @JsonIgnoreProperties({"appuntamenti", "esami", "parametri", "conversazioni"})
    private Paziente paziente;

    @Column(nullable = false)
    private String titolo;

    @Column(columnDefinition = "TEXT")
    private String descrizione;

    @Column(columnDefinition = "TEXT")
    private String diagnosi;

    @Column(name = "note_medico", columnDefinition = "TEXT")
    private String noteMedico;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "data_creazione", nullable = false)
    private LocalDateTime dataCreazione = LocalDateTime.now();

    @Column(name = "data_referto")
    private LocalDateTime dataReferto;

    public Referto() {}

    // GETTER & SETTER
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Esame getEsame() { return esame; }
    public void setEsame(Esame esame) { this.esame = esame; }

    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { this.medico = medico; }

    public Paziente getPaziente() { return paziente; }
    public void setPaziente(Paziente paziente) { this.paziente = paziente; }

    public String getTitolo() { return titolo; }
    public void setTitolo(String titolo) { this.titolo = titolo; }

    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

    public String getDiagnosi() { return diagnosi; }
    public void setDiagnosi(String diagnosi) { this.diagnosi = diagnosi; }

    public String getNoteMedico() { return noteMedico; }
    public void setNoteMedico(String noteMedico) { this.noteMedico = noteMedico; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public LocalDateTime getDataCreazione() { return dataCreazione; }
    public void setDataCreazione(LocalDateTime dataCreazione) { this.dataCreazione = dataCreazione; }

    public LocalDateTime getDataReferto() { return dataReferto; }
    public void setDataReferto(LocalDateTime dataReferto) { this.dataReferto = dataReferto; }
}
