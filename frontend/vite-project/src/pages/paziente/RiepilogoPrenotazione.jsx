import { useNavigate } from "react-router-dom";
import axios from "axios";
import "../../styles/paziente/selezionaVisita.css";

export default function RiepilogoPrenotazione() {
  const navigate = useNavigate();

  const visita = localStorage.getItem("visitaSelezionata");
  const idMedico = localStorage.getItem("idMedicoSelezionato");
  const dataAppuntamento = localStorage.getItem("dataPrenotazione");
  const oraAppuntamento = localStorage.getItem("oraPrenotazione");
  const idPaziente = localStorage.getItem("idPaziente");
  const token = localStorage.getItem("token");

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

      navigate("/paziente/prenota/confermata");
    } catch (err) {
      console.error("Errore nella conferma:", err);
      alert("Errore nella conferma della prenotazione. Riprova.");
    }
  };

  return (
    <div className="visita-container">
      <h2>Riepilogo prenotazione</h2>
      <p className="visita-descrizione">
        Controlla i dati della tua prenotazione prima di confermare.
      </p>

      <div className="riepilogo-box">
        <p><strong>Visita:</strong> {visita}</p>
        <p><strong>Medico:</strong> Mario Rossi</p>
        <p><strong>Data:</strong> {dataAppuntamento}</p>
        <p><strong>Ora:</strong> {oraAppuntamento}</p>
      </div>

      <div className="bottoni-navigazione">
        <button className="btn-indietro" onClick={() => navigate(-1)}>
          Indietro
        </button>

        <button className="btn-continua" onClick={handleConferma}>
          Conferma prenotazione
        </button>
      </div>
    </div>
  );
}
