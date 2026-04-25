package com.cardiocare360.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "utente")
public class Utente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nome;

    @Column(nullable = false, length = 50)
    private String cognome;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Ruolo ruolo;

    @Column(name = "data_registrazione")
    private LocalDateTime dataRegistrazione = LocalDateTime.now();

    // ENUM Ruolo
    public enum Ruolo {
        PAZIENTE,
        MEDICO,
        ADMIN
    }

    // Costruttore vuoto richiesto da JPA
    public Utente() {}

    // Getter e Setter (puoi usare Lombok se vuoi)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCognome() { return cognome; }
    public void setCognome(String cognome) { this.cognome = cognome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Ruolo getRuolo() { return ruolo; }
    public void setRuolo(Ruolo ruolo) { this.ruolo = ruolo; }

    public LocalDateTime getDataRegistrazione() { return dataRegistrazione; }
    public void setDataRegistrazione(LocalDateTime dataRegistrazione) { this.dataRegistrazione = dataRegistrazione; }
}
