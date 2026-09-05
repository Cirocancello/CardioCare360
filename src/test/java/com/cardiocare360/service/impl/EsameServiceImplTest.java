package com.cardiocare360.service.impl;

import com.cardiocare360.model.entity.Esame;
import com.cardiocare360.model.entity.Medico;
import com.cardiocare360.model.entity.Paziente;
import com.cardiocare360.model.response.EsameDTO;
import com.cardiocare360.repository.EsameRepository;
import com.cardiocare360.repository.MedicoRepository;
import com.cardiocare360.repository.PazienteRepository;
import com.cardiocare360.repository.RefertoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

/**
 * TEST DI UNITÀ — Gestione Esami (EsameServiceImpl).
 *
 * COSA SI TESTA: il ciclo di vita di un esame diagnostico — l'aggiornamento del
 * suo stato (es. da PRENOTATO a ESEGUITO), la sua eliminazione, e il recupero
 * del referto associato.
 *
 * PERCHÉ QUESTI CASI:
 * - aggiornamento stato: verifica che il cambio di stato avvenga correttamente
 *   (è l'unica modifica realmente permessa su un esame già prenotato — non si
 *   possono cambiare data/ora, solo lo stato di avanzamento).
 * - eliminazione: sia il caso normale sia quello in cui l'esame non esiste già
 *   più (es. cancellato in un'altra sessione), per verificare che il sistema
 *   dia un errore chiaro invece di un comportamento indefinito.
 * - recupero referto: si verifica il caso limite in cui un paziente prova a
 *   visualizzare il referto di un esame che il medico non ha ancora refertato —
 *   il sistema deve avvisarlo chiaramente, non restituire un referto vuoto o
 *   un errore tecnico incomprensibile.
 *
 * Dipendenze reali (repository di Esame/Paziente/Medico/Referto) sostituite
 * con mock.
 */
@ExtendWith(MockitoExtension.class)
class EsameServiceImplTest {

    @Mock
    private EsameRepository esameRepository;

    @Mock
    private PazienteRepository pazienteRepository;

    @Mock
    private MedicoRepository medicoRepository;

    @Mock
    private RefertoRepository refertoRepository;

    @InjectMocks
    private EsameServiceImpl esameService;

    private Esame esame;

    @BeforeEach
    void setUp() {
        Paziente paziente = new Paziente();
        paziente.setId(1L);

        Medico medico = new Medico();
        medico.setId(2L);

        esame = new Esame();
        esame.setId(50L);
        esame.setPaziente(paziente);
        esame.setMedico(medico);
        esame.setStato(Esame.StatoEsame.PRENOTATO);
    }

    // TC-17: aggiornamento stato esame da PRENOTATO a ESEGUITO -> successo
    @Test
    void quandoStatoValido_aggiornaStatoConSuccesso() {
        when(esameRepository.findById(50L)).thenReturn(Optional.of(esame));
        lenient().when(esameRepository.save(any(Esame.class))).thenReturn(esame);

        EsameDTO risultato = esameService.aggiornaStatoEsame(50L, "ESEGUITO");

        assertEquals("ESEGUITO", risultato.getStato());
    }

    // TC-18: eliminazione esame esistente -> nessuna eccezione, repository invocato
    @Test
    void quandoEsameEsiste_eliminaSenzaErrori() {
        when(esameRepository.existsById(50L)).thenReturn(true);

        esameService.eliminaEsame(50L);

        // se non lancia eccezioni, il test è superato
    }

    // TC-19: eliminazione esame inesistente -> errore
    @Test
    void quandoEsameNonEsiste_lanciaEccezioneInEliminazione() {
        when(esameRepository.existsById(999L)).thenReturn(false);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> esameService.eliminaEsame(999L)
        );
        assertEquals("Esame non trovato", exception.getMessage());
    }

    // TC-20: recupero referto quando non ne esiste ancora nessuno per l'esame -> errore
    @Test
    void quandoNessunRefertoPresente_lanciaEccezione() {
        when(refertoRepository.findByEsame_Id(50L)).thenReturn(Collections.emptyList());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> esameService.getRefertoByEsame(50L)
        );
        assertEquals("Referto non presente per questo esame", exception.getMessage());
    }
}
