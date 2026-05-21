import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "../../styles/paziente/prenotazione.css";

export default function SelezionaData() {
  const navigate = useNavigate();

  const [dateDisponibili, setDateDisponibili] = useState([]);
  const [dataSelezionata, setDataSelezionata] = useState("");
  const [loading, setLoading] = useState(true);
  const [errore, setErrore] = useState("");

  useEffect(() => {
    const idMedico = localStorage.getItem("idMedicoSelezionato");

    if (!idMedico) {
      navigate("/paziente/prenota/medico");
      return;
    }

    axios
      .get(`http://localhost:8080/disponibilita/date/medico/${idMedico}`)
      .then((res) => {
        setDateDisponibili(res.data);
        setLoading(false);
      })
      .catch((err) => {
        console.error("Errore caricamento date:", err);
        setErrore("Errore nel caricamento delle date disponibili.");
        setLoading(false);
      });
  }, [navigate]);

  const handleNext = () => {
    if (!dataSelezionata) return;

    localStorage.setItem("dataPrenotazione", dataSelezionata);
    navigate("/paziente/prenota/orario");
  };

  if (loading) return <p>Caricamento date disponibili...</p>;

  return (
    <div className="prenotazione-container">
      <h1>Seleziona la data</h1>
      <p>Scegli una data tra quelle disponibili per il medico.</p>

      {errore && <p className="error-message">{errore}</p>}

      <div className="steps-column">
        {dateDisponibili.length > 0 ? (
          dateDisponibili.map((data) => (
            <div
              key={data}
              className={`step-card ${dataSelezionata === data ? "selected" : ""}`}
              onClick={() => setDataSelezionata(data)}
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
  );
}
