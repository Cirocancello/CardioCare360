package com.cardiocare360.model.request;
public record CreaMedicoRequest(
        String nome,
        String cognome,
        String email,
        String password,
        String specializzazione,
        String telefono
) {}
