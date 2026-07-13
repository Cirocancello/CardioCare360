import { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../../styles/paziente/selezionaVisita.css";

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

    localStorage.setItem("visitaSelezionata", visitaSelezionata);
    localStorage.setItem("specializzazioneVisita", specializzazione);

    navigate("/paziente/prenota/medico");
  };

  return (
    <div className="visita-container">
      <h2>Seleziona la visita</h2>
      <p className="visita-descrizione">
        Scegli il tipo di visita che desideri prenotare.
      </p>

      <div className="visita-grid">
        {visiteDisponibili.map((visita) => (
          <div
            key={visita}
            className={`visita-card ${
              visitaSelezionata === visita ? "selected" : ""
            }`}
            onClick={() => setVisitaSelezionata(visita)}
          >
            <span>{visita}</span>
          </div>
        ))}
      </div>

      <div className="bottoni-navigazione">
        <button className="btn-indietro" onClick={() => navigate(-1)}>
          Indietro
        </button>
        <button
          className="btn-continua"
          disabled={!visitaSelezionata}
          onClick={handleNext}
        >
          Continua
        </button>
      </div>
    </div>
  );
}
