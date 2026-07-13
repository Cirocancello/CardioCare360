import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "../../styles/paziente/selezionaVisita.css";

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
    <div className="visita-container">
      <h2>Seleziona la data</h2>
      <p className="visita-descrizione">
        Scegli una data tra quelle disponibili per il medico selezionato.
      </p>

      {errore && <p className="error-message">{errore}</p>}

      <div className="visita-grid">
        {dateDisponibili.length > 0 ? (
          dateDisponibili.map((data) => (
            <div
              key={data}
              className={`visita-card ${
                dataSelezionata === data ? "selected" : ""
              }`}
              onClick={() => setDataSelezionata(data)}
            >
              <span>{data}</span>
            </div>
          ))
        ) : (
          <p>Nessuna data disponibile.</p>
        )}
      </div>

      <div className="bottoni-navigazione">
        <button className="btn-indietro" onClick={() => navigate(-1)}>
          Indietro
        </button>

        <button
          className="btn-continua"
          disabled={!dataSelezionata}
          onClick={handleNext}
        >
          Continua
        </button>
      </div>
    </div>
  );
}
