package com.cardiocare360.model.request;

public class CambiaPasswordRequest {

    private String passwordAttuale;
    private String nuovaPassword;

    public String getPasswordAttuale() {
        return passwordAttuale;
    }

    public void setPasswordAttuale(String passwordAttuale) {
        this.passwordAttuale = passwordAttuale;
    }

    public String getNuovaPassword() {
        return nuovaPassword;
    }

    public void setNuovaPassword(String nuovaPassword) {
        this.nuovaPassword = nuovaPassword;
    }

    // ---------------------------------------------------------
    // VALIDAZIONI DI SICUREZZA
    // ---------------------------------------------------------
    public boolean isValid() {
        return passwordAttuale != null && !passwordAttuale.isBlank()
                && nuovaPassword != null && !nuovaPassword.isBlank();
    }

    public boolean isPasswordAttualeValida() {
        return passwordAttuale != null && !passwordAttuale.isBlank();
    }

    public boolean isNuovaPasswordValida() {
        return nuovaPassword != null && !nuovaPassword.isBlank();
    }
}
