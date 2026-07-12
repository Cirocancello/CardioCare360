package com.cardiocare360.model.request;

public record CreaMedicoRequest(
        String nome,
        String cognome,
        String email,
        String password,
        String specializzazione,
        String telefono
) {

    // ---------------------------------------------------------
    // VALIDAZIONI DI SICUREZZA
    // ---------------------------------------------------------

    public boolean isValid() {
        return isNomeValido()
                && isCognomeValido()
                && isEmailValida()
                && isPasswordValida()
                && isSpecializzazioneValida();
    }

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

    public boolean isSpecializzazioneValida() {
        return specializzazione != null && !specializzazione.isBlank();
    }

    public boolean isTelefonoValido() {
        return telefono != null && !telefono.isBlank();
    }
}
