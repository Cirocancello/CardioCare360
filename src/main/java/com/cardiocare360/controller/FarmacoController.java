package com.cardiocare360.controller;

import com.cardiocare360.model.entity.Farmaco;
import com.cardiocare360.repository.FarmacoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/farmaci")
@RequiredArgsConstructor
public class FarmacoController {

    private final FarmacoRepository farmacoRepository;

    @GetMapping("/all")
    public List<Farmaco> getAllFarmaci() {
        return farmacoRepository.findAll();
    }
}
