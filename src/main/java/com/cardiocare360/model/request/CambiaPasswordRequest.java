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
}
