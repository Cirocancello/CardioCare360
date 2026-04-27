package com.cardiocare360.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank
    private String nome;

    @NotBlank
    private String cognome;

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String ruolo; // PAZIENTE, MEDICO, ADMIN
}
