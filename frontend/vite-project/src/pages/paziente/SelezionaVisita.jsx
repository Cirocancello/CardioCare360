import { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../../styles/paziente/prenotazione.css";

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

  const handleNext = () => {
    if (!visitaSelezionata) return;

    const mappa = {
      "Visita cardiologica": "Cardiologia",
      "ECG": "Cardiologia",
      "Holter cardiaco": "Cardiologia",
      "Ecocardiogramma": "Cardiologia",
      "Test da sforzo": "Cardiologia",
      "Controllo pressione": "Cardiologia"
    };

    const specializzazione = mappa[visitaSelezionata];

    // 🔥 SALVATAGGIO CORRETTO
    localStorage.setItem("visitaSelezionata", visitaSelezionata);
    localStorage.setItem("specializzazioneVisita", specializzazione);

    navigate("/paziente/prenota/medico");
  };

  return (
    <div className="prenotazione-container">
      <h1>Seleziona la visita</h1>
      <p>Scegli il tipo di visita che desideri prenotare.</p>

      <div className="steps-grid">
        {visiteDisponibili.map((visita) => (
          <div
            key={visita}
            className={`step-card ${
              visitaSelezionata === visita ? "selected" : ""
            }`}
            onClick={() => setVisitaSelezionata(visita)}
          >
            {visita}
          </div>
        ))}
      </div>

      {/* 🔙 Pulsante Indietro + Continua */}
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
  );
}
