import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import SidebarPaziente from "../../components/SidebarPaziente.jsx";
import TopbarPaziente from "../../components/TopbarPaziente.jsx";
import "../../styles/paziente/DashboardPaziente.css";

export default function DashboardPaziente() {
  const [username, setUsername] = useState(null);
  const [loading, setLoading] = useState(true);
  const [errore, setErrore] = useState(null);

  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem("token");

    if (!token) {
      navigate("/login");
      return;
    }

    const fetchMe = async () => {
      try {
        const res = await fetch("http://localhost:8080/utente/me", {
          headers: { Authorization: `Bearer ${token}` },
        });

        if (!res.ok) throw new Error("Errore nel caricamento del profilo");

        const data = await res.text(); // perché /me restituisce una stringa
        setUsername(data.replace("Utente autenticato: ", ""));
      } catch (err) {
        console.error("Errore caricamento paziente:", err);
        setErrore("Errore nel caricamento dei dati del paziente.");
      } finally {
        setLoading(false);
      }
    };

    fetchMe();
  }, [navigate]);

  if (loading) return <p className="loading-message">Caricamento dati...</p>;
  if (errore) return <p className="error-message">{errore}</p>;

  return (
    <div className="layout-paziente">
      <SidebarPaziente />

      <div className="dashboard-paziente-container">
        <TopbarPaziente />

        <h1 className="dashboard-title">
          Benvenuto{username ? `, ${username}` : ""}
        </h1>

        <div className="dashboard-grid">
          <div className="dashboard-card" onClick={() => navigate("/paziente/appuntamenti")}>
            📅 Appuntamenti
          </div>

          <div className="dashboard-card" onClick={() => navigate("/paziente/prenota/visita")}>
            🩺 Prenota Visita
          </div>

          <div className="dashboard-card" onClick={() => navigate("/paziente/prenota/esame")}>
            🧪 Prenota Esame
          </div>

          <div className="dashboard-card" onClick={() => navigate("/paziente/esami")}>
            📄 Referti
          </div>

          <div className="dashboard-card" onClick={() => navigate("/paziente/parametri/inserisci")}>
            ❤️ Inserisci Parametri
          </div>

          <div className="dashboard-card" onClick={() => navigate("/paziente/storico-parametri")}>
            📊 Storico Parametri
          </div>

          <div className="dashboard-card" onClick={() => navigate("/paziente/terapie")}>
            💊 Terapie
          </div>

          <div className="dashboard-card" onClick={() => navigate("/paziente/conversazioni")}>
            💬 Messaggi
          </div>

          <div
            className="dashboard-card logout-card"
            onClick={() => {
              localStorage.clear();
              window.location.href = "/login";
            }}
          >
            🚪 Logout
          </div>
        </div>
      </div>
    </div>
  );
}
