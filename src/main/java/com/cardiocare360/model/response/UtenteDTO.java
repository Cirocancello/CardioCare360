// Cosa rappresenta: i dati dell’utente che vogliamo esporre al frontend (senza password).


package com.cardiocare360.model.response;

import lombok.Data;

@Data
public class UtenteDTO {

    private Long id;
    private String nome;
    private String cognome;
    private String email;
    private String ruolo;
}
