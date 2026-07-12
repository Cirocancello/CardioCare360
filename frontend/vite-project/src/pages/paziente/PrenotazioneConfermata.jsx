import { useNavigate, useLocation } from "react-router-dom";
import "../../styles/paziente/prenotazione.css";

export default function PrenotazioneConfermata() {
  const navigate = useNavigate();
  const location = useLocation();

  // 🔒 Recupero dati in modo sicuro (se presenti)
  const appuntamento = location.state?.appuntamento || null;

  return (
    <div className="prenotazione-container" style={{ textAlign: "center" }}>
      <h1 style={{ fontSize: "32px", color: "#2ecc71" }}>
        Prenotazione confermata! ✅
      </h1>

      <p style={{ fontSize: "18px", marginTop: "20px" }}>
        La tua visita è stata registrata correttamente.
      </p>

      {/* 🔒 Se l’utente ricarica la pagina → niente crash */}
      {!appuntamento && (
        <p className="nota-warning" style={{ marginTop: "10px", color: "#e67e22" }}>
          ⚠️ Non è possibile mostrare il riepilogo perché la pagina è stata
          ricaricata o aperta senza dati.
        </p>
      )}

      {/* 🔒 Riepilogo sicuro */}
      {appuntamento && (
        <div className="riepilogo-box" style={{ marginTop: "20px" }}>
          <h3>Riepilogo appuntamento</h3>

          <p><strong>Data:</strong> {appuntamento.dataAppuntamento || "—"}</p>
          <p><strong>Ora:</strong> {appuntamento.oraAppuntamento || "—"}</p>
          <p><strong>Medico:</strong> {appuntamento.nomeMedico || "—"} {appuntamento.cognomeMedico || ""}</p>

          {appuntamento.note && (
            <p><strong>Note:</strong> {appuntamento.note}</p>
          )}
        </div>
      )}

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
