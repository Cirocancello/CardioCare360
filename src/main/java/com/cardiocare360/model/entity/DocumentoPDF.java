package com.cardiocare360.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "documento_pdf")
public class DocumentoPDF {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "appuntamento_id", nullable = false, unique = true)
    private Appuntamento appuntamento;

    @Column(nullable = false, length = 255)
    private String nomeFile;

    @Column(nullable = false, length = 255)
    private String percorsoFile;

    @Column(name = "data_caricamento", nullable = false)
    private LocalDateTime dataCaricamento = LocalDateTime.now();

    // Costruttore vuoto richiesto da JPA
    public DocumentoPDF() {}

    // Getter e Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Appuntamento getAppuntamento() { return appuntamento; }
    public void setAppuntamento(Appuntamento appuntamento) { this.appuntamento = appuntamento; }

    public String getNomeFile() { return nomeFile; }
    public void setNomeFile(String nomeFile) { this.nomeFile = nomeFile; }

    public String getPercorsoFile() { return percorsoFile; }
    public void setPercorsoFile(String percorsoFile) { this.percorsoFile = percorsoFile; }

    public LocalDateTime getDataCaricamento() { return dataCaricamento; }
    public void setDataCaricamento(LocalDateTime dataCaricamento) { this.dataCaricamento = dataCaricamento; }
}
