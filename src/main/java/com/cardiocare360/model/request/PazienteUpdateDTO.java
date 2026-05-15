package com.cardiocare360.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class PazienteUpdateDTO {

    private String nome;
    private String cognome;

    private String codiceFiscale;

    private String luogoNascita;

    // 🔥 Il frontend invia "yyyy-MM-dd"
    @JsonFormat(pattern = "yyyy-MM-dd")
    private String dataNascita;

    private String telefono;
    private String indirizzo;
}
