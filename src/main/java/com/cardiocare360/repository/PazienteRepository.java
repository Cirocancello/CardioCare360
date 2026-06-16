package com.cardiocare360.repository;

import com.cardiocare360.model.entity.Paziente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PazienteRepository extends JpaRepository<Paziente, Long> {

    Optional<Paziente> findByCodiceFiscale(String codiceFiscale);

    Optional<Paziente> findByEmail(String email);

    boolean existsByCodiceFiscale(String codiceFiscale);

    // lookup diretto per ereditarietà (id paziente = id utente)
    Optional<Paziente> findById(Long id);

    //  NECESSARIO PER IL MEDICO
    List<Paziente> findByMedico_Id(Long medicoId);
}
