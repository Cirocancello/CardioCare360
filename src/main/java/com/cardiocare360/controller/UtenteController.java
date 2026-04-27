package com.cardiocare360.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/utente")
public class UtenteController {

    @GetMapping("/me")
    public String me(Authentication auth) {
        return "Utente autenticato: " + auth.getName();
    }
}
