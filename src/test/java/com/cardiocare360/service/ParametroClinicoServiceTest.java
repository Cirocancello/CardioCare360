package com.cardiocare360.service;

import com.cardiocare360.model.entity.Parametro;
import com.cardiocare360.model.entity.ParametroClinico;
import com.cardiocare360.model.entity.Paziente;
import com.cardiocare360.model.entity.SogliaParametro;
import com.cardiocare360.repository.ParametroRepository;
import com.cardiocare360.repository.SogliaParametroRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

/**
 * TEST DI UNITÀ — Logica di generazione degli alert clinici (ParametroClinicoService).
 *
 * COSA SI TESTA: il metodo checkParametroFuoriSoglia() confronta il valore di un
 * parametro vitale appena inserito dal paziente con la soglia minima/massima
 * configurata per quel paziente, e restituisce un messaggio di alert se il valore
 * è fuori range, oppure null se è nella norma.
 *
 * PERCHÉ QUESTA TECNICA: si usa il "Boundary Value Analysis" (analisi dei valori
 * limite) — invece di provare valori a caso, si controlla il comportamento esatto
 * AI CONFINI della soglia (es. valore = minimo esatto, valore = minimo - 0.1),
 * perché è lì che si annidano più facilmente i bug (es. un operatore "<=" scritto
 * per errore come "<"). Si verifica anche il caso limite in cui il paziente non
 * ha ancora nessuna soglia configurata, per assicurarsi che il sistema non vada
 * in errore (NullPointerException) invece di gestire il caso in modo controllato.
 *
 * Soglia di riferimento usata in tutti i test: Pressione Sistolica tra 90.0 e 140.0 (mmHg).
 * Dipendenze reali (ParametroRepository, SogliaParametroRepository) sostituite con
 * dei mock (Mockito), per isolare la sola logica di calcolo dal database.
 */
@ExtendWith(MockitoExtension.class)
class ParametroClinicoServiceTest {

    @Mock
    private ParametroRepository parametroRepository;

    @Mock
    private SogliaParametroRepository sogliaParametroRepository;

    @InjectMocks
    private ParametroClinicoService service;

    private Paziente paziente;
    private Parametro parametroPressioneSistolica;
    private SogliaParametro sogliaPressioneSistolica;

    @BeforeEach
    void setUp() {
        paziente = new Paziente();
        paziente.setId(1L);

        parametroPressioneSistolica = new Parametro();
        parametroPressioneSistolica.setId(10);
        parametroPressioneSistolica.setTipo("PRESSIONE_SIS");

        sogliaPressioneSistolica = new SogliaParametro();
        sogliaPressioneSistolica.setValoreMin(90.0);
        sogliaPressioneSistolica.setValoreMax(140.0);
    }

    private ParametroClinico creaParametroPressioneSistolica(Double valore) {
        ParametroClinico p = new ParametroClinico();
        p.setPaziente(paziente);
        p.setTipo("PRESSIONE_SIS");
        p.setPressioneSistolica(valore);
        return p;
    }

    private void mockRepositoryConSoglia(SogliaParametro soglia) {
        when(parametroRepository.findByTipo("PRESSIONE_SIS")).thenReturn(parametroPressioneSistolica);
        when(sogliaParametroRepository.findByPazienteIdAndParametroId(1L, 10)).thenReturn(soglia);
    }

    // TC-1: valore ben dentro il range -> nessun alert
    @Test
    void quandoValoreDentroSoglia_nessunAlert() {
        mockRepositoryConSoglia(sogliaPressioneSistolica);

        ParametroClinico p = creaParametroPressioneSistolica(120.0);
        String alert = service.checkParametroFuoriSoglia(p);

        assertNull(alert, "Un valore ben dentro il range non deve generare alert");
    }

    // TC-2: valore esattamente al limite minimo -> nessun alert (confine incluso)
    @Test
    void quandoValoreEsattamenteAlLimiteMinimo_nessunAlert() {
        mockRepositoryConSoglia(sogliaPressioneSistolica);

        ParametroClinico p = creaParametroPressioneSistolica(90.0);
        String alert = service.checkParametroFuoriSoglia(p);

        assertNull(alert, "Un valore esattamente al limite minimo deve essere considerato nella norma");
    }

    // TC-3: valore appena sotto il minimo -> alert
    @Test
    void quandoValoreAppenaSottoIlMinimo_generaAlert() {
        mockRepositoryConSoglia(sogliaPressioneSistolica);

        ParametroClinico p = creaParametroPressioneSistolica(89.9);
        String alert = service.checkParametroFuoriSoglia(p);

        assertEquals("Pressione sistolica fuori soglia", alert);
    }

    // TC-4: valore esattamente al limite massimo -> nessun alert (confine incluso)
    @Test
    void quandoValoreEsattamenteAlLimiteMassimo_nessunAlert() {
        mockRepositoryConSoglia(sogliaPressioneSistolica);

        ParametroClinico p = creaParametroPressioneSistolica(140.0);
        String alert = service.checkParametroFuoriSoglia(p);

        assertNull(alert, "Un valore esattamente al limite massimo deve essere considerato nella norma");
    }

    // TC-5: valore appena sopra il massimo -> alert
    @Test
    void quandoValoreAppenaSopraIlMassimo_generaAlert() {
        mockRepositoryConSoglia(sogliaPressioneSistolica);

        ParametroClinico p = creaParametroPressioneSistolica(140.1);
        String alert = service.checkParametroFuoriSoglia(p);

        assertEquals("Pressione sistolica fuori soglia", alert);
    }

    // TC-6: nessuna soglia configurata per il paziente -> nessun alert (non deve esplodere)
    @Test
    void quandoNessunaSogliaConfigurata_nessunAlert() {
        mockRepositoryConSoglia(null);

        ParametroClinico p = creaParametroPressioneSistolica(999.0);
        String alert = service.checkParametroFuoriSoglia(p);

        assertNull(alert, "Se non esiste una soglia configurata, non deve essere generato nessun alert");
    }
}
