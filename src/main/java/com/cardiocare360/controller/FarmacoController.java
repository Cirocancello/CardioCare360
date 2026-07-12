package com.cardiocare360.controller;

import com.cardiocare360.model.entity.Farmaco;
import com.cardiocare360.repository.FarmacoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/farmaci")
@RequiredArgsConstructor
public class FarmacoController {

    private final FarmacoRepository farmacoRepository;

    @GetMapping("/all")
    public ResponseEntity<?> getAllFarmaci() {
        try {
            List<Farmaco> farmaci = farmacoRepository.findAll();

            if (farmaci == null || farmaci.isEmpty()) {
                return ResponseEntity.ok(List.of()); // lista vuota, nessun errore
            }

            return ResponseEntity.ok(farmaci);

        } catch (Exception e) {
            System.err.println(">>> [FARMACI] Errore inatteso: " + e.getMessage());
            return ResponseEntity.status(500).body("ERRORE_SERVER");
        }
    }
}
