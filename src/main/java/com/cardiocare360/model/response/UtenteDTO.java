package com.cardiocare360.model.response;

import lombok.Data;

@Data
public class UtenteDTO {

    private Long id;
    private String nome;
    private String cognome;
    private String email;
    private String ruolo;

    // ---------------------------------------------------------
    // VALIDAZIONI DI SICUREZZA
    // ---------------------------------------------------------

    public boolean isValid() {
        return id != null && id > 0 &&
               nome != null && !nome.isBlank() &&
               cognome != null && !cognome.isBlank() &&
               email != null && !email.isBlank() &&
               ruolo != null && !ruolo.isBlank();
    }

    public boolean hasBasicInfo() {
        return nome != null && cognome != null;
    }
}
