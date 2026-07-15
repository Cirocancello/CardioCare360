# CardioCare360
Progetto realizzato per l'esame del corso di Ingegneria del Software 2025/26

🚀 Istruzioni di utilizzo
1️⃣ Avvio del backend (Spring Boot)
Per avviare il server backend:

Apri Eclipse (o IntelliJ, se preferisci).

Importa il progetto CardioCare360 come Maven Project.

Assicurati che MySQL sia attivo e che il database sia configurato correttamente.

Avvia l’applicazione Spring Boot tramite:

Run As → Spring Boot App

oppure eseguendo la classe CardioCare360Application.java.

Il server partirà sulla porta 8080.

2️⃣ Avvio del frontend (Vite + React)
Apri Visual Studio Code.

Vai nella cartella:

Codice
cardiocare360/frontend-vite-project
Installa le dipendenze (solo la prima volta):

Codice
npm install
Avvia il server di sviluppo:

Codice
npm run dev
Il frontend sarà disponibile su http://localhost:5173 (o altra porta indicata da Vite).

👤 Utenti di esempio
Puoi utilizzare questi account per testare il sistema:

👨‍⚕️ Amministratore
Email: admin@cardiocare360.com

Password: admin1234

🧑‍🩺 Medico
Email: nardi@ospedale.it

Password: 12345

🧑‍🧑 Paziente
Email: ciro.rossi@ospedale.it

Password: 12345

🔧 Note utili
Il backend deve essere avviato prima del frontend.

Assicurati che il file application.properties punti al database corretto.

Se modifichi la porta del backend, aggiorna anche la configurazione del frontend (file .env o chiamate fetch).
