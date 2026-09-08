package com.cardiocare360.service.impl;

import com.cardiocare360.model.entity.Appuntamento;
import com.cardiocare360.model.entity.Medico;
import com.cardiocare360.model.entity.Paziente;
import com.cardiocare360.model.response.AppuntamentoDTO;
import com.cardiocare360.repository.AppuntamentoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * TEST DI UNITÀ — State-based Testing sulle transizioni di stato di Appuntamento
 * (AppuntamentoServiceImpl.aggiornaStato()).
 *
 * COSA SI TESTA: lo statechart di Appuntamento definito nel RAD prevede un ciclo
 * di vita con transizioni specifiche (PRENOTATO → CONFERMATO → COMPLETATO, con
 * possibilità di ANNULLATO da PRENOTATO o CONFERMATO). Questa tecnica costruisce
 * la macchina a stati finiti dell'entità (stati = valori dell'enum
 * StatoAppuntamento, transizioni = chiamate ad aggiornaStato()) e verifica il
 * comportamento del sistema lungo cammini specifici di quella macchina, non solo
 * un singolo cambio di stato isolato.
 *
 * PERCHÉ QUESTI CASI: oltre a verificare le transizioni "in avanti" previste dallo
 * statechart, si verifica deliberatamente anche un caso non previsto (una
 * transizione "all'indietro", da COMPLETATO a PRENOTATO) per accertare se il
 * sistema rispetta davvero il ciclo di vita documentato nel RAD, o se il metodo
 * accetta qualunque stato senza convalidare la transizione.
 *
 * Dipendenza reale (AppuntamentoRepository) sostituita con mock.
 */
@ExtendWith(MockitoExtension.class)
class AppuntamentoStateBasedTest {

    @Mock
    private AppuntamentoRepository appuntamentoRepository;

    @InjectMocks
    private AppuntamentoServiceImpl appuntamentoService;

    private Appuntamento appuntamento;
    private Paziente paziente;
    private Medico medico;

    @BeforeEach
    void setUp() {
        paziente = new Paziente();
        paziente.setId(1L);

        medico = new Medico();
        medico.setId(2L);

        appuntamento = new Appuntamento();
        appuntamento.setId(100L);
        appuntamento.setPaziente(paziente);
        appuntamento.setMedico(medico);
        appuntamento.setStato(Appuntamento.StatoAppuntamento.PRENOTATO);
    }

    private void mockTrovaEsalva() {
        when(appuntamentoRepository.findById(100L)).thenReturn(Optional.of(appuntamento));
        when(appuntamentoRepository.save(any(Appuntamento.class))).thenAnswer(inv -> inv.getArgument(0));
    }

    // TS_STATE-01: transizione valida PRENOTATO -> CONFERMATO (richiesta dal medico)
    @Test
    void quandoPrenotato_transizioneAConfermatoValida() {
        mockTrovaEsalva();

        AppuntamentoDTO risultato = appuntamentoService.aggiornaStato(100L, "CONFERMATO", 2L);

        assertEquals("CONFERMATO", risultato.getStato());
    }

    // TS_STATE-02: transizione valida CONFERMATO -> COMPLETATO
    @Test
    void quandoConfermato_transizioneACompletatoValida() {
        appuntamento.setStato(Appuntamento.StatoAppuntamento.CONFERMATO);
        mockTrovaEsalva();

        AppuntamentoDTO risultato = appuntamentoService.aggiornaStato(100L, "COMPLETATO", 2L);

        assertEquals("COMPLETATO", risultato.getStato());
    }

    // TS_STATE-03: transizione valida PRENOTATO -> ANNULLATO (richiesta dal paziente)
    @Test
    void quandoPrenotato_transizioneAAnnullatoValida() {
        mockTrovaEsalva();

        AppuntamentoDTO risultato = appuntamentoService.aggiornaStato(100L, "ANNULLATO", 1L);

        assertEquals("ANNULLATO", risultato.getStato());
    }

    // TS_STATE-04: transizione NON prevista dallo statechart (COMPLETATO -> PRENOTATO,
    // "all'indietro") — verifica se il sistema la impedisce o la accetta comunque.
    @Test
    void quandoCompletato_transizioneAllIndietroNonValidataDalSistema() {
        appuntamento.setStato(Appuntamento.StatoAppuntamento.COMPLETATO);
        mockTrovaEsalva();

        // Il metodo non lancia eccezioni per transizioni non previste dallo
        // statechart: accetta qualunque valore valido dell'enum, indipendentemente
        // dallo stato di partenza. Questo test documenta il comportamento reale.
        AppuntamentoDTO risultato = appuntamentoService.aggiornaStato(100L, "PRENOTATO", 2L);

        assertEquals("PRENOTATO", risultato.getStato());
    }

    // TS_STATE-05: nessun permesso, utente non associato all'appuntamento
    @Test
    void quandoUtenteNonAssociato_transizioneNegata() {
        when(appuntamentoRepository.findById(100L)).thenReturn(Optional.of(appuntamento));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> appuntamentoService.aggiornaStato(100L, "CONFERMATO", 999L)
        );
        assertEquals("Non hai i permessi", exception.getMessage());
    }
}
