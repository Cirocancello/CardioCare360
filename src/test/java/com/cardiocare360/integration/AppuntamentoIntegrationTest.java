package com.cardiocare360.integration;

import com.cardiocare360.repository.AppuntamentoRepository;
import com.cardiocare360.repository.MedicoRepository;
import com.cardiocare360.repository.PazienteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * TEST DI INTEGRAZIONE — Flusso reale Registrazione \u2192 Login \u2192 Creazione Appuntamento.
 *
 * COSA SI TESTA: a differenza dei test unitari (dove le dipendenze sono mock), qui
 * si avvia l'intera applicazione Spring Boot (Controller + Service + Repository +
 * Security + database reale H2 in memoria) e si eseguono vere chiamate HTTP tramite
 * MockMvc. Si verifica che i componenti lavorino correttamente INSIEME: la
 * registrazione salva davvero un utente nel database, il login genera un token JWT
 * autentico, e quel token permette davvero di accedere a un endpoint protetto e
 * creare un appuntamento che viene realmente persistito.
 *
 * PERCHÉ QUESTO LIVELLO DI TEST: i test unitari garantiscono che ogni singolo
 * Service funzioni bene isolato, ma non dimostrano che l'intera catena
 * (autenticazione JWT inclusa) funzioni quando i componenti sono collegati per
 * davvero. Questo test copre esattamente quel rischio.
 *
 * Ambiente: profilo "test" (application-test.properties), database H2 in memoria,
 * ricreato da zero a ogni esecuzione. @Transactional annulla le modifiche al DB
 * dopo ogni singolo test, mantenendo i test indipendenti tra loro.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class AppuntamentoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PazienteRepository pazienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private AppuntamentoRepository appuntamentoRepository;

    private String codiceFiscaleUnico(String email) {
        // Un vero codice fiscale italiano ha sempre 16 caratteri: qui teniamo un
        // prefisso fisso di 12 caratteri validi + 4 cifre derivate dall'email,
        // per avere un valore sempre unico ma della lunghezza corretta.
        int numero = Math.abs(email.hashCode()) % 10000;
        return String.format("RSSMRA80A01H%04d", numero);
    }

    private String registraELoggaPaziente(String email) throws Exception {
        Map<String, Object> registrazione = new HashMap<>();
        registrazione.put("nome", "Mario");
        registrazione.put("cognome", "Rossi");
        registrazione.put("email", email);
        registrazione.put("password", "password123");
        registrazione.put("codiceFiscale", codiceFiscaleUnico(email));
        registrazione.put("luogoNascita", "Roma");
        registrazione.put("dataNascita", "1980-01-01");
        registrazione.put("telefono", "3331234567");
        registrazione.put("indirizzo", "Via Roma 1");

        mockMvc.perform(post("/auth/register-paziente")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrazione)))
                .andExpect(status().isOk());

        Map<String, String> login = new HashMap<>();
        login.put("email", email);
        login.put("password", "password123");

        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andReturn();

        Map<?, ?> response = objectMapper.readValue(loginResult.getResponse().getContentAsString(), Map.class);
        return (String) response.get("token");
    }

    // TI-01: registrazione reale, dati effettivamente salvati nel database H2
    @Test
    void quandoRegistrazioneValida_utenteSalvatoDavveroNelDatabase() throws Exception {
        String email = "integrazione.paziente1@test.it";
        registraELoggaPaziente(email);

        boolean esisteNelDb = pazienteRepository.existsByCodiceFiscale(codiceFiscaleUnico(email));
        assertTrue(esisteNelDb, "Il paziente deve essere realmente presente nel database dopo la registrazione");
    }

    // TI-02: login riuscito genera un token JWT autentico e riutilizzabile
    @Test
    void quandoLoginRiuscito_tokenJwtValidoPerAccedereAEndpointProtetto() throws Exception {
        String email = "integrazione.paziente2@test.it";
        String token = registraELoggaPaziente(email);

        assertTrue(token != null && token.length() > 20, "Il token JWT deve essere una stringa non vuota");

        // Uso il token vero per accedere a un endpoint protetto
        mockMvc.perform(get("/appuntamenti/paziente")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    // TI-03: senza token, l'endpoint protetto nega l'accesso
    @Test
    void quandoNessunToken_accessoNegatoAEndpointProtetto() throws Exception {
        mockMvc.perform(get("/appuntamenti/paziente"))
                .andExpect(status().isUnauthorized());
    }

    // TI-04: creazione di un appuntamento tramite API reale, verificata sul database
    @Test
    void quandoCreazioneAppuntamentoTramiteApi_datoRealmentePersistito() throws Exception {
        // Il medico non si auto-registra (nel sistema reale lo crea l'Admin):
        // lo inserisco direttamente nel database H2 come dato di supporto al test.
        com.cardiocare360.model.entity.Medico medico = new com.cardiocare360.model.entity.Medico();
        medico.setNome("Luca");
        medico.setCognome("Bianchi");
        medico.setEmail("integrazione.medico1@test.it");
        medico.setPassword("passwordCriptataFittizia");
        medico.setRuolo(com.cardiocare360.model.entity.Utente.Ruolo.MEDICO);
        medico.setSpecializzazione("Cardiologia");
        medico = medicoRepository.save(medico);
        Long idMedico = medico.getId();

        String token = registraELoggaPaziente("integrazione.paziente3@test.it");

        Map<String, Object> nuovoAppuntamento = new HashMap<>();
        nuovoAppuntamento.put("dataAppuntamento", LocalDate.now().plusDays(5).toString());
        nuovoAppuntamento.put("oraAppuntamento", "10:00");
        nuovoAppuntamento.put("idMedico", idMedico);
        nuovoAppuntamento.put("tipoVisita", "Visita cardiologica");

        mockMvc.perform(post("/appuntamenti")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuovoAppuntamento)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stato").value("PRENOTATO"));

        assertEquals(1, appuntamentoRepository.findByMedicoId(idMedico).size(),
                "L'appuntamento deve essere realmente presente nel database associato al medico");
    }
}
