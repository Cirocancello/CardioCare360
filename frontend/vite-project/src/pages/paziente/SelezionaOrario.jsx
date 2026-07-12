import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

import SidebarPaziente from "../../components/SidebarPaziente.jsx";
import TopbarPaziente from "../../components/TopbarPaziente.jsx";

import "../../styles/paziente/Appuntamenti.css"; // 🔥 layout paziente

export default function SelezionaOrario() {
  const navigate = useNavigate();

  const [slotDisponibili, setSlotDisponibili] = useState([]);
  const [orariOccupati, setOrariOccupati] = useState([]);
  const [orarioSelezionato, setOrarioSelezionato] = useState("");

  const [loading, setLoading] = useState(true);
  const [errore, setErrore] = useState("");

  useEffect(() => {
    const data = localStorage.getItem("dataPrenotazione");
    const idMedico = localStorage.getItem("idMedicoSelezionato");
    const token = localStorage.getItem("token");

    if (!data || !idMedico) {
      setErrore("Dati mancanti per selezionare l'orario.");
      navigate("/paziente/prenota/data");
      return;
    }

    const fetchOrari = async () => {
      try {
        // Slot disponibili
        const resSlot = await axios.get(
          `http://localhost:8080/disponibilita/slot/${idMedico}`,
          { params: { data } }
        );

        const unici = (resSlot.data || []).filter(
          (slot, index, self) =>
            index === self.findIndex((s) => s.inizio === slot.inizio)
        );

        setSlotDisponibili(unici);

        // Orari occupati
        const resOcc = await axios.get(
          "http://localhost:8080/appuntamenti/occupati",
          {
            params: { idMedico, data },
            headers: { Authorization: `Bearer ${token}` },
          }
        );

        setOrariOccupati(resOcc.data || []);
      } catch (err) {
        console.error("Errore nel caricamento orari:", err);
        setErrore("Errore nel caricamento degli orari disponibili.");
      } finally {
        setLoading(false);
      }
    };

    fetchOrari();
  }, [navigate]);

  const handleNext = () => {
    if (!orarioSelezionato) return;

    const oraFormattata = orarioSelezionato.substring(11, 16);
    localStorage.setItem("oraPrenotazione", oraFormattata);

    navigate("/paziente/prenota/riepilogo");
  };

  if (loading) return <p>Caricamento orari disponibili...</p>;

  return (
    <div className="layout-paziente">

      {/* Sidebar */}
      <SidebarPaziente />

      {/* Contenuto */}
      <div className="appuntamenti-container">

        {/* Topbar */}
        <TopbarPaziente />

        <h1 className="page-title">Seleziona l'orario</h1>

        {errore && <p className="error-message">{errore}</p>}

        <p>
          Data scelta:{" "}
          <strong>{localStorage.getItem("dataPrenotazione") || "—"}</strong>
        </p>

        <div className="appointments-grid">
          {slotDisponibili.length > 0 ? (
            slotDisponibili.map((slot) => {
              const oraSlot = slot.inizio.substring(11, 16);
              const oraFine = slot.fine.substring(11, 16);

              const occupato = orariOccupati.some((ora) =>
                ora.startsWith(oraSlot)
              );

              return (
                <div
                  key={slot.id}
                  className={`appointment-card 
                    ${orarioSelezionato === slot.inizio ? "selected" : ""} 
                    ${occupato ? "disabled-slot" : ""}`}
                  onClick={() => {
                    if (!occupato) setOrarioSelezionato(slot.inizio);
                  }}
                  style={{ cursor: occupato ? "not-allowed" : "pointer" }}
                >
                  {oraSlot} - {oraFine}
                </div>
              );
            })
          ) : (
            <p>Nessuno slot disponibile per questa data.</p>
          )}
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
            disabled={!orarioSelezionato}
            onClick={handleNext}
          >
            Continua
          </button>
        </div>

      </div>
    </div>
  );
}
