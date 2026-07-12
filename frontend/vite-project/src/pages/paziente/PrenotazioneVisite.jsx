import { useNavigate } from "react-router-dom";

import SidebarPaziente from "../../components/SidebarPaziente.jsx";
import TopbarPaziente from "../../components/TopbarPaziente.jsx";

import "../../styles/paziente/Appuntamenti.css"; // 🔥 usa lo stesso CSS della pagina funzionante

export default function PrenotaVisite() {
  const navigate = useNavigate();

  const visite = [
    { label: "Visita Cardiologica", value: "cardiologica" },
    { label: "Elettrocardiogramma", value: "ecg" },
    { label: "Ecocardiogramma", value: "eco" },
    { label: "Holter Cardiaco", value: "holter" }
  ];

  const selezionaVisita = (value) => {
    localStorage.setItem("prenotazione_visita", value);
    navigate("/paziente/prenota/data");
  };

  return (
    <div className="layout-paziente">

      {/* Sidebar */}
      <SidebarPaziente />

      {/* Contenuto */}
      <div className="appuntamenti-container"> {/* 🔥 identico alla pagina funzionante */}

        {/* Topbar */}
        <TopbarPaziente />

        <h1 className="page-title">Seleziona la visita</h1>
        <p>Scegli il tipo di visita che vuoi prenotare.</p>

        <div className="appointments-grid"> {/* 🔥 identico alla pagina funzionante */}
          {visite.map(v => (
            <div
              key={v.value}
              className="appointment-card"
              onClick={() => selezionaVisita(v.value)}
              style={{ cursor: "pointer" }}
            >
              {v.label}
            </div>
          ))}
        </div>

      </div>
    </div>
  );
}
