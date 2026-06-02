import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import SidebarPaziente from "../../components/SidebarPaziente.jsx";
import TopbarPaziente from "../../components/TopbarPaziente.jsx";
import "../../styles/paziente/DashboardPaziente.css";

export default function DashboardPaziente() {
  const [paziente, setPaziente] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const idPaziente = localStorage.getItem("idPaziente");
    const token = localStorage.getItem("token");

    if (!idPaziente || !token) {
      navigate("/login");
      return;
    }

    fetch(`http://localhost:8080/paziente/${idPaziente}`, {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then((res) => res.json())
      .then((data) => setPaziente(data))
      .catch((err) => console.error("Errore caricamento paziente:", err));
  }, [navigate]);

  if (!paziente) return <p>Caricamento dati...</p>;

  return (
    <div className="layout-paziente">

      {/* Sidebar */}
      <SidebarPaziente />

      {/* Contenuto */}
      <div className="dashboard-paziente-container">

        {/* Topbar */}
        <TopbarPaziente />

        {/* Titolo */}
        <h1 className="dashboard-title">
          Benvenuto{paziente?.nomeCompleto ? `, ${paziente.nomeCompleto}` : ""}
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

          {/* Logout come card */}
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
