import { useNavigate } from "react-router-dom";
import axios from "axios";

import SidebarPaziente from "../../components/SidebarPaziente.jsx";
import TopbarPaziente from "../../components/TopbarPaziente.jsx";

import "../../styles/paziente/Appuntamenti.css"; // 🔥 layout paziente

export default function RiepilogoPrenotazione() {
  const navigate = useNavigate();

  const visita = localStorage.getItem("visitaSelezionata") || "";
  const idMedico = localStorage.getItem("idMedicoSelezionato") || "";
  const dataAppuntamento = localStorage.getItem("dataPrenotazione") || "";
  const oraAppuntamento = localStorage.getItem("oraPrenotazione") || "";
  const idPaziente = localStorage.getItem("idPaziente") || "";
  const token = localStorage.getItem("token");

  const handleConferma = async () => {
    if (!token) {
      alert("Token mancante. Effettua di nuovo il login.");
      navigate("/login");
      return;
    }

    if (!idMedico || !idPaziente || !dataAppuntamento || !oraAppuntamento) {
      alert("Dati mancanti per completare la prenotazione.");
      return;
    }

    try {
      const nuovoAppuntamento = {
        idMedico: Number(idMedico),
        idPaziente: Number(idPaziente),
        dataAppuntamento,
        oraAppuntamento,
        note: visita ? `Prenotazione per visita ${visita}` : "",
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

      localStorage.removeItem("visitaSelezionata");
      localStorage.removeItem("idMedicoSelezionato");
      localStorage.removeItem("dataPrenotazione");
      localStorage.removeItem("oraPrenotazione");

      navigate("/paziente/prenota/confermata", {
        state: { appuntamento: res.data },
      });
    } catch (err) {
      console.error("Errore nella conferma:", err);
      alert("Errore nella conferma della prenotazione. Riprova.");
    }
  };

  return (
    <div className="layout-paziente">

      {/* Sidebar */}
      <SidebarPaziente />

      {/* Contenuto */}
      <div className="appuntamenti-container">

        {/* Topbar */}
        <TopbarPaziente />

        <h1 className="page-title">Riepilogo prenotazione</h1>

        <p><strong>Visita:</strong> {visita || "—"}</p>
        <p><strong>Medico:</strong> Mario Rossi</p>
        <p><strong>Data:</strong> {dataAppuntamento || "—"}</p>
        <p><strong>Ora:</strong> {oraAppuntamento || "—"}</p>

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
    </div>
  );
}
