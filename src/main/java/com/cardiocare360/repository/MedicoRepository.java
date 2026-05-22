package com.cardiocare360.repository;

import com.cardiocare360.model.entity.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface MedicoRepository extends JpaRepository<Medico, Long> {

    Optional<Medico> findByEmail(String email);

    Optional<Medico> findById(Long id); // 🔥 utile per lookup diretto

    List<Medico> findBySpecializzazione(String specializzazione);

    List<Medico> findBySpecializzazioneIgnoreCase(String specializzazione);
}
