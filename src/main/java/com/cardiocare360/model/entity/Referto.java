package com.cardiocare360.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "referto")
public class Referto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "appuntamento_id", nullable = false, unique = true)
    private Appuntamento appuntamento;

    @OneToOne(optional = false)
    @JoinColumn(name = "documento_id", nullable = false, unique = true)
    private DocumentoPDF documentoPDF;

    @Column(nullable = false, length = 500)
    private String diagnosi;

    @Column(length = 1000)
    private String note;

    @Column(name = "data_creazione", nullable = false)
    private LocalDateTime dataCreazione = LocalDateTime.now();

    // Costruttore vuoto richiesto da JPA
    public Referto() {}

    // Getter e Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Appuntamento getAppuntamento() { return appuntamento; }
    public void setAppuntamento(Appuntamento appuntamento) { this.appuntamento = appuntamento; }

    public DocumentoPDF getDocumentoPDF() { return documentoPDF; }
    public void setDocumentoPDF(DocumentoPDF documentoPDF) { this.documentoPDF = documentoPDF; }

    public String getDiagnosi() { return diagnosi; }
    public void setDiagnosi(String diagnosi) { this.diagnosi = diagnosi; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public LocalDateTime getDataCreazione() { return dataCreazione; }
    public void setDataCreazione(LocalDateTime dataCreazione) { this.dataCreazione = dataCreazione; }
}
