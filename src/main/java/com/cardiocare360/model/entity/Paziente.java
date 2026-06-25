package com.cardiocare360.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "paziente")
@PrimaryKeyJoinColumn(name = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@DynamicUpdate
public class Paziente extends Utente {

    @Column(name = "codice_fiscale", nullable = false, unique = true, length = 16)
    private String codiceFiscale;

    @Column(name = "luogo_nascita", nullable = false, length = 100)
    private String luogoNascita;

    @Column(name = "data_nascita", nullable = false)
    private LocalDate dataNascita;

    @Column(length = 20)
    private String telefono;

    @Column(length = 255)
    private String indirizzo;

    // ⭐ MEDICO ASSOCIATO
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medico_id")
    @JsonIgnoreProperties({"pazienti", "visite", "esami"})
    private Medico medico;

    // ⭐ APPUNTAMENTI DEL PAZIENTE
    @OneToMany(mappedBy = "paziente")
    @JsonIgnoreProperties({"paziente", "medico", "notifiche", "terapia"})
    private List<Appuntamento> appuntamenti;

    // ⭐ ESAMI DEL PAZIENTE
    @OneToMany(mappedBy = "paziente")
    @JsonIgnoreProperties({"paziente", "medico", "referti"})
    private List<Esame> esami;

    // ⭐ PARAMETRI CLINICI DEL PAZIENTE
    @OneToMany(mappedBy = "paziente")
    @JsonIgnoreProperties("paziente")
    private List<ParametroClinico> parametri;

    // ⭐ CONVERSAZIONI DEL PAZIENTE
    @OneToMany(mappedBy = "paziente")
    @JsonIgnoreProperties({"paziente", "medico", "messaggi"})
    private List<Conversazione> conversazioni;

    public Paziente() {}

    public String getCodiceFiscale() { return codiceFiscale; }
    public void setCodiceFiscale(String codiceFiscale) { this.codiceFiscale = codiceFiscale; }

    public String getLuogoNascita() { return luogoNascita; }
    public void setLuogoNascita(String luogoNascita) { this.luogoNascita = luogoNascita; }

    public LocalDate getDataNascita() { return dataNascita; }
    public void setDataNascita(LocalDate dataNascita) { this.dataNascita = dataNascita; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getIndirizzo() { return indirizzo; }
    public void setIndirizzo(String indirizzo) { this.indirizzo = indirizzo; }

    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { this.medico = medico; }

    public List<Appuntamento> getAppuntamenti() { return appuntamenti; }
    public void setAppuntamenti(List<Appuntamento> appuntamenti) { this.appuntamenti = appuntamenti; }

    public List<Esame> getEsami() { return esami; }
    public void setEsami(List<Esame> esami) { this.esami = esami; }

    public List<ParametroClinico> getParametri() { return parametri; }
    public void setParametri(List<ParametroClinico> parametri) { this.parametri = parametri; }

    public List<Conversazione> getConversazioni() { return conversazioni; }
    public void setConversazioni(List<Conversazione> conversazioni) { this.conversazioni = conversazioni; }
}
