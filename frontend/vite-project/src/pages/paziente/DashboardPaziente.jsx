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
      .then((data) => setPaziente(data))
      .catch((err) => console.error("Errore caricamento paziente:", err));
  }, []);

  if (!paziente) return <p>Caricamento dati...</p>;

  return (
    <div className="dashboard-paziente-container">

      <h1 className="dashboard-title">
        Benvenuto, {paziente.nome} {paziente.cognome}
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
        <a href="/paziente/terapie" className="quick-card">Terapie</a>
        <a href="/paziente/referti" className="quick-card">Referti</a>
        <a href="/paziente/messaggi" className="quick-card">Messaggi</a>
      </div>

    </div>
  );
}
