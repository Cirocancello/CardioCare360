package com.cardiocare360.service.impl;

import com.cardiocare360.model.entity.Appuntamento;
import com.cardiocare360.model.entity.Farmaco;
import com.cardiocare360.model.entity.Medico;
import com.cardiocare360.model.entity.Paziente;
import com.cardiocare360.model.entity.Terapia;
import com.cardiocare360.repository.AppuntamentoRepository;
import com.cardiocare360.repository.FarmacoRepository;
import com.cardiocare360.repository.MedicoRepository;
import com.cardiocare360.repository.PazienteRepository;
import com.cardiocare360.repository.TerapiaRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * TEST DI UNITÀ — Gestione Terapie (TerapiaServiceImpl).
 *
 * COSA SI TESTA: la creazione di una nuova terapia farmacologica assegnata da un
 * medico a un paziente, collegata a un farmaco e a un appuntamento specifico.
 *
 * PERCHÉ QUESTI CASI: il caso più interessante da testare qui è la REGOLA
 * ANTI-DUPLICATO implementata nel codice — un singolo appuntamento non può avere
 * più di una terapia associata (il sistema controlla questo PRIMA di validare
 * qualunque altro dato, per bloccare subito la richiesta). Si verifica anche il
 * comportamento quando uno degli oggetti collegati (in questo caso il paziente)
 * non esiste nel sistema, per assicurarsi che l'errore sia gestito con un
 * messaggio chiaro invece di un errore generico o un crash.
 *
 * Dipendenze reali (repository di Terapia/Paziente/Medico/Farmaco/Appuntamento)
 * sostituite con mock.
 */
@ExtendWith(MockitoExtension.class)
class TerapiaServiceImplTest {

    @Mock
    private TerapiaRepository terapiaRepository;

    @Mock
    private PazienteRepository pazienteRepository;

    @Mock
    private MedicoRepository medicoRepository;

    @Mock
    private FarmacoRepository farmacoRepository;

    @Mock
    private AppuntamentoRepository appuntamentoRepository;

    @InjectMocks
    private TerapiaServiceImpl terapiaService;

    private Paziente paziente;
    private Medico medico;
    private Farmaco farmaco;
    private Appuntamento appuntamento;

    @BeforeEach
    void setUp() {
        paziente = new Paziente();
        paziente.setId(1L);

        medico = new Medico();
        medico.setId(2L);

        farmaco = new Farmaco();
        farmaco.setId(3L);

        appuntamento = new Appuntamento();
        appuntamento.setId(4L);
    }

    // TC-14: creazione terapia con dati validi e appuntamento libero -> successo
    @Test
    void quandoDatiValidiEAppuntamentoLibero_creaTerapiaConSuccesso() {
        when(terapiaRepository.existsByAppuntamento_Id(4L)).thenReturn(false);
        when(pazienteRepository.findById(1L)).thenReturn(Optional.of(paziente));
        when(medicoRepository.findById(2L)).thenReturn(Optional.of(medico));
        when(farmacoRepository.findById(3L)).thenReturn(Optional.of(farmaco));
        when(appuntamentoRepository.findById(4L)).thenReturn(Optional.of(appuntamento));
        when(terapiaRepository.save(any(Terapia.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Terapia risultato = terapiaService.creaTerapia(
                1L, 2L, 3L, 4L, "500mg 2 volte al giorno", "Dopo i pasti", "2026-09-10", null
        );

        assertNotNull(risultato);
        assertEquals("500mg 2 volte al giorno", risultato.getDosaggio());
    }

    // TC-15: appuntamento che ha già una terapia associata -> errore anti-duplicato
    @Test
    void quandoAppuntamentoHaGiaUnaTerapia_lanciaEccezione() {
        when(terapiaRepository.existsByAppuntamento_Id(4L)).thenReturn(true);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> terapiaService.creaTerapia(1L, 2L, 3L, 4L, "500mg", "note", "2026-09-10", null)
        );
        assertEquals("Esiste già una terapia per questo appuntamento.", exception.getMessage());
    }

    // TC-16: paziente non trovato -> errore
    @Test
    void quandoPazienteNonTrovato_lanciaEccezione() {
        when(terapiaRepository.existsByAppuntamento_Id(4L)).thenReturn(false);
        when(pazienteRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> terapiaService.creaTerapia(1L, 2L, 3L, 4L, "500mg", "note", "2026-09-10", null)
        );
        assertEquals("Paziente non trovato", exception.getMessage());
    }
}
