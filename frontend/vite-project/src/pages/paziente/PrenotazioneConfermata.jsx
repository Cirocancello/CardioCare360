import { useNavigate } from "react-router-dom";
import "../../styles/paziente/prenotazione.css";

export default function PrenotazioneConfermata() {
  const navigate = useNavigate();

  return (
    <div className="prenotazione-container" style={{ textAlign: "center" }}>
      <h1 style={{ fontSize: "32px", color: "#2ecc71" }}>
        Prenotazione confermata! ✅
      </h1>

      <p style={{ fontSize: "18px", marginTop: "20px" }}>
        La tua visita è stata registrata correttamente.
      </p>

      <button
        className="btn-primary"
        onClick={() => navigate("/paziente/appuntamenti")}
        style={{ marginTop: "30px", padding: "12px 25px", fontSize: "16px" }}
      >
        Vai ai tuoi appuntamenti
      </button>
    </div>
  );
}
