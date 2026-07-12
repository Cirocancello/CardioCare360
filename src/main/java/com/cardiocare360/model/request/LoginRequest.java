package com.cardiocare360.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    // ---------------------------------------------------------
    // VALIDAZIONI DI SICUREZZA
    // ---------------------------------------------------------

    public boolean isValid() {
        return isEmailValida() && isPasswordValida();
    }

    public boolean isEmailValida() {
        return email != null && !email.isBlank();
    }

    public boolean isPasswordValida() {
        return password != null && !password.isBlank();
    }
}
