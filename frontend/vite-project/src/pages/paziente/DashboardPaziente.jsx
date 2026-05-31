import { useEffect, useState } from "react";

export default function DashboardPaziente() {
  const [paziente, setPaziente] = useState(null);

  useEffect(() => {
    const idPaziente = localStorage.getItem("idPaziente");
    const token = localStorage.getItem("token");

    fetch(`http://localhost:8080/paziente/${idPaziente}`, {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then((res) => res.json())      
      .then((data) => {
      console.log("Dati paziente:", data); // 👈 log corretto
      setPaziente(data);
    })
      .catch((err) => console.error("Errore caricamento paziente:", err));
  }, []);

  if (!paziente) return <p>Caricamento dati...</p>;

  return (
    <div className="dashboard-paziente-container">

      {/* 🔥 Pulsante Logout */}
      <div className="dashboard-logout-container">
        <button
          className="dashboard-logout-btn"
          onClick={() => {
            localStorage.removeItem("token");
            localStorage.removeItem("ruolo");
            localStorage.removeItem("idUtente");
            localStorage.removeItem("idPaziente");
            window.location.href = "/login";
          }}
        >
          Logout
        </button>
      </div>

      <h1 className="dashboard-title">
      <h1 className="dashboard-title">
        Benvenuto{paziente?.nomeCompleto ? `, ${paziente.nomeCompleto}` : ""}
      </h1>

      </h1>

      <div className="dashboard-info">
        <div className="info-card">
          <h3>Email</h3>
          <p>{paziente.email}</p>
        </div>

        <div className="info-card">
          <h3>Codice Fiscale</h3>
          <p>{paziente.codiceFiscale}</p>
        </div>

        <div className="info-card">
          <h3>Telefono</h3>
          <p>{paziente.telefono}</p>
        </div>
      </div>

      <h2 className="section-title">Azioni rapide</h2>

      <div className="quick-actions">

        <a href="/paziente/appuntamenti" className="quick-card">Appuntamenti</a>
        <a href="/paziente/prenota/visita" className="quick-card">Visite</a>
        <a href="/paziente/prenota/esame" className="quick-card">Esami</a>
        <a href="/paziente/esami" className="quick-card">Referti</a>

        <a href="/paziente/parametri/inserisci" className="quick-card">Parametri Vitali</a>
        <a href="/paziente/storico-parametri" className="quick-card">Storico Parametri</a>

        <a href="/paziente/terapie" className="quick-card">Terapie</a>
        <a href="/paziente/conversazioni" className="quick-card">Messaggi</a>

      </div>

    </div>
  );
}
