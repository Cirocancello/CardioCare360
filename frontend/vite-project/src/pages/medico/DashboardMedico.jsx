import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import SidebarMedico from "../../components/SidebarMedico.jsx";
import TopbarMedico from "../../components/TopbarMedico.jsx";
import "../../styles/medico/DashboardMedico.css";

export default function DashboardMedico() {
  const [medico, setMedico] = useState({
    nome: "",
    cognome: "",
    email: "",
    specializzazione: ""
  });

  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    const idMedico = localStorage.getItem("idMedico");
    const token = localStorage.getItem("token");

    if (!idMedico || !token) {
      navigate("/login");
      return;
    }

    fetch(`http://localhost:8080/medico/${idMedico}`, {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then(async (res) => {
        if (!res.ok) {
          const text = await res.text();
          console.error(`Errore HTTP ${res.status}: ${text}`);
          throw new Error(`Errore HTTP ${res.status}`);
        }
        return res.json();
      })
      .then((data) => {
        setMedico({
          nome: data.nome || "",
          cognome: data.cognome || "",
          email: data.email || "",
          specializzazione: data.specializzazione || ""
        });
      })
      .catch((err) => console.error("Errore caricamento medico:", err))
      .finally(() => setLoading(false));
  }, [navigate]);

  if (loading) return <p>Caricamento dati...</p>;

  return (
    <div className="layout-medico">
      <SidebarMedico />

      <div className="dashboard-medico-container">
        <TopbarMedico />

        <h1 className="dashboard-title">
          Benvenuto, {medico.nome} {medico.cognome}
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
            className="dashboard-card"
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
