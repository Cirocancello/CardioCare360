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
-- Dumping data for table `amministratore`
--

LOCK TABLES `amministratore` WRITE;
/*!40000 ALTER TABLE `amministratore` DISABLE KEYS */;
/*!40000 ALTER TABLE `amministratore` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `appuntamento`
--

LOCK TABLES `appuntamento` WRITE;
/*!40000 ALTER TABLE `appuntamento` DISABLE KEYS */;
INSERT INTO `appuntamento` VALUES (19,31,30,'2024-05-01','11:00:00','COMPLETATO','Visita di controllo','Controllo generale'),(20,31,30,'2024-06-15','12:30:00','CONFERMATO','Appuntamento di controllo','Controllo cardiologico'),(21,31,30,'2026-06-10','10:00:00','COMPLETATO','','Controllo generale'),(22,31,30,'2026-06-10','10:00:00','CONFERMATO','Appuntamento generato per test','Controllo generale'),(23,31,30,'2026-06-15','10:00:00','COMPLETATO',NULL,'Controllo generale'),(24,31,30,'2026-06-20','10:00:00','COMPLETATO',NULL,NULL),(25,31,30,'2026-06-18','10:00:00','CONFERMATO','Appuntamento di test','Controllo generale'),(26,31,30,'2026-06-22','11:30:00','PRENOTATO','Appuntamento di test 2','Visita cardiologica'),(27,31,30,'2026-06-25','09:00:00','COMPLETATO','Appuntamento di test 3','Controllo pressione'),(28,31,30,'2026-07-27','09:00:00','PRENOTATO','Prenotazione per visita Ecocardiogramma',NULL),(30,31,30,'2026-08-03','09:00:00','PRENOTATO','Prenotazione per visita Visita cardiologica',NULL),(31,31,30,'2026-07-20','09:00:00','PRENOTATO','Prenotazione per visita ECG',NULL),(32,31,30,'2026-07-28','09:00:00','PRENOTATO','Prenotazione per visita Ecocardiogramma',NULL);
/*!40000 ALTER TABLE `appuntamento` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `conversazione`
--

LOCK TABLES `conversazione` WRITE;
/*!40000 ALTER TABLE `conversazione` DISABLE KEYS */;
INSERT INTO `conversazione` VALUES (2,31,30,'2026-05-30 16:14:00','ciao','2026-07-12 16:48:01');
/*!40000 ALTER TABLE `conversazione` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `disponibilita_medico`
--

LOCK TABLES `disponibilita_medico` WRITE;
/*!40000 ALTER TABLE `disponibilita_medico` DISABLE KEYS */;
INSERT INTO `disponibilita_medico` VALUES (6,30,'LUNEDI','09:00:00','09:30:00'),(7,30,'LUNEDI','09:00:00','09:30:00'),(10,30,'MARTEDI','09:00:00','09:30:00');
/*!40000 ALTER TABLE `disponibilita_medico` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `documento_pdf`
--

LOCK TABLES `documento_pdf` WRITE;
/*!40000 ALTER TABLE `documento_pdf` DISABLE KEYS */;
/*!40000 ALTER TABLE `documento_pdf` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `esame`
--

LOCK TABLES `esame` WRITE;
/*!40000 ALTER TABLE `esame` DISABLE KEYS */;
INSERT INTO `esame` VALUES (4,31,30,'ECG','2026-05-25','REFERTATO',NULL,NULL,NULL,'10:00:00.000000',NULL),(13,31,30,'Esame del sangue','2026-06-10','REFERTATO','Eseguito come controllo di routine','In attesa dei risultati',NULL,'10:30:00.000000',22),(20,31,30,'ECG','2026-06-06','REFERTATO',NULL,NULL,'Test automatico','09:00:00.000000',NULL),(21,31,30,'ECOCARDIOGRAMMA','2026-06-06','REFERTATO',NULL,NULL,'Test automatico','10:00:00.000000',NULL),(22,31,30,'HOLTER','2026-06-06','REFERTATO',NULL,NULL,'Test automatico','11:00:00.000000',NULL),(23,31,30,'HOLTER','2026-07-12','PRENOTATO',NULL,NULL,'','09:00:00.000000',NULL),(24,31,33,'ECG','2026-07-12','PRENOTATO',NULL,NULL,'','09:15:00.000000',NULL),(25,31,30,'ECG','2026-07-12','PRENOTATO',NULL,NULL,'','09:30:00.000000',NULL),(26,31,33,'ECG','2026-07-12','PRENOTATO',NULL,NULL,'','09:45:00.000000',NULL),(27,31,33,'ECG','2026-07-12','PRENOTATO',NULL,NULL,'','10:00:00.000000',NULL),(28,31,30,'HOLTER','2026-07-12','PRENOTATO',NULL,NULL,'','10:30:00.000000',NULL),(29,31,33,'HOLTER','2026-07-12','PRENOTATO',NULL,NULL,'','11:00:00.000000',NULL),(30,31,30,'ECG','2026-07-12','PRENOTATO',NULL,NULL,'','10:15:00.000000',NULL),(31,31,30,'ECG','2026-07-12','PRENOTATO',NULL,NULL,'','10:45:00.000000',NULL),(32,31,30,'ECG','2026-07-12','PRENOTATO',NULL,NULL,'','11:15:00.000000',NULL),(33,31,30,'ECG','2026-07-13','PRENOTATO',NULL,NULL,'','09:00:00.000000',NULL),(34,31,30,'ECG','2026-07-13','PRENOTATO',NULL,NULL,'','09:15:00.000000',NULL),(35,31,30,'ECG','2026-07-13','PRENOTATO',NULL,NULL,'','09:30:00.000000',NULL),(36,31,30,'ECG','2026-07-13','PRENOTATO',NULL,NULL,'','09:45:00.000000',NULL),(37,31,30,'ECG','2026-07-13','PRENOTATO',NULL,NULL,'','10:00:00.000000',NULL),(38,31,33,'HOLTER','2026-07-13','PRENOTATO',NULL,NULL,'','10:30:00.000000',NULL),(39,31,30,'HOLTER','2026-07-13','PRENOTATO',NULL,NULL,'','11:00:00.000000',NULL),(40,31,30,'ECG','2026-07-13','PRENOTATO',NULL,NULL,'','10:15:00.000000',NULL);
/*!40000 ALTER TABLE `esame` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `farmaco`
--

LOCK TABLES `farmaco` WRITE;
/*!40000 ALTER TABLE `farmaco` DISABLE KEYS */;
INSERT INTO `farmaco` VALUES (1,'Tachipirina 1000mg','Analgesico e antipiretico','Paracetamolo'),(2,'Augmentin 875mg','Antibiotico ad ampio spettro','Amoxicillina + Acido Clavulanico'),(3,'Brufen 600mg','Antinfiammatorio non steroideo','Ibuprofene');
/*!40000 ALTER TABLE `farmaco` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `medico`
--

LOCK TABLES `medico` WRITE;
/*!40000 ALTER TABLE `medico` DISABLE KEYS */;
INSERT INTO `medico` VALUES (30,'Cardiologia'),(33,'Cardiologia'),(40,'Cardiologia');
/*!40000 ALTER TABLE `medico` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `messaggio`
--

LOCK TABLES `messaggio` WRITE;
/*!40000 ALTER TABLE `messaggio` DISABLE KEYS */;
INSERT INTO `messaggio` VALUES (1,'Buongiorno dottore, ho un dubbio.','2026-05-08 16:50:43',_binary '\0',NULL,NULL),(2,'Buongiorno, sono il suo medico. Come sta oggi?','2026-05-08 17:10:42',_binary '\0',NULL,NULL),(4,'Ciao dottore, avrei bisogno di un chiarimento.','2026-05-30 16:14:32',_binary '',2,'PAZIENTE'),(5,'Certo, dimmi pure.','2026-05-30 16:15:02',_binary '',2,'MEDICO'),(6,'Certo, dimmi pure.','2026-05-30 16:15:05',_binary '',2,'MEDICO'),(7,'Ho notato un po’ di affanno negli ultimi giorni, è normale?','2026-05-30 16:15:32',_binary '',2,'PAZIENTE'),(8,'Dipende dall’intensità. Succede spesso o solo in alcuni momenti?','2026-05-30 16:15:38',_binary '',2,'MEDICO'),(9,'Principalmente quando salgo le scale o faccio sforzi.','2026-05-30 16:15:44',_binary '',2,'PAZIENTE'),(10,'Principalmente quando salgo le scale o faccio sforzi.','2026-05-30 16:16:03',_binary '',2,'PAZIENTE'),(11,'Capito. Hai avuto anche palpitazioni o dolori al petto?','2026-05-30 16:16:09',_binary '',2,'MEDICO'),(12,'No, nessun dolore. Solo un po’ di fiato corto.','2026-05-30 16:16:16',_binary '',2,'PAZIENTE'),(13,'Perfetto, allora monitoriamo la situazione. Se peggiora, prenota una visita.','2026-05-30 16:16:24',_binary '',2,'MEDICO'),(14,'Va bene, grazie.','2026-05-30 16:38:22',_binary '',2,'PAZIENTE'),(15,'buona giornata','2026-06-18 14:48:51',_binary '',2,'MEDICO'),(17,'ciao','2026-07-12 10:36:53',_binary '',2,'PAZIENTE'),(18,'ciao','2026-07-12 16:48:01',_binary '',2,'MEDICO');
/*!40000 ALTER TABLE `messaggio` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `notifica`
--

LOCK TABLES `notifica` WRITE;
/*!40000 ALTER TABLE `notifica` DISABLE KEYS */;
/*!40000 ALTER TABLE `notifica` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `parametro`
--

DROP TABLE IF EXISTS `parametro`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `parametro` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(255) NOT NULL,
  `tipo` varchar(255) NOT NULL,
  `unita_misura` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parametro`
--

LOCK TABLES `parametro` WRITE;
/*!40000 ALTER TABLE `parametro` DISABLE KEYS */;
INSERT INTO `parametro` VALUES (1,'Frequenza cardiaca','BATTITI','bpm'),(2,'Glicemia','GLICEMIA','mg/dL'),(3,'Temperatura corporea','TEMPERATURA','°C'),(4,'Saturazione ossigeno','SATURAZIONE','%'),(5,'Peso corporeo','PESO','kg'),(6,'Pressione arteriosa','PRESSIONE','mmHg'),(7,'Pressione sistolica','PRESSIONE_SIS','mmHg'),(8,'Pressione diastolica','PRESSIONE_DIA','mmHg');
/*!40000 ALTER TABLE `parametro` ENABLE KEYS */;
UNLOCK TABLES;

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
  `paziente_id` bigint NOT NULL,
  `alert` varchar(255) DEFAULT NULL,
  `battiti` double DEFAULT NULL,
  `glicemia` double DEFAULT NULL,
  `peso` double DEFAULT NULL,
  `pressione_diastolica` double DEFAULT NULL,
  `pressione_sistolica` double DEFAULT NULL,
  `saturazione` double DEFAULT NULL,
  `temperatura` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8q43nonpitjwlhcsrm1epvbh3` (`paziente_id`),
  CONSTRAINT `FK8q43nonpitjwlhcsrm1epvbh3` FOREIGN KEY (`paziente_id`) REFERENCES `paziente` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=175 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parametro_clinico`
--

LOCK TABLES `parametro_clinico` WRITE;
/*!40000 ALTER TABLE `parametro_clinico` DISABLE KEYS */;
INSERT INTO `parametro_clinico` VALUES (3,'Pressione arteriosa','mmHg','2026-05-31 10:30:36.319000','PRESSIONE',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(4,'Frequenza cardiaca','bpm','2026-05-31 10:30:36.319000','BATTITI',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(5,'Glicemia','mg/dL','2026-05-31 10:30:36.319000','GLICEMIA',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(6,'Saturazione ossigeno','%','2026-05-31 10:30:36.319000','SATURAZIONE',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(7,'Peso corporeo','kg','2026-05-31 10:30:36.319000','PESO',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(8,'Temperatura corporea','°C','2026-05-31 10:30:36.319000','TEMPERATURA',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(9,'Pressione arteriosa','mmHg','2026-05-31 10:34:54.741000','PRESSIONE',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(10,'Frequenza cardiaca','bpm','2026-05-31 10:34:54.741000','BATTITI',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(11,'Glicemia','mg/dL','2026-05-31 10:34:54.741000','GLICEMIA',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(12,'Saturazione ossigeno','%','2026-05-31 10:34:54.741000','SATURAZIONE',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(13,'Peso corporeo','kg','2026-05-31 10:34:54.741000','PESO',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(14,'Temperatura corporea','°C','2026-05-31 10:34:54.741000','TEMPERATURA',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(15,'Pressione arteriosa','mmHg','2026-06-03 18:54:26.000000','PRESSIONE',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(16,'Frequenza cardiaca','bpm','2026-06-03 18:54:26.000000','BATTITI',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(17,'Temperatura corporea','°C','2026-06-03 18:54:26.000000','TEMPERATURA',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(18,'Saturazione ossigeno','%','2026-06-03 18:54:26.000000','SATURAZIONE',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(19,'Peso corporeo','kg','2026-06-03 18:54:26.000000','PESO',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(20,'Pressione arteriosa','mmHg','2026-06-03 19:05:08.000000','PRESSIONE',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(21,'Frequenza cardiaca','bpm','2026-06-03 19:05:08.000000','BATTITI',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(22,'Temperatura corporea','°C','2026-06-03 19:05:08.000000','TEMPERATURA',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(23,'Pressione arteriosa','mmHg','2026-07-12 14:18:29.871000','PRESSIONE',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(24,'Frequenza cardiaca','bpm','2026-07-12 14:18:29.871000','BATTITI',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(25,'Glicemia','mg/dL','2026-07-12 14:18:29.871000','GLICEMIA',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(26,'Saturazione ossigeno','%','2026-07-12 14:18:29.871000','SATURAZIONE',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(27,'Peso corporeo','kg','2026-07-12 14:18:29.871000','PESO',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(28,'Temperatura corporea','°C','2026-07-12 14:18:29.871000','TEMPERATURA',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(29,'Frequenza cardiaca','bpm','2026-07-19 09:38:47.989081','BATTITI',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(30,'Glicemia','mg/dL','2026-07-19 09:38:47.989081','GLICEMIA',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(31,'Temperatura corporea','°C','2026-07-19 09:38:47.989081','TEMPERATURA',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(32,'Frequenza cardiaca','bpm','2026-07-19 09:41:12.018274','BATTITI',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(33,'Glicemia','mg/dL','2026-07-19 09:41:12.018274','GLICEMIA',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(34,'Temperatura corporea','°C','2026-07-19 09:41:12.018274','TEMPERATURA',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(35,'Frequenza cardiaca','bpm','2026-07-19 09:59:34.343185','BATTITI',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(36,'Glicemia','mg/dL','2026-07-19 09:59:34.343185','GLICEMIA',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(37,'Temperatura corporea','°C','2026-07-19 09:59:34.343185','TEMPERATURA',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(38,'Frequenza cardiaca','bpm','2026-07-19 10:09:43.065488','BATTITI',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(39,'Glicemia','mg/dL','2026-07-19 10:09:43.065488','GLICEMIA',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(40,'Temperatura corporea','°C','2026-07-19 10:09:43.065488','TEMPERATURA',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(41,'Frequenza cardiaca','bpm','2026-07-19 10:12:32.626140','BATTITI',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(42,'Glicemia','mg/dL','2026-07-19 10:12:32.626140','GLICEMIA',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(43,'Temperatura corporea','°C','2026-07-19 10:12:32.626140','TEMPERATURA',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(44,'Frequenza cardiaca','bpm','2026-07-19 10:33:40.863326','BATTITI',31,'Parametro fuori soglia',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(45,'Glicemia','mg/dL','2026-07-19 10:33:40.863326','GLICEMIA',31,'Parametro fuori soglia',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(46,'Temperatura corporea','°C','2026-07-19 10:33:40.863326','TEMPERATURA',31,'Parametro fuori soglia',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(47,'Frequenza cardiaca','bpm','2026-07-19 11:09:39.570980','BATTITI',31,'Parametro fuori soglia',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(48,'Glicemia','mg/dL','2026-07-19 11:09:39.570980','GLICEMIA',31,'Parametro fuori soglia',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(49,'Temperatura corporea','°C','2026-07-19 11:09:39.570980','TEMPERATURA',31,'Parametro fuori soglia',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(50,'Frequenza cardiaca','bpm','2026-07-19 11:11:31.796603','BATTITI',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(51,'Glicemia','mg/dL','2026-07-19 11:11:31.796603','GLICEMIA',31,'Parametro fuori soglia',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(52,'Temperatura corporea','°C','2026-07-19 11:11:31.796603','TEMPERATURA',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(53,'Frequenza cardiaca','bpm','2026-07-19 11:21:03.862530','BATTITI',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(54,'Glicemia','mg/dL','2026-07-19 11:21:03.862530','GLICEMIA',31,'Parametro fuori soglia',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(55,'Temperatura corporea','°C','2026-07-19 11:21:03.862530','TEMPERATURA',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(56,'Frequenza cardiaca','bpm','2026-07-19 11:21:23.473165','BATTITI',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(57,'Glicemia','mg/dL','2026-07-19 11:21:23.473165','GLICEMIA',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(58,'Temperatura corporea','°C','2026-07-19 11:21:23.473165','TEMPERATURA',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(61,'Pressione arteriosa','mmHg','2026-07-19 11:53:26.543001','PRESSIONE',31,'Pressione sistolica fuori soglia',NULL,NULL,NULL,90,150,NULL,NULL),(62,'Frequenza cardiaca','bpm','2026-07-19 11:53:26.543001','BATTITI',31,NULL,80,NULL,NULL,NULL,NULL,NULL,NULL),(63,'Glicemia','mg/dL','2026-07-19 11:53:26.543001','GLICEMIA',31,NULL,NULL,110,NULL,NULL,NULL,NULL,NULL),(64,'Saturazione ossigeno','%','2026-07-19 11:53:26.543001','SATURAZIONE',31,NULL,NULL,NULL,NULL,NULL,NULL,98,NULL),(65,'Peso corporeo','kg','2026-07-19 11:53:26.543001','PESO',31,NULL,NULL,NULL,70,NULL,NULL,NULL,NULL),(66,'Temperatura corporea','°C','2026-07-19 11:53:26.543001','TEMPERATURA',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,36.5),(67,'Pressione arteriosa','mmHg','2026-07-19 11:54:22.901283','PRESSIONE',31,NULL,NULL,NULL,NULL,90,120,NULL,NULL),(68,'Frequenza cardiaca','bpm','2026-07-19 11:54:22.901283','BATTITI',31,NULL,80,NULL,NULL,NULL,NULL,NULL,NULL),(69,'Glicemia','mg/dL','2026-07-19 11:54:22.901283','GLICEMIA',31,NULL,NULL,110,NULL,NULL,NULL,NULL,NULL),(70,'Saturazione ossigeno','%','2026-07-19 11:54:22.901283','SATURAZIONE',31,NULL,NULL,NULL,NULL,NULL,NULL,98,NULL),(71,'Peso corporeo','kg','2026-07-19 11:54:22.901283','PESO',31,NULL,NULL,NULL,70,NULL,NULL,NULL,NULL),(72,'Temperatura corporea','°C','2026-07-19 11:54:22.901283','TEMPERATURA',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,36.5),(73,'Pressione arteriosa','mmHg','2026-07-19 11:55:20.476334','PRESSIONE',31,NULL,NULL,NULL,NULL,90,120,NULL,NULL),(74,'Frequenza cardiaca','bpm','2026-07-19 11:55:20.476334','BATTITI',31,NULL,120,NULL,NULL,NULL,NULL,NULL,NULL),(75,'Glicemia','mg/dL','2026-07-19 11:55:20.476334','GLICEMIA',31,NULL,NULL,110,NULL,NULL,NULL,NULL,NULL),(76,'Saturazione ossigeno','%','2026-07-19 11:55:20.476334','SATURAZIONE',31,NULL,NULL,NULL,NULL,NULL,NULL,98,NULL),(77,'Peso corporeo','kg','2026-07-19 11:55:20.476334','PESO',31,NULL,NULL,NULL,70,NULL,NULL,NULL,NULL),(78,'Temperatura corporea','°C','2026-07-19 11:55:20.476334','TEMPERATURA',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,36.5),(79,'Pressione arteriosa','mmHg','2026-07-19 12:02:16.981057','PRESSIONE',31,NULL,NULL,NULL,NULL,90,120,NULL,NULL),(80,'Frequenza cardiaca','bpm','2026-07-19 12:02:16.981057','BATTITI',31,'Parametro fuori soglia',130,NULL,NULL,NULL,NULL,NULL,NULL),(81,'Glicemia','mg/dL','2026-07-19 12:02:16.981057','GLICEMIA',31,NULL,NULL,110,NULL,NULL,NULL,NULL,NULL),(82,'Saturazione ossigeno','%','2026-07-19 12:02:16.981057','SATURAZIONE',31,NULL,NULL,NULL,NULL,NULL,NULL,98,NULL),(83,'Peso corporeo','kg','2026-07-19 12:02:16.981057','PESO',31,NULL,NULL,NULL,70,NULL,NULL,NULL,NULL),(84,'Temperatura corporea','°C','2026-07-19 12:02:16.981057','TEMPERATURA',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,36.5),(85,'Pressione arteriosa','mmHg','2026-07-19 12:06:57.391186','PRESSIONE',31,NULL,NULL,NULL,NULL,90,120,NULL,NULL),(86,'Frequenza cardiaca','bpm','2026-07-19 12:06:57.391186','BATTITI',31,NULL,120,NULL,NULL,NULL,NULL,NULL,NULL),(87,'Glicemia','mg/dL','2026-07-19 12:06:57.391186','GLICEMIA',31,NULL,NULL,110,NULL,NULL,NULL,NULL,NULL),(88,'Saturazione ossigeno','%','2026-07-19 12:06:57.391186','SATURAZIONE',31,NULL,NULL,NULL,NULL,NULL,NULL,98,NULL),(89,'Peso corporeo','kg','2026-07-19 12:06:57.391186','PESO',31,NULL,NULL,NULL,70,NULL,NULL,NULL,NULL),(90,'Temperatura corporea','°C','2026-07-19 12:06:57.391186','TEMPERATURA',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,36.5),(91,'Pressione sistolica','mmHg','2026-07-19 12:10:01.357286','PRESSIONE_SIS',31,NULL,NULL,NULL,NULL,NULL,120,NULL,NULL),(92,'Pressione diastolica','mmHg','2026-07-19 12:10:01.357286','PRESSIONE_DIA',31,NULL,NULL,NULL,NULL,90,NULL,NULL,NULL),(93,'Frequenza cardiaca','bpm','2026-07-19 12:10:01.357286','BATTITI',31,NULL,120,NULL,NULL,NULL,NULL,NULL,NULL),(94,'Glicemia','mg/dL','2026-07-19 12:10:01.357286','GLICEMIA',31,NULL,NULL,110,NULL,NULL,NULL,NULL,NULL),(95,'Saturazione ossigeno','%','2026-07-19 12:10:01.357286','SATURAZIONE',31,NULL,NULL,NULL,NULL,NULL,NULL,98,NULL),(96,'Peso corporeo','kg','2026-07-19 12:10:01.357286','PESO',31,NULL,NULL,NULL,70,NULL,NULL,NULL,NULL),(97,'Temperatura corporea','°C','2026-07-19 12:10:01.357286','TEMPERATURA',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,36.5),(98,'Pressione sistolica','mmHg','2026-07-19 12:10:24.812502','PRESSIONE_SIS',31,NULL,NULL,NULL,NULL,NULL,130,NULL,NULL),(99,'Pressione diastolica','mmHg','2026-07-19 12:10:24.812502','PRESSIONE_DIA',31,NULL,NULL,NULL,NULL,90,NULL,NULL,NULL),(100,'Frequenza cardiaca','bpm','2026-07-19 12:10:24.812502','BATTITI',31,NULL,120,NULL,NULL,NULL,NULL,NULL,NULL),(101,'Glicemia','mg/dL','2026-07-19 12:10:24.812502','GLICEMIA',31,NULL,NULL,110,NULL,NULL,NULL,NULL,NULL),(102,'Saturazione ossigeno','%','2026-07-19 12:10:24.812502','SATURAZIONE',31,NULL,NULL,NULL,NULL,NULL,NULL,98,NULL),(103,'Peso corporeo','kg','2026-07-19 12:10:24.812502','PESO',31,NULL,NULL,NULL,70,NULL,NULL,NULL,NULL),(104,'Temperatura corporea','°C','2026-07-19 12:10:24.812502','TEMPERATURA',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,36.5),(105,'Pressione sistolica','mmHg','2026-07-19 12:10:52.539930','PRESSIONE_SIS',31,NULL,NULL,NULL,NULL,NULL,140,NULL,NULL),(106,'Pressione diastolica','mmHg','2026-07-19 12:10:52.539930','PRESSIONE_DIA',31,NULL,NULL,NULL,NULL,90,NULL,NULL,NULL),(107,'Frequenza cardiaca','bpm','2026-07-19 12:10:52.539930','BATTITI',31,NULL,120,NULL,NULL,NULL,NULL,NULL,NULL),(108,'Glicemia','mg/dL','2026-07-19 12:10:52.539930','GLICEMIA',31,NULL,NULL,110,NULL,NULL,NULL,NULL,NULL),(109,'Saturazione ossigeno','%','2026-07-19 12:10:52.539930','SATURAZIONE',31,NULL,NULL,NULL,NULL,NULL,NULL,98,NULL),(110,'Peso corporeo','kg','2026-07-19 12:10:52.539930','PESO',31,NULL,NULL,NULL,70,NULL,NULL,NULL,NULL),(111,'Temperatura corporea','°C','2026-07-19 12:10:52.539930','TEMPERATURA',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,36.5),(112,'Pressione sistolica','mmHg','2026-07-19 12:11:13.356608','PRESSIONE_SIS',31,NULL,NULL,NULL,NULL,NULL,140,NULL,NULL),(113,'Pressione diastolica','mmHg','2026-07-19 12:11:13.356608','PRESSIONE_DIA',31,NULL,NULL,NULL,NULL,100,NULL,NULL,NULL),(114,'Frequenza cardiaca','bpm','2026-07-19 12:11:13.356608','BATTITI',31,NULL,120,NULL,NULL,NULL,NULL,NULL,NULL),(115,'Glicemia','mg/dL','2026-07-19 12:11:13.356608','GLICEMIA',31,NULL,NULL,110,NULL,NULL,NULL,NULL,NULL),(116,'Saturazione ossigeno','%','2026-07-19 12:11:13.356608','SATURAZIONE',31,NULL,NULL,NULL,NULL,NULL,NULL,98,NULL),(117,'Peso corporeo','kg','2026-07-19 12:11:13.356608','PESO',31,NULL,NULL,NULL,70,NULL,NULL,NULL,NULL),(118,'Temperatura corporea','°C','2026-07-19 12:11:13.356608','TEMPERATURA',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,36.5),(119,'Pressione sistolica','mmHg','2026-07-19 12:13:15.530370','PRESSIONE_SIS',31,NULL,NULL,NULL,NULL,NULL,140,NULL,NULL),(120,'Pressione diastolica','mmHg','2026-07-19 12:13:15.530370','PRESSIONE_DIA',31,NULL,NULL,NULL,NULL,100,NULL,NULL,NULL),(121,'Frequenza cardiaca','bpm','2026-07-19 12:13:15.530370','BATTITI',31,NULL,120,NULL,NULL,NULL,NULL,NULL,NULL),(122,'Glicemia','mg/dL','2026-07-19 12:13:15.530370','GLICEMIA',31,NULL,NULL,110,NULL,NULL,NULL,NULL,NULL),(123,'Saturazione ossigeno','%','2026-07-19 12:13:15.530370','SATURAZIONE',31,NULL,NULL,NULL,NULL,NULL,NULL,98,NULL),(124,'Peso corporeo','kg','2026-07-19 12:13:15.530370','PESO',31,NULL,NULL,NULL,70,NULL,NULL,NULL,NULL),(125,'Temperatura corporea','°C','2026-07-19 12:13:15.530370','TEMPERATURA',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,36.5),(126,'Pressione sistolica','mmHg','2026-07-19 12:16:33.030389','PRESSIONE_SIS',31,'Pressione sistolica fuori soglia',NULL,NULL,NULL,NULL,140,NULL,NULL),(127,'Pressione diastolica','mmHg','2026-07-19 12:16:33.030389','PRESSIONE_DIA',31,'Pressione diastolica fuori soglia',NULL,NULL,NULL,100,NULL,NULL,NULL),(128,'Frequenza cardiaca','bpm','2026-07-19 12:16:33.030389','BATTITI',31,NULL,120,NULL,NULL,NULL,NULL,NULL,NULL),(129,'Glicemia','mg/dL','2026-07-19 12:16:33.030389','GLICEMIA',31,NULL,NULL,110,NULL,NULL,NULL,NULL,NULL),(130,'Saturazione ossigeno','%','2026-07-19 12:16:33.030389','SATURAZIONE',31,NULL,NULL,NULL,NULL,NULL,NULL,98,NULL),(131,'Peso corporeo','kg','2026-07-19 12:16:33.030389','PESO',31,NULL,NULL,NULL,70,NULL,NULL,NULL,NULL),(132,'Temperatura corporea','°C','2026-07-19 12:16:33.030389','TEMPERATURA',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,36.5),(133,'Pressione sistolica','mmHg','2026-07-19 12:16:57.022886','PRESSIONE_SIS',31,'Pressione sistolica fuori soglia',NULL,NULL,NULL,NULL,140,NULL,NULL),(134,'Pressione diastolica','mmHg','2026-07-19 12:16:57.022886','PRESSIONE_DIA',31,'Pressione diastolica fuori soglia',NULL,NULL,NULL,100,NULL,NULL,NULL),(135,'Frequenza cardiaca','bpm','2026-07-19 12:16:57.022886','BATTITI',31,'Parametro fuori soglia',130,NULL,NULL,NULL,NULL,NULL,NULL),(136,'Glicemia','mg/dL','2026-07-19 12:16:57.022886','GLICEMIA',31,NULL,NULL,110,NULL,NULL,NULL,NULL,NULL),(137,'Saturazione ossigeno','%','2026-07-19 12:16:57.022886','SATURAZIONE',31,NULL,NULL,NULL,NULL,NULL,NULL,98,NULL),(138,'Peso corporeo','kg','2026-07-19 12:16:57.022886','PESO',31,NULL,NULL,NULL,70,NULL,NULL,NULL,NULL),(139,'Temperatura corporea','°C','2026-07-19 12:16:57.022886','TEMPERATURA',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,36.5),(140,'Pressione sistolica','mmHg','2026-07-19 12:17:14.599482','PRESSIONE_SIS',31,'Pressione sistolica fuori soglia',NULL,NULL,NULL,NULL,140,NULL,NULL),(141,'Pressione diastolica','mmHg','2026-07-19 12:17:14.599482','PRESSIONE_DIA',31,'Pressione diastolica fuori soglia',NULL,NULL,NULL,100,NULL,NULL,NULL),(142,'Frequenza cardiaca','bpm','2026-07-19 12:17:14.599482','BATTITI',31,'Parametro fuori soglia',130,NULL,NULL,NULL,NULL,NULL,NULL),(143,'Glicemia','mg/dL','2026-07-19 12:17:14.599482','GLICEMIA',31,'Parametro fuori soglia',NULL,120,NULL,NULL,NULL,NULL,NULL),(144,'Saturazione ossigeno','%','2026-07-19 12:17:14.599482','SATURAZIONE',31,NULL,NULL,NULL,NULL,NULL,NULL,98,NULL),(145,'Peso corporeo','kg','2026-07-19 12:17:14.599482','PESO',31,NULL,NULL,NULL,70,NULL,NULL,NULL,NULL),(146,'Temperatura corporea','°C','2026-07-19 12:17:14.599482','TEMPERATURA',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,36.5),(147,'Pressione sistolica','mmHg','2026-07-19 12:17:45.548686','PRESSIONE_SIS',31,'Pressione sistolica fuori soglia',NULL,NULL,NULL,NULL,140,NULL,NULL),(148,'Pressione diastolica','mmHg','2026-07-19 12:17:45.548686','PRESSIONE_DIA',31,'Pressione diastolica fuori soglia',NULL,NULL,NULL,100,NULL,NULL,NULL),(149,'Frequenza cardiaca','bpm','2026-07-19 12:17:45.548686','BATTITI',31,'Parametro fuori soglia',130,NULL,NULL,NULL,NULL,NULL,NULL),(150,'Glicemia','mg/dL','2026-07-19 12:17:45.548686','GLICEMIA',31,'Parametro fuori soglia',NULL,120,NULL,NULL,NULL,NULL,NULL),(151,'Saturazione ossigeno','%','2026-07-19 12:17:45.548686','SATURAZIONE',31,'Parametro fuori soglia',NULL,NULL,NULL,NULL,NULL,118,NULL),(152,'Peso corporeo','kg','2026-07-19 12:17:45.548686','PESO',31,NULL,NULL,NULL,70,NULL,NULL,NULL,NULL),(153,'Temperatura corporea','°C','2026-07-19 12:17:45.548686','TEMPERATURA',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,36.5),(154,'Pressione sistolica','mmHg','2026-07-19 12:18:09.584109','PRESSIONE_SIS',31,'Pressione sistolica fuori soglia',NULL,NULL,NULL,NULL,140,NULL,NULL),(155,'Pressione diastolica','mmHg','2026-07-19 12:18:09.584109','PRESSIONE_DIA',31,'Pressione diastolica fuori soglia',NULL,NULL,NULL,100,NULL,NULL,NULL),(156,'Frequenza cardiaca','bpm','2026-07-19 12:18:09.584109','BATTITI',31,'Parametro fuori soglia',130,NULL,NULL,NULL,NULL,NULL,NULL),(157,'Glicemia','mg/dL','2026-07-19 12:18:09.584109','GLICEMIA',31,'Parametro fuori soglia',NULL,120,NULL,NULL,NULL,NULL,NULL),(158,'Saturazione ossigeno','%','2026-07-19 12:18:09.584109','SATURAZIONE',31,'Parametro fuori soglia',NULL,NULL,NULL,NULL,NULL,118,NULL),(159,'Peso corporeo','kg','2026-07-19 12:18:09.584109','PESO',31,NULL,NULL,NULL,70,NULL,NULL,NULL,NULL),(160,'Temperatura corporea','°C','2026-07-19 12:18:09.584109','TEMPERATURA',31,'Parametro fuori soglia',NULL,NULL,NULL,NULL,NULL,NULL,38),(161,'Pressione sistolica','mmHg','2026-07-19 12:24:25.118676','PRESSIONE_SIS',31,'Pressione sistolica fuori soglia',NULL,NULL,NULL,NULL,140,NULL,NULL),(162,'Pressione diastolica','mmHg','2026-07-19 12:24:25.118676','PRESSIONE_DIA',31,'Pressione diastolica fuori soglia',NULL,NULL,NULL,100,NULL,NULL,NULL),(163,'Frequenza cardiaca','bpm','2026-07-19 12:24:25.118676','BATTITI',31,'Battiti fuori soglia',130,NULL,NULL,NULL,NULL,NULL,NULL),(164,'Glicemia','mg/dL','2026-07-19 12:24:25.118676','GLICEMIA',31,'Glicemia fuori soglia',NULL,120,NULL,NULL,NULL,NULL,NULL),(165,'Saturazione ossigeno','%','2026-07-19 12:24:25.118676','SATURAZIONE',31,'Saturazione fuori soglia',NULL,NULL,NULL,NULL,NULL,118,NULL),(166,'Peso corporeo','kg','2026-07-19 12:24:25.118676','PESO',31,NULL,NULL,NULL,70,NULL,NULL,NULL,NULL),(167,'Temperatura corporea','°C','2026-07-19 12:24:25.118676','TEMPERATURA',31,'Temperatura fuori soglia',NULL,NULL,NULL,NULL,NULL,NULL,38),(168,'Pressione sistolica','mmHg','2026-07-19 10:25:53.948000','PRESSIONE_SIS',31,'Pressione sistolica fuori soglia',NULL,NULL,NULL,NULL,150,NULL,NULL),(169,'Pressione diastolica','mmHg','2026-07-19 10:25:53.948000','PRESSIONE_DIA',31,NULL,NULL,NULL,NULL,90,NULL,NULL,NULL),(170,'Frequenza cardiaca','bpm','2026-07-19 10:25:53.948000','BATTITI',31,NULL,80,NULL,NULL,NULL,NULL,NULL,NULL),(171,'Glicemia','mg/dL','2026-07-19 10:25:53.948000','GLICEMIA',31,NULL,NULL,100,NULL,NULL,NULL,NULL,NULL),(172,'Saturazione ossigeno','%','2026-07-19 10:25:53.948000','SATURAZIONE',31,'Saturazione fuori soglia',NULL,NULL,NULL,NULL,NULL,90,NULL),(173,'Peso corporeo','kg','2026-07-19 10:25:53.948000','PESO',31,NULL,NULL,NULL,80,NULL,NULL,NULL,NULL),(174,'Temperatura corporea','°C','2026-07-19 10:25:53.948000','TEMPERATURA',31,NULL,NULL,NULL,NULL,NULL,NULL,NULL,36);
/*!40000 ALTER TABLE `parametro_clinico` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `paziente`
--

LOCK TABLES `paziente` WRITE;
/*!40000 ALTER TABLE `paziente` DISABLE KEYS */;
INSERT INTO `paziente` VALUES (4,'RSSMRA80A01F839X','1980-01-01','12121212','Via Roma 10','Napoli',NULL),(31,'cnccnccnc7512','1980-01-01','3331234567','Via Roma 10','Napoli',30),(49,'nadrnd7575765','1975-01-01','222333444555','Via Roma 10','Napoli',NULL),(50,'vcncnn1231231234','1975-01-01','333444555666','Via Roma 10','Napoli',NULL);
/*!40000 ALTER TABLE `paziente` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `referto`
--

LOCK TABLES `referto` WRITE;
/*!40000 ALTER TABLE `referto` DISABLE KEYS */;
INSERT INTO `referto` VALUES (11,31,30,'2026-06-06 18:23:34.126086','Documento PDF del referto caricato dal medico','2026-06-06 18:23:34.126086','In attesa di diagnosi','C:\\Users\\ciroc\\eclipse-workspace\\cardiocare360\\uploads\\referti\\referto_13.pdf','Referto Esame Esame del sangue',13,'\"Referto di prova\"'),(12,31,30,'2026-06-06 18:26:31.176416','Documento PDF del referto caricato dal medico','2026-06-06 18:26:31.176416','In attesa di diagnosi','C:\\Users\\ciroc\\eclipse-workspace\\cardiocare360\\uploads\\referti\\referto_20.pdf','Referto Esame ECG',20,'\"Referto di prova\"'),(13,31,30,'2026-06-06 18:35:22.773110','Documento PDF del referto caricato dal medico','2026-06-06 18:35:22.773110','In attesa di diagnosi','C:\\Users\\ciroc\\eclipse-workspace\\cardiocare360\\uploads\\referti\\referto_21.pdf','Referto Esame ECOCARDIOGRAMMA',21,'Referto di prova'),(15,31,30,'2026-06-06 18:43:08.962745','Documento PDF del referto caricato dal medico','2026-06-06 18:43:08.962745','In attesa di diagnosi','C:\\Users\\ciroc\\eclipse-workspace\\cardiocare360\\uploads\\referti\\referto_4.pdf','Referto Esame ECG',4,'Referto di prova'),(16,31,30,'2026-06-06 18:51:03.705882','Documento PDF del referto caricato dal medico','2026-06-06 18:51:03.705882','In attesa di diagnosi','C:\\Users\\ciroc\\eclipse-workspace\\cardiocare360\\uploads\\referti\\referto_22.pdf','Referto Esame HOLTER',22,'Referto di prova');
/*!40000 ALTER TABLE `referto` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `slot_orario`
--

LOCK TABLES `slot_orario` WRITE;
/*!40000 ALTER TABLE `slot_orario` DISABLE KEYS */;
INSERT INTO `slot_orario` VALUES (103,6,'2026-08-03 09:30:00.000000','2026-08-03 09:00:00.000000',_binary '\0'),(104,7,'2026-08-03 09:30:00.000000','2026-08-03 09:00:00.000000',_binary '\0'),(105,6,'2026-07-20 09:30:00.000000','2026-07-20 09:00:00.000000',_binary '\0'),(106,7,'2026-07-20 09:30:00.000000','2026-07-20 09:00:00.000000',_binary '\0'),(107,6,'2026-07-27 09:30:00.000000','2026-07-27 09:00:00.000000',_binary '\0'),(108,7,'2026-07-27 09:30:00.000000','2026-07-27 09:00:00.000000',_binary '\0'),(109,6,'2026-07-13 09:30:00.000000','2026-07-13 09:00:00.000000',_binary '\0'),(110,7,'2026-07-13 09:30:00.000000','2026-07-13 09:00:00.000000',_binary '\0'),(111,10,'2026-07-28 09:30:00.000000','2026-07-28 09:00:00.000000',_binary '\0');
/*!40000 ALTER TABLE `slot_orario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `soglia_parametro`
--

DROP TABLE IF EXISTS `soglia_parametro`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `soglia_parametro` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `parametro_id` int NOT NULL,
  `valore_min` double NOT NULL,
  `valore_max` double NOT NULL,
  `paziente_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKio5twpbc7xxw4xl8w4kw12yx0` (`paziente_id`),
  KEY `fk_soglia_parametro_tipo` (`parametro_id`),
  CONSTRAINT `fk_soglia_parametro_tipo` FOREIGN KEY (`parametro_id`) REFERENCES `parametro` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FKio5twpbc7xxw4xl8w4kw12yx0` FOREIGN KEY (`paziente_id`) REFERENCES `paziente` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `soglia_parametro`
--

LOCK TABLES `soglia_parametro` WRITE;
/*!40000 ALTER TABLE `soglia_parametro` DISABLE KEYS */;
INSERT INTO `soglia_parametro` VALUES (4,1,50,120,31),(5,2,70,110,31),(6,3,35,37.5,31),(7,4,92,100,31),(8,5,40,120,31),(9,6,90,130,31),(10,7,60,90,31),(11,8,60,90,31);
/*!40000 ALTER TABLE `soglia_parametro` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `storico_parametri`
--

LOCK TABLES `storico_parametri` WRITE;
/*!40000 ALTER TABLE `storico_parametri` DISABLE KEYS */;
/*!40000 ALTER TABLE `storico_parametri` ENABLE KEYS */;
UNLOCK TABLES;

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
-- Dumping data for table `terapia`
--

LOCK TABLES `terapia` WRITE;
/*!40000 ALTER TABLE `terapia` DISABLE KEYS */;
INSERT INTO `terapia` VALUES (3,31,30,1,'2024-05-01','2024-05-10','1 compressa al giorno','Assumere dopo i pasti','2026-05-30 16:49:45',19),(6,31,30,2,'2024-06-15','2024-06-25','1 compressa mattina e sera','Monitorare pressione e frequenza cardiaca','2026-05-30 16:52:13',20),(11,31,30,3,'2026-06-10','2026-06-20','1 compressa ogni 12 ore','Assumere con acqua','2026-06-03 18:48:25',21),(12,31,30,3,'2026-06-10','2026-06-20','1 compressa ogni 12 ore','Assumere con acqua','2026-06-03 18:52:12',22),(32,31,30,1,'2024-05-01','2024-05-10','1 compressa ogni 12 ore','Terapia iniziale','2026-06-14 13:55:06',24),(34,4,30,1,'2026-06-14','2026-07-14','1 pillola al giorno dopo i pasti ','non saltare la terapia','2026-06-14 19:24:28',25),(35,4,30,1,'2026-07-12','2026-07-19','1 al giorno','','2026-07-12 16:58:00',23),(36,4,30,2,'2026-07-12','2026-07-31','2 al giorno, mattina e sera dopo pranzo','','2026-07-12 17:03:05',27);
/*!40000 ALTER TABLE `terapia` ENABLE KEYS */;
UNLOCK TABLES;

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

--
-- Dumping data for table `utente`
--

LOCK TABLES `utente` WRITE;
/*!40000 ALTER TABLE `utente` DISABLE KEYS */;
INSERT INTO `utente` VALUES (2,'Mario','Rossi','mario.rossi@ospedale.it','$2a$10$DHbIJGS7wpZMbMj1G.cssunDECVuISOAf7Y8cA.XmLN8qn5As6KKG','MEDICO','2026-05-01 06:02:06'),(3,'Luigi','Bianchi','luigi.bianchi@ospedale.it','$2a$10$jUCo5Wc6Jirv0BwMDjymIOpGTQPUnyOzcPfIkBLk24GaWoHO6AWVO','MEDICO','2026-05-01 08:19:04'),(4,'Mario','Rossi','paziente@example.com','$2a$10$ew.TZbb5rPlIM2zAq33sc.aXAt.jcfBkqSuCMu/qOwJRQYZV91JkC','PAZIENTE','2026-05-07 22:00:00'),(21,'Mario','Rossi','mario.rossi@cardiocare360.it','hashpassword','MEDICO','2026-05-19 18:58:21'),(22,'Luigi','Verdi','luigi.verdi@ospedale.it','hash','MEDICO','2026-05-19 19:03:06'),(26,'Stefano','Nardi','stefano.nardi@ospedale.it','$2a$10$ZFwRRYEZsX12CZs3R9S/7uzC1hbRIq0KjA99Aeyx9/lTyVcDCWU/O','MEDICO','2026-05-22 16:40:47'),(27,'Stefano','Nardi','stefanonardi@ospedale.it','$2a$10$cbdz41wxKw.Y2RRGOehOK.hHR74lr.X7oeMClgyXS4dHvMKxo4jqm','MEDICO','2026-05-22 17:32:54'),(28,'Stefano','Nardi','stefano.nardi2@ospedale.it','$2a$10$I4Sd4jHgSMCnd15pALmIDeLG6o.AVXABMZy7DlHi/eJeVsJl3ybQy','MEDICO','2026-05-22 17:56:29'),(29,'Stefano','Nardi','nardi2@ospedale.it','$2a$10$Yi0GibirnTL7AbRJE3ZU5.DJuT1YeEc.8Jm0QyXajIgrEvUPwSxlu','MEDICO','2026-05-22 18:00:05'),(30,'Stefano','Nardi','nardi@ospedale.it','$2a$10$UoSv28wBHf.iNJgD3.tvPeY1QzBKiSaMGUP2GWetec97oHGbFLCfa','MEDICO','2026-05-22 18:05:00'),(31,'Ciro','Rossi','ciro.rossi@ospedale.it','$2a$10$hpBpNxXElLm0yokV/tJ3FOtfc.KyLTdjdQHaMtyfeW6td7KXgGV3i','PAZIENTE','2026-05-22 18:08:39'),(33,'Grazia','Voccia','grazia.voccia@ospedale.it','$2a$10$IM1sIWC0VXMr2a9cRRUtb.GfBQlBipZUi.wpAweCiyGZC4XZWdjGC','MEDICO','2026-05-25 17:04:07'),(36,'Admin','System','admin@cardiocare360.com','$2a$10$/2TMMvbwT6B.HOVqXDi2aeXAWv5mvXQrdWQT/.YYIvWHJ.fy9/nFK','ADMIN','2026-06-22 16:18:38'),(40,'Giovanni','Cancello','givannicancello@ospedale.it','$2a$10$Qiuj9p7bnwljqB2LZytaRuaGAn77QMYr8hBhigNnl.AiTWCGbqUHS','MEDICO','2026-06-23 17:36:03'),(49,'Nadia','Rinaldi','nadiarinaldi75@mail.com','$2a$10$Y2QO.5RIn9P7DI8.t9uCKuNhlKz5Ltg7cpmc4pV3rsgrYCaxxn4DW','PAZIENTE','2026-06-25 14:26:45'),(50,'Vincenzo','Cancello','vincenzo@prova.com','$2a$10$o6sD6WHVc0vEhb5mche/F.vktTy6YeZWia/7BVMhn9OxOe4Y898B2','PAZIENTE','2026-06-25 14:57:53');
/*!40000 ALTER TABLE `utente` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-07-19 17:50:06
