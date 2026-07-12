package com.cardiocare360.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/utente")
public class UtenteController {

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication auth) {
        try {
            if (auth == null || auth.getName() == null) {
                return ResponseEntity.status(401).body("NON_AUTENTICATO");
            }

            String username = auth.getName();
            return ResponseEntity.ok("Utente autenticato: " + username);

        } catch (Exception e) {
            System.err.println(">>> [UTENTE] Errore /me: " + e.getMessage());
            return ResponseEntity.status(500).body("ERRORE_SERVER");
        }
    }
}
