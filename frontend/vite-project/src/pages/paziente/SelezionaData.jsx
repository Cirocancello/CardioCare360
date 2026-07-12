import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

import SidebarPaziente from "../../components/SidebarPaziente.jsx";
import TopbarPaziente from "../../components/TopbarPaziente.jsx";

import "../../styles/paziente/Appuntamenti.css"; // 🔥 layout funzionante

export default function SelezionaData() {
  const navigate = useNavigate();

  const [dateDisponibili, setDateDisponibili] = useState([]);
  const [dataSelezionata, setDataSelezionata] = useState("");
  const [loading, setLoading] = useState(true);
  const [errore, setErrore] = useState("");

  useEffect(() => {
    const idMedico = localStorage.getItem("idMedicoSelezionato");

    if (!idMedico) {
      setErrore("Medico non selezionato.");
      navigate("/paziente/prenota/medico");
      return;
    }

    const fetchDate = async () => {
      try {
        const res = await axios.get(
          `http://localhost:8080/disponibilita/date/medico/${idMedico}`
        );

        setDateDisponibili(res.data || []);
      } catch (err) {
        console.error("Errore caricamento date:", err);
        setErrore("Errore nel caricamento delle date disponibili.");
      } finally {
        setLoading(false);
      }
    };

    fetchDate();
  }, [navigate]);

  const handleNext = () => {
    if (!dataSelezionata) return;

    localStorage.setItem("dataPrenotazione", dataSelezionata);
    navigate("/paziente/prenota/orario");
  };

  if (loading) return <p>Caricamento date disponibili...</p>;

  return (
    <div className="layout-paziente">

      {/* Sidebar */}
      <SidebarPaziente />

      {/* Contenuto */}
      <div className="appuntamenti-container">

        {/* Topbar */}
        <TopbarPaziente />

        <h1 className="page-title">Seleziona la data</h1>
        <p>Scegli una data tra quelle disponibili per il medico.</p>

        {errore && <p className="error-message">{errore}</p>}

        <div className="appointments-grid">
          {dateDisponibili.length > 0 ? (
            dateDisponibili.map((data) => (
              <div
                key={data}
                className={`appointment-card ${
                  dataSelezionata === data ? "selected" : ""
                }`}
                onClick={() => setDataSelezionata(data)}
                style={{ cursor: "pointer" }}
              >
                {data}
              </div>
            ))
          ) : (
            <p>Nessuna data disponibile.</p>
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
            disabled={!dataSelezionata}
            onClick={handleNext}
          >
            Continua
          </button>
        </div>

      </div>
    </div>
  );
}
