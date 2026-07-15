CREATE DATABASE  IF NOT EXISTS `cardiocare360` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `cardiocare360`;
-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: cardiocare360
-- ------------------------------------------------------
-- Server version	8.0.40

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `amministratore`
--

DROP TABLE IF EXISTS `amministratore`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `amministratore` (
  `id` bigint NOT NULL,
  `livello` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `amministratore_ibfk_1` FOREIGN KEY (`id`) REFERENCES `utente` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `appuntamento`
--

DROP TABLE IF EXISTS `appuntamento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `appuntamento` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `paziente_id` bigint NOT NULL,
  `medico_id` bigint NOT NULL,
  `data_appuntamento` date NOT NULL,
  `ora_appuntamento` time NOT NULL,
  `stato` varchar(20) NOT NULL,
  `note` varchar(255) DEFAULT NULL,
  `tipo_visita` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_appuntamento_medico` (`medico_id`),
  KEY `FK_appuntamento_paziente` (`paziente_id`),
  CONSTRAINT `FK_appuntamento_medico` FOREIGN KEY (`medico_id`) REFERENCES `medico` (`id`),
  CONSTRAINT `FK_appuntamento_paziente` FOREIGN KEY (`paziente_id`) REFERENCES `paziente` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `conversazione`
--

DROP TABLE IF EXISTS `conversazione`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `conversazione` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `paziente_id` bigint NOT NULL,
  `medico_id` bigint NOT NULL,
  `data_creazione` datetime NOT NULL,
  `ultimo_messaggio` varchar(255) DEFAULT NULL,
  `ultimo_aggiornamento` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_conversazione_paziente` (`paziente_id`),
  KEY `fk_conversazione_medico` (`medico_id`),
  CONSTRAINT `fk_conversazione_medico` FOREIGN KEY (`medico_id`) REFERENCES `medico` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_conversazione_paziente` FOREIGN KEY (`paziente_id`) REFERENCES `paziente` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `disponibilita_medico`
--

DROP TABLE IF EXISTS `disponibilita_medico`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `disponibilita_medico` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `medico_id` bigint NOT NULL,
  `giorno_settimana` varchar(10) NOT NULL,
  `ora_inizio` time NOT NULL,
  `ora_fine` time NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_disp_medico` (`medico_id`),
  CONSTRAINT `FK_disp_medico` FOREIGN KEY (`medico_id`) REFERENCES `medico` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `documento_pdf`
--

DROP TABLE IF EXISTS `documento_pdf`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `documento_pdf` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nome_file` varchar(255) NOT NULL,
  `percorso` varchar(255) NOT NULL,
  `data_caricamento` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `percorso_file` varchar(255) NOT NULL,
  `appuntamento_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKkcurxtx0qt7je0o9ay8qeogaw` (`appuntamento_id`),
  CONSTRAINT `FKjqpx2u90w3kxo72uxcu6yv45v` FOREIGN KEY (`appuntamento_id`) REFERENCES `appuntamento` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `esame`
--

DROP TABLE IF EXISTS `esame`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `esame` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `paziente_id` bigint NOT NULL,
  `medico_id` bigint NOT NULL,
  `tipo_esame` varchar(255) NOT NULL,
  `data_esame` date NOT NULL,
  `stato` varchar(50) NOT NULL,
  `note_paziente` text,
  `note_medico` text,
  `note` text,
  `ora_esame` time(6) NOT NULL,
  `appuntamento_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_esame_paziente` (`paziente_id`),
  KEY `FKtm4jb8fc4mfq72ua9d4etnmam` (`medico_id`),
  KEY `fk_esame_appuntamento` (`appuntamento_id`),
  CONSTRAINT `fk_esame_appuntamento` FOREIGN KEY (`appuntamento_id`) REFERENCES `appuntamento` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_esame_paziente` FOREIGN KEY (`paziente_id`) REFERENCES `paziente` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FKtm4jb8fc4mfq72ua9d4etnmam` FOREIGN KEY (`medico_id`) REFERENCES `medico` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `farmaco`
--

DROP TABLE IF EXISTS `farmaco`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `farmaco` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) NOT NULL,
  `descrizione` varchar(255) DEFAULT NULL,
  `principio_attivo` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `medico`
--

DROP TABLE IF EXISTS `medico`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medico` (
  `id` bigint NOT NULL,
  `specializzazione` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_medico_utente` FOREIGN KEY (`id`) REFERENCES `utente` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `messaggio`
--

DROP TABLE IF EXISTS `messaggio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `messaggio` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `testo` text NOT NULL,
  `timestamp` datetime NOT NULL,
  `letto` bit(1) NOT NULL,
  `conversazione_id` bigint DEFAULT NULL,
  `mittente` enum('PAZIENTE','MEDICO') DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_messaggio_conversazione` (`conversazione_id`),
  CONSTRAINT `fk_messaggio_conversazione` FOREIGN KEY (`conversazione_id`) REFERENCES `conversazione` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `notifica`
--

DROP TABLE IF EXISTS `notifica`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notifica` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `utente_id` bigint NOT NULL,
  `messaggio` varchar(1000) NOT NULL,
  `tipo` enum('INFO','AVVISO','URGENTE') DEFAULT 'INFO',
  `data_invio` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `letto` tinyint(1) DEFAULT '0',
  `data_creazione` datetime(6) NOT NULL,
  `titolo` varchar(255) NOT NULL,
  `appuntamento_id` bigint DEFAULT NULL,
  `parametro_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `utente_id` (`utente_id`),
  KEY `FKah4aevyr8qv4s7cvcxens51pi` (`appuntamento_id`),
  KEY `FKjw35bngwhp1uck84n1q7hm6s1` (`parametro_id`),
  CONSTRAINT `FKah4aevyr8qv4s7cvcxens51pi` FOREIGN KEY (`appuntamento_id`) REFERENCES `appuntamento` (`id`),
  CONSTRAINT `FKjw35bngwhp1uck84n1q7hm6s1` FOREIGN KEY (`parametro_id`) REFERENCES `parametro_clinico` (`id`),
  CONSTRAINT `notifica_ibfk_1` FOREIGN KEY (`utente_id`) REFERENCES `utente` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `parametro_clinico`
--

DROP TABLE IF EXISTS `parametro_clinico`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `parametro_clinico` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) NOT NULL,
  `unita_misura` varchar(50) DEFAULT NULL,
  `data_rilevazione` datetime(6) NOT NULL,
  `tipo` varchar(100) NOT NULL,
  `valore` varchar(50) NOT NULL,
  `paziente_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8q43nonpitjwlhcsrm1epvbh3` (`paziente_id`),
  CONSTRAINT `FK8q43nonpitjwlhcsrm1epvbh3` FOREIGN KEY (`paziente_id`) REFERENCES `paziente` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `paziente`
--

DROP TABLE IF EXISTS `paziente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `paziente` (
  `id` bigint NOT NULL,
  `codice_fiscale` varchar(16) NOT NULL,
  `data_nascita` date NOT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `indirizzo` varchar(255) DEFAULT NULL,
  `luogo_nascita` varchar(100) NOT NULL,
  `medico_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `codice_fiscale` (`codice_fiscale`),
  KEY `FKtpkkxqj4w9pw6plvy4rqmfwd9` (`medico_id`),
  CONSTRAINT `FKtpkkxqj4w9pw6plvy4rqmfwd9` FOREIGN KEY (`medico_id`) REFERENCES `medico` (`id`),
  CONSTRAINT `paziente_ibfk_1` FOREIGN KEY (`id`) REFERENCES `utente` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `referto`
--

DROP TABLE IF EXISTS `referto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `referto` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `paziente_id` bigint NOT NULL,
  `medico_id` bigint DEFAULT NULL,
  `data_referto` datetime(6) NOT NULL,
  `descrizione` text NOT NULL,
  `data_creazione` datetime(6) NOT NULL,
  `diagnosi` varchar(500) NOT NULL,
  `file_path` varchar(255) NOT NULL,
  `titolo` varchar(255) NOT NULL,
  `esame_id` bigint DEFAULT NULL,
  `note_medico` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `esame_id` (`esame_id`),
  KEY `FKkakkfd2a5pqd90o1mtedo7b54` (`medico_id`),
  KEY `FK_referto_paziente` (`paziente_id`),
  CONSTRAINT `fk_referto_esame` FOREIGN KEY (`esame_id`) REFERENCES `esame` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FK_referto_paziente` FOREIGN KEY (`paziente_id`) REFERENCES `paziente` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FKkakkfd2a5pqd90o1mtedo7b54` FOREIGN KEY (`medico_id`) REFERENCES `medico` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `slot_orario`
--

DROP TABLE IF EXISTS `slot_orario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `slot_orario` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `disponibilita_id` bigint NOT NULL,
  `fine` datetime(6) NOT NULL,
  `inizio` datetime(6) NOT NULL,
  `prenotato` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `disponibilita_id` (`disponibilita_id`),
  CONSTRAINT `slot_orario_ibfk_1` FOREIGN KEY (`disponibilita_id`) REFERENCES `disponibilita_medico` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=112 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `soglia_parametro`
--

DROP TABLE IF EXISTS `soglia_parametro`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `soglia_parametro` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `parametro_id` bigint NOT NULL,
  `valore_min` varchar(50) DEFAULT NULL,
  `valore_max` varchar(50) DEFAULT NULL,
  `paziente_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `parametro_id` (`parametro_id`),
  KEY `FKio5twpbc7xxw4xl8w4kw12yx0` (`paziente_id`),
  CONSTRAINT `FKio5twpbc7xxw4xl8w4kw12yx0` FOREIGN KEY (`paziente_id`) REFERENCES `paziente` (`id`) ON DELETE CASCADE,
  CONSTRAINT `soglia_parametro_ibfk_1` FOREIGN KEY (`parametro_id`) REFERENCES `parametro_clinico` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `storico_parametri`
--

DROP TABLE IF EXISTS `storico_parametri`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `storico_parametri` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `paziente_id` bigint NOT NULL,
  `parametro_id` bigint NOT NULL,
  `valore` varchar(50) NOT NULL,
  `data_rilevazione` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `paziente_id` (`paziente_id`),
  KEY `parametro_id` (`parametro_id`),
  CONSTRAINT `storico_parametri_ibfk_1` FOREIGN KEY (`paziente_id`) REFERENCES `paziente` (`id`) ON DELETE CASCADE,
  CONSTRAINT `storico_parametri_ibfk_2` FOREIGN KEY (`parametro_id`) REFERENCES `parametro_clinico` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `terapia`
--

DROP TABLE IF EXISTS `terapia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `terapia` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `paziente_id` bigint NOT NULL,
  `medico_id` bigint NOT NULL,
  `farmaco_id` bigint NOT NULL,
  `data_inizio` date NOT NULL,
  `data_fine` date DEFAULT NULL,
  `dosaggio` varchar(255) NOT NULL,
  `note` varchar(500) DEFAULT NULL,
  `data_creazione` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `appuntamento_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKq2jxq06bkqfb59bwxbwwxhhbl` (`appuntamento_id`),
  KEY `farmaco_id` (`farmaco_id`),
  KEY `FK_terapia_paziente` (`paziente_id`),
  KEY `FK25j9jsr3h452moky2afq2njhi` (`medico_id`),
  CONSTRAINT `FK25j9jsr3h452moky2afq2njhi` FOREIGN KEY (`medico_id`) REFERENCES `medico` (`id`),
  CONSTRAINT `FK_terapia_appuntamento` FOREIGN KEY (`appuntamento_id`) REFERENCES `appuntamento` (`id`) ON DELETE CASCADE,
  CONSTRAINT `terapia_ibfk_1` FOREIGN KEY (`paziente_id`) REFERENCES `paziente` (`id`) ON DELETE CASCADE,
  CONSTRAINT `terapia_ibfk_3` FOREIGN KEY (`farmaco_id`) REFERENCES `farmaco` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `utente`
--

DROP TABLE IF EXISTS `utente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `utente` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nome` varchar(50) NOT NULL,
  `cognome` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `ruolo` enum('PAZIENTE','MEDICO','ADMIN') NOT NULL,
  `data_registrazione` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-07-14 17:17:39
