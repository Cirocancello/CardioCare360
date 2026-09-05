package com.cardiocare360.service.impl;

import com.cardiocare360.model.entity.Utente;
import com.cardiocare360.model.request.LoginRequest;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.lenient;

/**
 * TEST DI UNITÀ — Autenticazione (AuthServiceImpl.login()).
 *
 * COSA SI TESTA: il processo di login: ricerca dell'utente per email, verifica
 * della password (tramite PasswordEncoder, che confronta la password in chiaro
 * inserita con quella criptata salvata nel database) e generazione del token JWT
 * in caso di successo.
 *
 * PERCHÉ QUESTI CASI: oltre al caso di successo, si verificano i due scenari di
 * fallimento più comuni per un login (email inesistente, password sbagliata) e
 * si controlla che il sistema restituisca in ENTRAMBI i casi lo stesso messaggio
 * generico "CREDENZIALI_ERRATE" — una buona pratica di sicurezza: se il sistema
 * dicesse "email non trovata" vs "password sbagliata" darebbe a un aggressore
 * indicazioni su quali email sono davvero registrate nel sistema.
 *
 * Si testa il ruolo ADMIN perché è il percorso di login più semplice (per Medico
 * e Paziente il metodo login() esegue controlli aggiuntivi su altre tabelle, non
 * rilevanti per verificare la logica di autenticazione in sé).
 * Dipendenze reali (repository, PasswordEncoder, JwtUtil) sostituite con mock.
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

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

    private LoginRequest loginRequest;
    private Utente utenteAdmin;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest();
        loginRequest.setEmail("admin@cardiocare360.it");
        loginRequest.setPassword("password123");

        utenteAdmin = new Utente();
        utenteAdmin.setId(1L);
        utenteAdmin.setEmail("admin@cardiocare360.it");
        utenteAdmin.setPassword("passwordCriptata");
        utenteAdmin.setRuolo(Utente.Ruolo.ADMIN);
    }

    // TC-7: login riuscito con credenziali valide (ruolo ADMIN)
    @Test
    void quandoCredenzialiValide_loginRiuscito() {
        when(utenteRepository.findByEmail("admin@cardiocare360.it")).thenReturn(Optional.of(utenteAdmin));
        when(passwordEncoder.matches("password123", "passwordCriptata")).thenReturn(true);
        lenient().when(jwtUtil.generateToken(org.mockito.ArgumentMatchers.any())).thenReturn("token-fittizio-di-test");

        AuthResponse response = authService.login(loginRequest);

        assertEquals("ADMIN", response.getRuolo());
        assertEquals(1L, response.getIdUtente());
    }

    // TC-8: utente non trovato -> credenziali errate
    @Test
    void quandoUtenteNonTrovato_credenzialiErrate() {
        when(utenteRepository.findByEmail("inesistente@cardiocare360.it")).thenReturn(Optional.empty());

        LoginRequest richiestaInesistente = new LoginRequest();
        richiestaInesistente.setEmail("inesistente@cardiocare360.it");
        richiestaInesistente.setPassword("qualsiasi");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> authService.login(richiestaInesistente)
        );
        assertEquals("CREDENZIALI_ERRATE", exception.getMessage());
    }

    // TC-9: password errata -> credenziali errate
    @Test
    void quandoPasswordErrata_credenzialiErrate() {
        when(utenteRepository.findByEmail("admin@cardiocare360.it")).thenReturn(Optional.of(utenteAdmin));
        when(passwordEncoder.matches("passwordSbagliata", "passwordCriptata")).thenReturn(false);

        LoginRequest richiestaPasswordErrata = new LoginRequest();
        richiestaPasswordErrata.setEmail("admin@cardiocare360.it");
        richiestaPasswordErrata.setPassword("passwordSbagliata");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> authService.login(richiestaPasswordErrata)
        );
        assertEquals("CREDENZIALI_ERRATE", exception.getMessage());
    }
}
