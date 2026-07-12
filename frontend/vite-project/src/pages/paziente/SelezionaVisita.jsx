import { useState } from "react";
import { useNavigate } from "react-router-dom";

import SidebarPaziente from "../../components/SidebarPaziente.jsx";
import TopbarPaziente from "../../components/TopbarPaziente.jsx";

import "../../styles/paziente/Appuntamenti.css"; // 🔥 usa il layout funzionante

export default function SelezionaVisita() {
  const navigate = useNavigate();

  const visiteDisponibili = [
    "Visita cardiologica",
    "ECG",
    "Holter cardiaco",
    "Ecocardiogramma",
    "Test da sforzo",
    "Controllo pressione"
  ];

  const [visitaSelezionata, setVisitaSelezionata] = useState("");
  const [errore, setErrore] = useState("");

  const handleNext = () => {
    setErrore("");

    if (!visitaSelezionata) {
      setErrore("Seleziona una visita per continuare.");
      return;
    }

    const mappa = {
      "Visita cardiologica": "Cardiologia",
      "ECG": "Cardiologia",
      "Holter cardiaco": "Cardiologia",
      "Ecocardiogramma": "Cardiologia",
      "Test da sforzo": "Cardiologia",
      "Controllo pressione": "Cardiologia"
    };

    const specializzazione = mappa[visitaSelezionata];

    localStorage.setItem("visitaSelezionata", visitaSelezionata);
    localStorage.setItem("specializzazioneVisita", specializzazione);

    navigate("/paziente/prenota/medico");
  };

  return (
    <div className="layout-paziente">

      {/* Sidebar */}
      <SidebarPaziente />

      {/* Contenuto */}
      <div className="appuntamenti-container"> {/* 🔥 identico alle altre pagine */}

        {/* Topbar */}
        <TopbarPaziente />

        <h1 className="page-title">Seleziona la visita</h1>
        <p>Scegli il tipo di visita che desideri prenotare.</p>

        {errore && <p className="error-message">{errore}</p>}

        <div className="appointments-grid"> {/* 🔥 identico alle altre pagine */}
          {visiteDisponibili.map((visita) => (
            <div
              key={visita}
              className={`appointment-card ${
                visitaSelezionata === visita ? "selected" : ""
              }`}
              onClick={() => setVisitaSelezionata(visita)}
              style={{ cursor: "pointer" }}
            >
              {visita}
            </div>
          ))}
        </div>

        <div style={{ marginTop: "30px" }}>
          <button
            className="btn-secondary"
            onClick={() => navigate(-1)}
            style={{ marginRight: "15px" }}
          >
            Indietro
          </button>

          <button
            className="btn-primary"
            disabled={!visitaSelezionata}
            onClick={handleNext}
          >
            Continua
          </button>
        </div>

      </div>
    </div>
  );
}
