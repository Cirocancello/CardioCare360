package com.cardiocare360.service.impl;

import com.cardiocare360.model.entity.Medico;
import com.cardiocare360.model.entity.Paziente;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

/**
 * TEST DI UNITÀ — Category-Partition Testing su TerapiaServiceImpl.creaTerapia().
 *
 * COSA SI TESTA: creaTerapia() ha 4 parametri che rappresentano riferimenti a
 * entità correlate (paziente, medico, farmaco, appuntamento), ciascuno dei quali
 * può esistere o non esistere nel sistema — le categorie di questa tecnica. Il
 * metodo del codice reale verifica questi riferimenti in un ordine preciso:
 * prima l'anti-duplicato sull'appuntamento, poi paziente, poi medico, poi
 * farmaco, infine l'appuntamento stesso.
 *
 * PERCHÉ QUESTI CASI: i test unitari già esistenti (TerapiaServiceImplTest)
 * coprono il caso di successo, l'anti-duplicato e il paziente non trovato.
 * Questi test coprono le rimanenti combinazioni di categorie non ancora
 * verificate — medico assente, farmaco assente, appuntamento assente — per
 * completare sistematicamente lo spazio dei casi rilevanti, invece di
 * verificare solo alcuni scenari a scelta.
 *
 * Dipendenze reali (tutti i Repository coinvolti) sostituite con mock.
 */
@ExtendWith(MockitoExtension.class)
class TerapiaCategoryPartitionTest {

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

    @BeforeEach
    void setUp() {
        paziente = new Paziente();
        paziente.setId(1L);

        medico = new Medico();
        medico.setId(2L);
    }

    // CP-01: categoria Medico = non trovato (paziente esiste, appuntamento libero)
    @Test
    void quandoMedicoNonTrovato_lanciaEccezione() {
        when(terapiaRepository.existsByAppuntamento_Id(4L)).thenReturn(false);
        when(pazienteRepository.findById(1L)).thenReturn(Optional.of(paziente));
        when(medicoRepository.findById(2L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> terapiaService.creaTerapia(1L, 2L, 3L, 4L, "500mg", "note", "2026-09-10", null)
        );
        assertEquals("Medico non trovato", exception.getMessage());
    }

    // CP-02: categoria Farmaco = non trovato (paziente e medico esistono)
    @Test
    void quandoFarmacoNonTrovato_lanciaEccezione() {
        when(terapiaRepository.existsByAppuntamento_Id(4L)).thenReturn(false);
        when(pazienteRepository.findById(1L)).thenReturn(Optional.of(paziente));
        when(medicoRepository.findById(2L)).thenReturn(Optional.of(medico));
        when(farmacoRepository.findById(3L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> terapiaService.creaTerapia(1L, 2L, 3L, 4L, "500mg", "note", "2026-09-10", null)
        );
        assertEquals("Farmaco non trovato", exception.getMessage());
    }

    // CP-03: categoria Appuntamento = non trovato (paziente, medico e farmaco esistono)
    @Test
    void quandoAppuntamentoNonTrovato_lanciaEccezione() {
        com.cardiocare360.model.entity.Farmaco farmaco = new com.cardiocare360.model.entity.Farmaco();
        farmaco.setId(3L);

        when(terapiaRepository.existsByAppuntamento_Id(4L)).thenReturn(false);
        when(pazienteRepository.findById(1L)).thenReturn(Optional.of(paziente));
        when(medicoRepository.findById(2L)).thenReturn(Optional.of(medico));
        when(farmacoRepository.findById(3L)).thenReturn(Optional.of(farmaco));
        when(appuntamentoRepository.findById(4L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> terapiaService.creaTerapia(1L, 2L, 3L, 4L, "500mg", "note", "2026-09-10", null)
        );
        assertEquals("Appuntamento non trovato", exception.getMessage());
    }
}
