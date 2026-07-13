import { Link } from "react-router-dom";
import "../../styles/paziente/prenotazioneVisita.css";

export default function PrenotazioneVisite() {
  const steps = [
    { label: "Seleziona visita", path: "/paziente/prenota/visita" },
    { label: "Seleziona data", path: "/paziente/prenota/data" },
    { label: "Seleziona orario", path: "/paziente/prenota/orario" },
    { label: "Seleziona medico", path: "/paziente/prenota/medico" },
    { label: "Riepilogo prenotazione", path: "/paziente/prenota/riepilogo" },
    { label: "Conferma prenotazione", path: "/paziente/prenota/conferma" }
  ];

  return (
    <div className="prenotazione-container">
      <h1>Prenotazione Visita</h1>
      <p>Seleziona una delle opzioni per iniziare la prenotazione.</p>

      <div className="steps-grid">
        {steps.map((step) => (
          <Link key={step.label} to={step.path} className="step-card">
            {step.label}
          </Link>
        ))}
      </div>
    </div>
  );
}
