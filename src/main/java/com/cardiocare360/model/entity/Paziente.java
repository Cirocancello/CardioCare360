package com.cardiocare360.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "paziente")
@PrimaryKeyJoinColumn(name = "id")
@JsonIgnoreProperties({"notifiche"}) // 🔥 evita loop Paziente → Notifica → Paziente
public class Paziente extends Utente {

    @Column(name = "codice_fiscale", nullable = false, unique = true, length = 16)
    private String codiceFiscale;

    @Column(name = "data_nascita", nullable = false)
    private LocalDate dataNascita;

    @Column(length = 20)
    private String telefono;

    @Column(length = 255)
    private String indirizzo;

    // ⭐ Relazione con Notifica (necessaria!)
    @OneToMany(mappedBy = "paziente", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("paziente")
    private List<Notifica> notifiche;

    public Paziente() {}

    // Getter e Setter
    public String getCodiceFiscale() { return codiceFiscale; }
    public void setCodiceFiscale(String codiceFiscale) { this.codiceFiscale = codiceFiscale; }

    public LocalDate getDataNascita() { return dataNascita; }
    public void setDataNascita(LocalDate dataNascita) { this.dataNascita = dataNascita; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getIndirizzo() { return indirizzo; }
    public void setIndirizzo(String indirizzo) { this.indirizzo = indirizzo; }

    public List<Notifica> getNotifiche() { return notifiche; }
    public void setNotifiche(List<Notifica> notifiche) { this.notifiche = notifiche; }
}
