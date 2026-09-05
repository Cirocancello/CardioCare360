package com.cardiocare360.service.impl;

import com.cardiocare360.model.request.RegisterRequest;
import com.cardiocare360.model.response.AuthResponse;
import com.cardiocare360.repository.MedicoRepository;
import com.cardiocare360.repository.PazienteRepository;
import com.cardiocare360.repository.UtenteRepository;
import com.cardiocare360.security.jwt.JwtUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

/**
 * TEST DI UNITÀ — Registrazione nuovo utente (AuthServiceImpl.register()).
 *
 * COSA SI TESTA: la creazione di un nuovo account Paziente, comprese le due
 * regole di unicità previste dal sistema — non possono esistere due account
 * con la stessa email, né due pazienti con lo stesso codice fiscale.
 *
 * PERCHÉ QUESTI CASI: la registrazione è il punto d'ingresso di tutto il
 * sistema, quindi le sue regole di unicità sono critiche — un bug qui
 * permetterebbe la creazione di account duplicati o l'furto di identità
 * di un paziente già registrato (registrandosi con il suo stesso codice
 * fiscale). Si verifica sia il percorso corretto sia entrambi i controlli
 * di duplicazione, che nel codice vengono eseguiti PRIMA di salvare
 * qualunque dato.
 *
 * Si usa il ruolo PAZIENTE perché è quello con più regole di validazione
 * specifiche (codice fiscale, data di nascita, ecc.), rendendolo il caso
 * più significativo da testare per questo metodo.
 * Dipendenze reali (repository, PasswordEncoder, JwtUtil) sostituite con mock.
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceImplRegisterTest {

    @Mock
    private UtenteRepository utenteRepository;

    @Mock
    private PazienteRepository pazienteRepository;

    @Mock
    private MedicoRepository medicoRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthServiceImpl authService;

    private RegisterRequest richiesta;

    @BeforeEach
    void setUp() {
        richiesta = new RegisterRequest();
        richiesta.setNome("Mario");
        richiesta.setCognome("Rossi");
        richiesta.setEmail("mario.rossi@example.com");
        richiesta.setPassword("password123");
        richiesta.setRuolo("PAZIENTE");
        richiesta.setCodiceFiscale("RSSMRA80A01H501X");
        richiesta.setLuogoNascita("Roma");
        richiesta.setDataNascita("1980-01-01");
        richiesta.setTelefono("3331234567");
        richiesta.setIndirizzo("Via Roma 1");
    }

    // TC-21: registrazione paziente con dati validi e nessun duplicato -> successo
    @Test
    void quandoDatiValidi_registrazionePazienteConSuccesso() {
        when(utenteRepository.existsByEmail("mario.rossi@example.com")).thenReturn(false);
        when(pazienteRepository.existsByCodiceFiscale("RSSMRA80A01H501X")).thenReturn(false);
        lenient().when(passwordEncoder.encode(any())).thenReturn("passwordCriptata");
        when(pazienteRepository.save(any())).thenAnswer(invocation -> {
            var paziente = invocation.getArgument(0, com.cardiocare360.model.entity.Paziente.class);
            paziente.setId(1L);
            return paziente;
        });
        lenient().when(jwtUtil.generateToken(any())).thenReturn("token-fittizio-di-test");

        AuthResponse response = authService.register(richiesta);

        assertEquals("PAZIENTE", response.getRuolo());
        assertEquals(1L, response.getIdPaziente());
    }

    // TC-22: email già registrata -> errore, nessuna creazione
    @Test
    void quandoEmailGiaRegistrata_lanciaEccezione() {
        when(utenteRepository.existsByEmail("mario.rossi@example.com")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> authService.register(richiesta)
        );
        assertEquals("EMAIL_DUPLICATA", exception.getMessage());
    }

    // TC-23: codice fiscale già associato a un altro paziente -> errore
    @Test
    void quandoCodiceFiscaleGiaRegistrato_lanciaEccezione() {
        when(utenteRepository.existsByEmail("mario.rossi@example.com")).thenReturn(false);
        when(pazienteRepository.existsByCodiceFiscale("RSSMRA80A01H501X")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> authService.register(richiesta)
        );
        assertEquals("CF_DUPLICATO", exception.getMessage());
    }
}
