package com.cardiocare360.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "farmaco")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Farmaco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 100)
    private String principioAttivo;

    @Column(length = 255)
    private String descrizione;

    public Farmaco() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getPrincipioAttivo() { return principioAttivo; }
    public void setPrincipioAttivo(String principioAttivo) { this.principioAttivo = principioAttivo; }

    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }
}
