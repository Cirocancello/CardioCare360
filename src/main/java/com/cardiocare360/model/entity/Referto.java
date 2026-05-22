package com.cardiocare360.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "referto")
public class Referto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relazione 1–1 con Esame
    @OneToOne(optional = false)
    @JoinColumn(name = "esame_id", nullable = false, unique = true)
    private Esame esame;

    // Medico che ha redatto il referto
    @ManyToOne(optional = false)
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    // Note del medico
    @Column(name = "note_medico", columnDefinition = "TEXT")
    private String noteMedico;

    // Percorso del file PDF salvato nel filesystem
    @Column(name = "file_path", nullable = false)
    private String filePath;

    // Data di creazione del referto (campo ESISTENTE nel DB)
    @Column(name = "data_creazione", nullable = false)
    private LocalDateTime dataCreazione = LocalDateTime.now();

    public Referto() {}

    // Getter e Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Esame getEsame() { return esame; }
    public void setEsame(Esame esame) { this.esame = esame; }

    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { this.medico = medico; }

    public String getNoteMedico() { return noteMedico; }
    public void setNoteMedico(String noteMedico) { this.noteMedico = noteMedico; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public LocalDateTime getDataCreazione() { return dataCreazione; }
    public void setDataCreazione(LocalDateTime dataCreazione) { this.dataCreazione = dataCreazione; }
}
