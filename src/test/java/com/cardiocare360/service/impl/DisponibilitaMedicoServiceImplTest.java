package com.cardiocare360.service.impl;

import com.cardiocare360.model.entity.DisponibilitaMedico;
import com.cardiocare360.model.entity.Medico;
import com.cardiocare360.repository.DisponibilitaMedicoRepository;
import com.cardiocare360.repository.MedicoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * TEST DI UNITÀ — Gestione Disponibilità Medico (DisponibilitaMedicoServiceImpl).
 *
 * COSA SI TESTA: la creazione di una nuova fascia oraria di disponibilità per un
 * medico, la modifica di una fascia esistente, e la consultazione delle
 * disponibilità di un medico in un giorno specifico.
 *
 * PERCHÉ QUESTI CASI: il caso più significativo è il controllo anti-duplicato
 * in fase di creazione — il sistema impedisce di inserire due volte la stessa
 * identica fascia oraria (stesso giorno, stessa ora di inizio e fine) per lo
 * stesso medico, evitando dati ridondanti che genererebbero confusione nella
 * prenotazione degli appuntamenti. Si verifica anche il caso in cui un paziente
 * chiede le disponibilità di un medico in un giorno in cui non ne ha nessuna,
 * per assicurarsi che il sistema comunichi l'assenza in modo chiaro.
 *
 * Dipendenze reali (DisponibilitaMedicoRepository, MedicoRepository) sostituite
 * con mock.
 */
@ExtendWith(MockitoExtension.class)
class DisponibilitaMedicoServiceImplTest {

    @Mock
    private DisponibilitaMedicoRepository disponibilitaRepo;

    @Mock
    private MedicoRepository medicoRepo;

    @InjectMocks
    private DisponibilitaMedicoServiceImpl disponibilitaService;

    private Medico medico;

    @BeforeEach
    void setUp() {
        medico = new Medico();
        medico.setId(2L);
    }

    // TC-24: creazione nuova fascia oraria, nessun duplicato -> successo
    @Test
    void quandoNessunDuplicato_creaDisponibilitaConSuccesso() {
        when(medicoRepo.findById(2L)).thenReturn(Optional.of(medico));
        when(disponibilitaRepo.findByMedicoIdAndGiornoSettimana(2L, "LUNEDI"))
                .thenReturn(Collections.emptyList());
        when(disponibilitaRepo.save(any(DisponibilitaMedico.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        DisponibilitaMedico risultato = disponibilitaService.creaDisponibilita(
                2L, "LUNEDI", "09:00", "13:00"
        );

        assertEquals(LocalTime.of(9, 0), risultato.getOraInizio());
        assertEquals(LocalTime.of(13, 0), risultato.getOraFine());
    }

    // TC-25: creazione di una fascia oraria identica a una già esistente -> errore anti-duplicato
    @Test
    void quandoFasciaOrariaDuplicata_lanciaEccezione() {
        DisponibilitaMedico esistente = new DisponibilitaMedico();
        esistente.setOraInizio(LocalTime.of(9, 0));
        esistente.setOraFine(LocalTime.of(13, 0));

        when(medicoRepo.findById(2L)).thenReturn(Optional.of(medico));
        when(disponibilitaRepo.findByMedicoIdAndGiornoSettimana(2L, "LUNEDI"))
                .thenReturn(List.of(esistente));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> disponibilitaService.creaDisponibilita(2L, "LUNEDI", "09:00", "13:00")
        );
        assertEquals("Questa fascia oraria è già presente per il medico in quel giorno", exception.getMessage());
    }

    // TC-26: modifica di una disponibilità esistente -> orari aggiornati correttamente
    @Test
    void quandoDisponibilitaEsiste_modificaConSuccesso() {
        DisponibilitaMedico esistente = new DisponibilitaMedico();
        esistente.setId(5L);
        esistente.setOraInizio(LocalTime.of(9, 0));
        esistente.setOraFine(LocalTime.of(13, 0));

        when(disponibilitaRepo.findById(5L)).thenReturn(Optional.of(esistente));
        when(disponibilitaRepo.save(any(DisponibilitaMedico.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        DisponibilitaMedico risultato = disponibilitaService.modificaDisponibilita(5L, "14:00", "18:00");

        assertEquals(LocalTime.of(14, 0), risultato.getOraInizio());
        assertEquals(LocalTime.of(18, 0), risultato.getOraFine());
    }

    // TC-27: nessuna disponibilità per il medico in quel giorno -> errore
    @Test
    void quandoNessunaDisponibilitaNelGiorno_lanciaEccezione() {
        when(disponibilitaRepo.findByMedicoIdAndGiornoSettimana(2L, "DOMENICA"))
                .thenReturn(Collections.emptyList());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> disponibilitaService.getDisponibilitaByMedicoAndGiorno(2L, "DOMENICA")
        );
        assertEquals("Nessuna disponibilità trovata per il medico in questo giorno", exception.getMessage());
    }
}
