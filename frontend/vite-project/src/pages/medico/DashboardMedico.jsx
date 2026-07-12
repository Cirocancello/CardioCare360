import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import SidebarMedico from "../../components/SidebarMedico.jsx";
import TopbarMedico from "../../components/TopbarMedico.jsx";
import "../../styles/medico/DashboardMedico.css";

export default function DashboardMedico() {
  const [medico, setMedico] = useState(null);
  const [loading, setLoading] = useState(true);
  const [errore, setErrore] = useState(null);

  const navigate = useNavigate();

  useEffect(() => {
    const idMedico = localStorage.getItem("idMedico");
    const token = localStorage.getItem("token");

    // 🔒 Blindatura: token o id mancanti
    if (!idMedico || !token) {
      navigate("/login");
      return;
    }

    const fetchMedico = async () => {
      try {
        const res = await fetch(`http://localhost:8080/medico/${idMedico}`, {
          headers: { Authorization: `Bearer ${token}` },
        });

        if (!res.ok) {
          const text = await res.text();
          console.error(`Errore HTTP ${res.status}: ${text}`);
          throw new Error("Errore nel caricamento del profilo medico");
        }

        const data = await res.json();
        setMedico(data);
      } catch (err) {
        console.error("Errore caricamento medico:", err);
        setErrore("Errore nel caricamento dei dati del medico.");
      } finally {
        setLoading(false);
      }
    };

    fetchMedico();
  }, [navigate]);

  // ---------------------------------------------------------
  // RENDER
  // ---------------------------------------------------------

  if (loading) return <p className="loading-message">Caricamento dati...</p>;

  if (errore) return <p className="error-message">{errore}</p>;

  if (!medico) return <p className="error-message">Medico non trovato.</p>;

  return (
    <div className="layout-medico">
      <SidebarMedico />

      <div className="dashboard-medico-container">
        <TopbarMedico />

        <h1 className="dashboard-title">
          Benvenuto, {medico.nomeCompleto || "—"}
        </h1>

        <div className="dashboard-grid">

          <div className="dashboard-card" onClick={() => navigate("/medico/pazienti")}>
            👤 Gestione Pazienti
          </div>

          <div className="dashboard-card" onClick={() => navigate("/medico/visite")}>
            📅 Gestione Visite
          </div>

          <div className="dashboard-card" onClick={() => navigate("/medico/esami")}>
            🧪 Esami e Referti
          </div>

          <div className="dashboard-card" onClick={() => navigate("/medico/terapie")}>
            💊 Gestione Terapie
          </div>

          <div className="dashboard-card" onClick={() => navigate("/medico/parametri")}>
            ❤️ Parametri Vitali
          </div>

          <div className="dashboard-card" onClick={() => navigate("/medico/conversazioni")}>
            💬 Conversazioni
          </div>

          <div className="dashboard-card" onClick={() => navigate("/medico/disponibilita")}>
            🕒 Disponibilità
          </div>

          <div className="dashboard-card" onClick={() => navigate("/medico/profilo")}>
            ⚙️ Profilo
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
