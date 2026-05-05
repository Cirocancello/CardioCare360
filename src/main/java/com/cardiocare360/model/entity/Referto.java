package com.cardiocare360.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "referto")
public class Referto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Paziente a cui appartiene il referto
    @ManyToOne(optional = false)
    @JoinColumn(name = "paziente_id", nullable = false)
    private Paziente paziente;

    // Medico che ha redatto il referto
    @ManyToOne(optional = false)
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    // Titolo del referto
    @Column(nullable = false)
    private String titolo;

    // Descrizione testuale del referto
    @Column(columnDefinition = "TEXT", nullable = false)
    private String descrizione;

    // Diagnosi (campo NOT NULL nel DB)
    @Column(nullable = false, length = 500)
    private String diagnosi;

    // Percorso del file PDF salvato nel filesystem
    @Column(name = "file_path", nullable = false)
    private String filePath;

    // Data di creazione del referto
    @Column(name = "data_creazione", nullable = false)
    private LocalDateTime dataCreazione = LocalDateTime.now();

    // Data del referto
    @Column(name = "data_referto", nullable = false)
    private LocalDateTime dataReferto = LocalDateTime.now();

    public Referto() {}

    // Getter e Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Paziente getPaziente() { return paziente; }
    public void setPaziente(Paziente paziente) { this.paziente = paziente; }

    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { this.medico = medico; }

    public String getTitolo() { return titolo; }
    public void setTitolo(String titolo) { this.titolo = titolo; }

    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

    public String getDiagnosi() { return diagnosi; }
    public void setDiagnosi(String diagnosi) { this.diagnosi = diagnosi; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public LocalDateTime getDataCreazione() { return dataCreazione; }
    public void setDataCreazione(LocalDateTime dataCreazione) { this.dataCreazione = dataCreazione; }

    public LocalDateTime getDataReferto() { return dataReferto; }
    public void setDataReferto(LocalDateTime dataReferto) { this.dataReferto = dataReferto; }
}
