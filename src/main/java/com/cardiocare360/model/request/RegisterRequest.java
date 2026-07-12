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

    // ---------------------------------------------------------
    // VALIDAZIONI DI SICUREZZA
    // ---------------------------------------------------------

    public boolean isValid() {
        return isNomeValido()
                && isCognomeValido()
                && isEmailValida()
                && isPasswordValida()
                && isRuoloValido()
                && isCampiSpecificiValidi();
    }

    // ---------------------------------------------------------
    // VALIDAZIONI COMUNI
    // ---------------------------------------------------------

    public boolean isNomeValido() {
        return nome != null && !nome.isBlank();
    }

    public boolean isCognomeValido() {
        return cognome != null && !cognome.isBlank();
    }

    public boolean isEmailValida() {
        return email != null && !email.isBlank();
    }

    public boolean isPasswordValida() {
        return password != null && !password.isBlank();
    }

    public boolean isRuoloValido() {
        return ruolo != null &&
                (ruolo.equalsIgnoreCase("PAZIENTE")
                || ruolo.equalsIgnoreCase("MEDICO")
                || ruolo.equalsIgnoreCase("ADMIN"));
    }

    // ---------------------------------------------------------
    // VALIDAZIONI SPECIFICHE PER RUOLO
    // ---------------------------------------------------------

    public boolean isCampiSpecificiValidi() {
        return switch (ruolo.toUpperCase()) {
            case "PAZIENTE" -> isCampiPazienteValidi();
            case "MEDICO" -> isCampiMedicoValidi();
            case "ADMIN" -> true; // admin non ha campi extra
            default -> false;
        };
    }

    public boolean isCampiPazienteValidi() {
        return codiceFiscale != null && !codiceFiscale.isBlank()
                && luogoNascita != null && !luogoNascita.isBlank()
                && dataNascita != null && !dataNascita.isBlank()
                && telefono != null && !telefono.isBlank()
                && indirizzo != null && !indirizzo.isBlank();
    }

    public boolean isCampiMedicoValidi() {
        return specializzazione != null && !specializzazione.isBlank()
                && numeroLicenza != null && !numeroLicenza.isBlank();
    }
}
