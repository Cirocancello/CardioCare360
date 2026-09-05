package com.cardiocare360.service.impl;

import com.cardiocare360.model.entity.Appuntamento;
import com.cardiocare360.model.entity.Medico;
import com.cardiocare360.model.entity.Paziente;
import com.cardiocare360.model.response.AppuntamentoDTO;
import com.cardiocare360.repository.AppuntamentoRepository;
import com.cardiocare360.repository.MedicoRepository;
import com.cardiocare360.repository.PazienteRepository;
import com.cardiocare360.repository.UtenteRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

/**
 * TEST DI UNITÀ — Gestione Appuntamenti (AppuntamentoServiceImpl).
 *
 * COSA SI TESTA: la creazione di un nuovo appuntamento (con le sue regole di
 * validazione) e l'eliminazione di un appuntamento esistente (con il controllo
 * dei permessi di chi la richiede).
 *
 * PERCHÉ QUESTI CASI:
 * - creazione: si verifica sia il percorso di successo (dati validi, orario
 *   libero) sia una regola di business fondamentale, la data non può essere nel
 *   passato — altrimenti si potrebbero prenotare visite "a ritroso nel tempo".
 * - eliminazione: si verifica la regola di autorizzazione più delicata di questo
 *   modulo — solo il PAZIENTE PROPRIETARIO dell'appuntamento può eliminarlo.
 *   Si testa sia il caso corretto (il proprietario elimina) sia il tentativo
 *   di un utente diverso (deve essere negato), perché è qui che si annidano i
 *   bug di sicurezza più gravi: un controllo di autorizzazione dimenticato o
 *   scritto al contrario.
 *
 * Dipendenze reali (repository di Appuntamento/Paziente/Medico/Utente) sostituite
 * con mock, per testare solo la logica del Service senza toccare il database.
 */
@ExtendWith(MockitoExtension.class)
class AppuntamentoServiceImplTest {

    @Mock
    private AppuntamentoRepository appuntamentoRepository;

    @Mock
    private PazienteRepository pazienteRepository;

    @Mock
    private MedicoRepository medicoRepository;

    @Mock
    private UtenteRepository utenteRepository;

    @InjectMocks
    private AppuntamentoServiceImpl appuntamentoService;

    private Paziente paziente;
    private Medico medico;

    @BeforeEach
    void setUp() {
        paziente = new Paziente();
        paziente.setId(1L);

        medico = new Medico();
        medico.setId(2L);
    }

    // TC-10: creazione appuntamento con data futura e orario libero -> successo
    @Test
    void quandoDatiValidi_creaAppuntamentoConSuccesso() {
        AppuntamentoDTO dto = new AppuntamentoDTO();
        dto.setDataAppuntamento(LocalDate.now().plusDays(5));
        dto.setOraAppuntamento(LocalTime.of(10, 0));
        dto.setIdMedico(2L);
        dto.setTipoVisita("Visita cardiologica");

        when(pazienteRepository.findById(1L)).thenReturn(Optional.of(paziente));
        when(medicoRepository.findById(2L)).thenReturn(Optional.of(medico));
        lenient().when(appuntamentoRepository.existsByMedicoIdAndDataAppuntamentoAndOraAppuntamento(
                eq(2L), any(), any())).thenReturn(false);
        lenient().when(appuntamentoRepository.existsByPazienteIdAndDataAppuntamentoAndOraAppuntamento(
                eq(1L), any(), any())).thenReturn(false);
        when(appuntamentoRepository.save(any(Appuntamento.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        AppuntamentoDTO risultato = appuntamentoService.creaAppuntamento(dto, 1L);

        assertEquals("PRENOTATO", risultato.getStato());
    }

    // TC-11: creazione appuntamento con data nel passato -> errore
    @Test
    void quandoDataNelPassato_lanciaEccezione() {
        AppuntamentoDTO dto = new AppuntamentoDTO();
        dto.setDataAppuntamento(LocalDate.now().minusDays(1));
        dto.setOraAppuntamento(LocalTime.of(10, 0));
        dto.setIdMedico(2L);

        when(pazienteRepository.findById(1L)).thenReturn(Optional.of(paziente));
        when(medicoRepository.findById(2L)).thenReturn(Optional.of(medico));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> appuntamentoService.creaAppuntamento(dto, 1L)
        );
        assertEquals("La data deve essere futura", exception.getMessage());
    }

    // TC-12: eliminazione riuscita da parte del paziente proprietario, con data futura
    @Test
    void quandoPazienteProprietarioEDataFutura_eliminaConSuccesso() {
        Appuntamento app = new Appuntamento();
        app.setId(100L);
        app.setPaziente(paziente);
        app.setDataAppuntamento(LocalDate.now().plusDays(3));

        when(appuntamentoRepository.findById(100L)).thenReturn(Optional.of(app));

        boolean risultato = appuntamentoService.eliminaAppuntamento(100L, 1L);

        assertTrue(risultato);
    }

    // TC-13: eliminazione negata se l'utente non è il paziente proprietario
    @Test
    void quandoUtenteNonProprietario_negaEliminazione() {
        Appuntamento app = new Appuntamento();
        app.setId(100L);
        app.setPaziente(paziente); // proprietario id 1L
        app.setDataAppuntamento(LocalDate.now().plusDays(3));

        when(appuntamentoRepository.findById(100L)).thenReturn(Optional.of(app));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> appuntamentoService.eliminaAppuntamento(100L, 999L) // id utente diverso dal proprietario
        );
        assertEquals("Non hai i permessi per eliminare questo appuntamento", exception.getMessage());
    }
}
