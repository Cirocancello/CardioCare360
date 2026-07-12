package com.cardiocare360.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    private String token;
    private String ruolo;
    private Long idUtente;
    private Long idPaziente;
    private Long idMedico;

    // ---------------------------------------------------------
    // VALIDAZIONI DI SICUREZZA
    // ---------------------------------------------------------

    public boolean isValid() {
        return isTokenValido()
                && isRuoloValido()
                && isIdUtenteValido()
                && isIdRuoloConsistente();
    }

    public boolean isTokenValido() {
        return token != null && !token.isBlank();
    }

    public boolean isRuoloValido() {
        return ruolo != null &&
                (ruolo.equalsIgnoreCase("PAZIENTE")
                || ruolo.equalsIgnoreCase("MEDICO")
                || ruolo.equalsIgnoreCase("ADMIN"));
    }

    public boolean isIdUtenteValido() {
        return idUtente != null && idUtente > 0;
    }

    // ---------------------------------------------------------
    // VALIDAZIONE SPECIFICA PER RUOLO
    // ---------------------------------------------------------

    public boolean isIdRuoloConsistente() {
        return switch (ruolo.toUpperCase()) {
            case "PAZIENTE" -> idPaziente != null && idPaziente > 0;
            case "MEDICO" -> idMedico != null && idMedico > 0;
            case "ADMIN" -> true; // admin non ha ID specifici
            default -> false;
        };
    }
}
