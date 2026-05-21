import { useNavigate } from "react-router-dom";
import axios from "axios";
import "../../styles/paziente/prenotazione.css";

export default function RiepilogoPrenotazione() {
  const navigate = useNavigate();

  // 🔹 Recupero dati dal localStorage
  const visita = localStorage.getItem("visitaSelezionata");
  const idMedico = localStorage.getItem("idMedicoSelezionato");
  const dataAppuntamento = localStorage.getItem("dataPrenotazione");
  const oraAppuntamento = localStorage.getItem("oraPrenotazione");
  const idPaziente = localStorage.getItem("idPaziente");
  const token = localStorage.getItem("token");

  // 🔹 Funzione di conferma prenotazione
  const handleConferma = async () => {
    if (!token) {
      alert("Token mancante. Effettua di nuovo il login.");
      navigate("/login");
      return;
    }

    try {
      const nuovoAppuntamento = {
        idMedico: Number(idMedico),
        idPaziente: Number(idPaziente),
        dataAppuntamento,
        oraAppuntamento,
        note: `Prenotazione per visita ${visita}`,
      };

      const res = await axios.post(
        "http://localhost:8080/appuntamenti",
        nuovoAppuntamento,
        {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        }
      );

      console.log("Prenotazione confermata:", res.data);

      // 🔹 Redirect automatico alla pagina di conferma
      navigate("/paziente/prenota/confermata");
    } catch (err) {
      console.error("Errore nella conferma:", err);
      alert("Errore nella conferma della prenotazione. Riprova.");
    }
  };

  return (
    <div className="prenotazione-container">
      <h1>Riepilogo prenotazione</h1>

      <p><strong>Visita:</strong> {visita}</p>
      <p><strong>Medico:</strong> Mario Rossi</p>
      <p><strong>Data:</strong> {dataAppuntamento}</p>
      <p><strong>Ora:</strong> {oraAppuntamento}</p>

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
          onClick={handleConferma}
        >
          Conferma prenotazione
        </button>
      </div>
    </div>
  );
}
