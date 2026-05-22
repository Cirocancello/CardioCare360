package com.cardiocare360.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class RegisterRequest {

    // 🔹 Campi comuni a TUTTI gli utenti
    private String nome;
    private String cognome;
    private String email;
    private String password;

    // 🔹 Campi specifici per PAZIENTE
    private String codiceFiscale;
    private String luogoNascita;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private String dataNascita;

    private String telefono;
    private String indirizzo;

    // 🔥 Campo che decide tutto
    private String ruolo; // PAZIENTE, MEDICO, ADMIN

    // 🔹 Campi specifici per MEDICO
    private String specializzazione;
    private String numeroLicenza;
}
