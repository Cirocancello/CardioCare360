CREATE DATABASE IF NOT EXISTS cardiocare360;
USE cardiocare360;

------------------------------------------------------------
-- 1. UTENTE E RUOLI
------------------------------------------------------------
CREATE TABLE utente (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    cognome VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    ruolo ENUM('PAZIENTE', 'MEDICO', 'ADMIN') NOT NULL,
    data_registrazione TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE paziente (
    id BIGINT PRIMARY KEY,
    codice_fiscale VARCHAR(16) NOT NULL UNIQUE,
    data_nascita DATE NOT NULL,
    telefono VARCHAR(20),
    indirizzo VARCHAR(255),
    FOREIGN KEY (id) REFERENCES utente(id) ON DELETE CASCADE
);

CREATE TABLE medico (
    id BIGINT PRIMARY KEY,
    specializzazione VARCHAR(100) NOT NULL,
    numero_licenza VARCHAR(50) NOT NULL UNIQUE,
    FOREIGN KEY (id) REFERENCES utente(id) ON DELETE CASCADE
);

CREATE TABLE amministratore (
    id BIGINT PRIMARY KEY,
    livello INT DEFAULT 1,
    FOREIGN KEY (id) REFERENCES utente(id) ON DELETE CASCADE
);

------------------------------------------------------------
-- 2. DISPONIBILITÀ E SLOT ORARI
------------------------------------------------------------
CREATE TABLE disponibilita_medico (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    medico_id BIGINT NOT NULL,
    giorno_settimana ENUM('LUN','MAR','MER','GIO','VEN','SAB') NOT NULL,
    ora_inizio TIME NOT NULL,
    ora_fine TIME NOT NULL,
    FOREIGN KEY (medico_id) REFERENCES medico(id) ON DELETE CASCADE
);

CREATE TABLE slot_orario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    disponibilita_id BIGINT NOT NULL,
    ora_inizio TIME NOT NULL,
    ora_fine TIME NOT NULL,
    occupato BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (disponibilita_id) REFERENCES disponibilita_medico(id) ON DELETE CASCADE
);

------------------------------------------------------------
-- 3. APPUNTAMENTI
------------------------------------------------------------
CREATE TABLE appuntamento (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    paziente_id BIGINT NOT NULL,
    medico_id BIGINT NOT NULL,
    data_appuntamento DATE NOT NULL,
    ora_appuntamento TIME NOT NULL,
    stato ENUM('PRENOTATO','CONFERMATO','ANNULLATO','COMPLETATO') DEFAULT 'PRENOTATO',
    note VARCHAR(255),
    FOREIGN KEY (paziente_id) REFERENCES paziente(id) ON DELETE CASCADE,
    FOREIGN KEY (medico_id) REFERENCES medico(id) ON DELETE CASCADE
);

------------------------------------------------------------
-- 4. REFERTI E DOCUMENTI PDF
------------------------------------------------------------
CREATE TABLE documento_pdf (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome_file VARCHAR(255) NOT NULL,
    percorso VARCHAR(255) NOT NULL,
    data_caricamento TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE referto (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    paziente_id BIGINT NOT NULL,
    medico_id BIGINT,
    data_referto DATE NOT NULL,
    descrizione TEXT NOT NULL,
    documento_id BIGINT,
    FOREIGN KEY (paziente_id) REFERENCES paziente(id) ON DELETE CASCADE,
    FOREIGN KEY (medico_id) REFERENCES medico(id) ON DELETE SET NULL,
    FOREIGN KEY (documento_id) REFERENCES documento_pdf(id) ON DELETE SET NULL
);

------------------------------------------------------------
-- 5. TERAPIE E FARMACI
------------------------------------------------------------
CREATE TABLE farmaco (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descrizione VARCHAR(255)
);

CREATE TABLE terapia (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    paziente_id BIGINT NOT NULL,
    medico_id BIGINT NOT NULL,
    farmaco_id BIGINT NOT NULL,
    data_inizio DATE NOT NULL,
    data_fine DATE,
    dosaggio VARCHAR(100),
    note VARCHAR(255),
    FOREIGN KEY (paziente_id) REFERENCES paziente(id) ON DELETE CASCADE,
    FOREIGN KEY (medico_id) REFERENCES medico(id) ON DELETE CASCADE,
    FOREIGN KEY (farmaco_id) REFERENCES farmaco(id) ON DELETE CASCADE
);

------------------------------------------------------------
-- 6. PARAMETRI CLINICI
------------------------------------------------------------
CREATE TABLE parametro_clinico (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    unita_misura VARCHAR(20) NOT NULL
);

CREATE TABLE soglia_parametro (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    parametro_id BIGINT NOT NULL,
    valore_min DECIMAL(10,2),
    valore_max DECIMAL(10,2),
    FOREIGN KEY (parametro_id) REFERENCES parametro_clinico(id) ON DELETE CASCADE
);

CREATE TABLE storico_parametri (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    paziente_id BIGINT NOT NULL,
    parametro_id BIGINT NOT NULL,
    valore DECIMAL(10,2) NOT NULL,
    data_rilevazione TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (paziente_id) REFERENCES paziente(id) ON DELETE CASCADE,
    FOREIGN KEY (parametro_id) REFERENCES parametro_clinico(id) ON DELETE CASCADE
);

------------------------------------------------------------
-- 7. MESSAGGI E NOTIFICHE
------------------------------------------------------------
CREATE TABLE messaggio (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    mittente_id BIGINT NOT NULL,
    destinatario_id BIGINT NOT NULL,
    contenuto TEXT NOT NULL,
    data_invio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (mittente_id) REFERENCES utente(id) ON DELETE CASCADE,
    FOREIGN KEY (destinatario_id) REFERENCES utente(id) ON DELETE CASCADE
);

CREATE TABLE notifica (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    utente_id BIGINT NOT NULL,
    messaggio VARCHAR(255) NOT NULL,
    tipo ENUM('INFO','AVVISO','URGENTE') DEFAULT 'INFO',
    data_invio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    letto BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (utente_id) REFERENCES utente(id) ON DELETE CASCADE
);
