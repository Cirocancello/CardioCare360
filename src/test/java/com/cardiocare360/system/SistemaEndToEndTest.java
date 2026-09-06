package com.cardiocare360.system;

import com.cardiocare360.model.entity.Parametro;
import com.cardiocare360.model.entity.Paziente;
import com.cardiocare360.model.entity.SogliaParametro;
import com.cardiocare360.repository.ParametroRepository;
import com.cardiocare360.repository.SogliaParametroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TEST DI SISTEMA — Flusso end-to-end tramite chiamate HTTP reali su server avviato.
 *
 * COSA SI TESTA: a differenza dei test di integrazione (che usano MockMvc, una
 * simulazione delle richieste HTTP senza un vero server in ascolto), questi test
 * avviano l'applicazione Spring Boot su una porta reale (webEnvironment =
 * RANDOM_PORT) e usano TestRestTemplate per inviare vere richieste HTTP, esattamente
 * come farebbe un client esterno (es. il frontend React). È il livello più vicino
 * al comportamento reale del sistema, senza però passare dal browser/interfaccia
 * grafica.
 *
 * PERCHÉ QUESTI SCENARI: si verifica il flusso end-to-end più rappresentativo del
 * progetto — l'inserimento di un parametro vitale e la generazione automatica di
 * un alert clinico — dalla richiesta HTTP fino alla risposta, passando per
 * autenticazione JWT, logica di business e persistenza reale. Si verifica inoltre
 * che le regole di autorizzazione basate sul ruolo siano rispettate anche a questo
 * livello (un Paziente non può accedere a un endpoint riservato al Medico).
 *
 * Ambiente: profilo "test" (application-test.properties), database H2 in memoria.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class SistemaEndToEndTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ParametroRepository parametroRepository;

    @Autowired
    private SogliaParametroRepository sogliaParametroRepository;

    private String baseUrl() {
        return "http://localhost:" + port;
    }

    private String codiceFiscaleUnico(String email) {
        int numero = Math.abs(email.hashCode()) % 10000;
        return String.format("RSSMRA80A01H%04d", numero);
    }

    private Map<String, Object> registraELoggaPaziente(String email) {
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

        restTemplate.postForEntity(baseUrl() + "/auth/register-paziente", registrazione, Map.class);

        Map<String, String> login = new HashMap<>();
        login.put("email", email);
        login.put("password", "password123");

        ResponseEntity<Map> loginResponse = restTemplate.postForEntity(
                baseUrl() + "/auth/login", login, Map.class);

        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        return loginResponse.getBody();
    }

    private HttpHeaders headerConToken(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return headers;
    }

    private Parametro ottieniOCreaParametroPressioneSistolica() {
        // I test TS-01 e TS-02 condividono lo stesso contesto Spring (e quindi lo
        // stesso database H2): senza questo controllo, il secondo test creerebbe un
        // secondo "Parametro" con lo stesso tipo, violando l'assunzione implicita del
        // codice applicativo che findByTipo() restituisca sempre un risultato unico.
        Parametro esistente = parametroRepository.findByTipo("PRESSIONE_SIS");
        if (esistente != null) {
            return esistente;
        }
        Parametro parametro = new Parametro();
        parametro.setNome("Pressione sistolica");
        parametro.setTipo("PRESSIONE_SIS");
        parametro.setUnitaMisura("mmHg");
        return parametroRepository.save(parametro);
    }

    // TS-01: inserimento parametro fuori soglia -> alert generato, end-to-end reale
    @Test
    void quandoParametroFuoriSoglia_alertGeneratoEndToEnd() {
        Map<String, Object> auth = registraELoggaPaziente("sistema.paziente1@test.it");
        String token = (String) auth.get("token");
        Integer idPaziente = (Integer) auth.get("idPaziente");

        // Configuro la soglia direttamente sul DB (non esiste un endpoint per farlo,
        // coerente con quanto documentato: le soglie sono dati di configurazione)
        Parametro parametro = ottieniOCreaParametroPressioneSistolica();

        SogliaParametro soglia = new SogliaParametro();
        soglia.setParametro(parametro);
        Paziente paziente = new Paziente();
        paziente.setId(idPaziente.longValue());
        soglia.setPaziente(paziente);
        soglia.setValoreMin(90.0);
        soglia.setValoreMax(140.0);
        sogliaParametroRepository.save(soglia);

        Map<String, Object> nuovoParametro = new HashMap<>();
        nuovoParametro.put("tipo", "PRESSIONE_SIS");
        nuovoParametro.put("pressioneSistolica", 170.0);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(nuovoParametro, headerConToken(token));
        ResponseEntity<Map> response = restTemplate.postForEntity(
                baseUrl() + "/api/pazienti/" + idPaziente + "/parametri", request, Map.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().get("alert"),
                "Un valore di 170 (soglia 90-140) deve generare un alert nella risposta reale del sistema");
    }

    // TS-02: inserimento parametro nella norma -> nessun alert, end-to-end reale
    @Test
    void quandoParametroNellaNorma_nessunAlertEndToEnd() {
        Map<String, Object> auth = registraELoggaPaziente("sistema.paziente2@test.it");
        String token = (String) auth.get("token");
        Integer idPaziente = (Integer) auth.get("idPaziente");

        Parametro parametro = ottieniOCreaParametroPressioneSistolica();

        SogliaParametro soglia = new SogliaParametro();
        soglia.setParametro(parametro);
        Paziente paziente = new Paziente();
        paziente.setId(idPaziente.longValue());
        soglia.setPaziente(paziente);
        soglia.setValoreMin(90.0);
        soglia.setValoreMax(140.0);
        sogliaParametroRepository.save(soglia);

        Map<String, Object> nuovoParametro = new HashMap<>();
        nuovoParametro.put("tipo", "PRESSIONE_SIS");
        nuovoParametro.put("pressioneSistolica", 120.0);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(nuovoParametro, headerConToken(token));
        ResponseEntity<Map> response = restTemplate.postForEntity(
                baseUrl() + "/api/pazienti/" + idPaziente + "/parametri", request, Map.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(null, response.getBody().get("alert"),
                "Un valore di 120 (dentro soglia 90-140) non deve generare nessun alert");
    }

    // TS-03: un Paziente non può accedere a un endpoint riservato al Medico
    @Test
    void quandoPazienteAccedeAEndpointMedico_accessoNegato() {
        Map<String, Object> auth = registraELoggaPaziente("sistema.paziente3@test.it");
        String token = (String) auth.get("token");

        HttpEntity<Void> request = new HttpEntity<>(headerConToken(token));
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl() + "/appuntamenti/medico",
                org.springframework.http.HttpMethod.GET,
                request,
                String.class);

        assertTrue(response.getStatusCode() == HttpStatus.FORBIDDEN || response.getStatusCode() == HttpStatus.UNAUTHORIZED,
                "Un paziente non deve poter accedere a un endpoint riservato al medico");
    }

    // TS-04: senza autenticazione, il sistema nega l'accesso a un endpoint protetto
    @Test
    void quandoNessunaAutenticazione_accessoNegatoDavveroSulServerReale() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                baseUrl() + "/appuntamenti/paziente", String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}
