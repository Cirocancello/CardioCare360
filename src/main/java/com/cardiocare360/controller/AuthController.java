package com.cardiocare360.controller;

import com.cardiocare360.model.request.LoginRequest;
import com.cardiocare360.model.request.RegisterRequest;
import com.cardiocare360.model.response.AuthResponse;
import com.cardiocare360.repository.AppuntamentoRepository;
import com.cardiocare360.repository.MedicoRepository;
import com.cardiocare360.repository.PazienteRepository;
import com.cardiocare360.service.AuthService;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    
    private final MedicoRepository medicoRepository;
    private final PazienteRepository pazienteRepository;
    private final AppuntamentoRepository appuntamentoRepository;

    // 🔥 Registrazione utente (ADMIN → crea PAZIENTE o MEDICO)
    @PostMapping("/register-paziente")
    public ResponseEntity<?> registerPaziente(@RequestBody RegisterRequest request) {

        System.out.println(">>> [AUTH] Registrazione paziente");

        request.setRuolo("PAZIENTE"); // forza il ruolo

        try {
            AuthResponse response = authService.register(request);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("ERRORE_SERVER");
        }
    }


    // 🔥 Login utente
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        System.out.println(">>> [AUTH] Richiesta login ricevuta per: " + request.getEmail());
        try {
            AuthResponse response = authService.login(request);
            System.out.println(">>> [AUTH] Login completato, ritorno 200");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println(">>> [AUTH] Errore durante il login: " + e.getMessage());
            return ResponseEntity.status(401).build(); // ⭐ restituisce 401 se credenziali errate
        }
    }

    // 🔥 Recupero password
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> body) {

        String email = body.get("email");
        System.out.println(">>> [AUTH] Richiesta recupero password per: " + email);

        if (email == null || email.isBlank()) {
            return ResponseEntity.status(400).body("EMAIL_NON_VALIDA");
        }

        // Qui potresti controllare se l'email esiste nel DB
        // boolean exists = utenteRepository.existsByEmail(email);
        // if (!exists) return ResponseEntity.status(404).body("EMAIL_NON_TROVATA");

        System.out.println(">>> [AUTH] Email di recupero inviata a: " + email);
        return ResponseEntity.ok("EMAIL_INVIATA");
    }
    
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("medici", medicoRepository.count());
        stats.put("pazienti", pazienteRepository.count());
        stats.put("appuntamenti", appuntamentoRepository.count());
        return ResponseEntity.ok(stats);
    }

}
