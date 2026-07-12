import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "../../styles/paziente/prenotazione.css";

export default function SelezionaMedico() {
  const navigate = useNavigate();
  const [medici, setMedici] = useState([]);
  const [medicoSelezionato, setMedicoSelezionato] = useState("");
  const [loading, setLoading] = useState(true);
  const [errore, setErrore] = useState("");

  useEffect(() => {
    const specializzazione = localStorage.getItem("specializzazioneVisita");
    const token = localStorage.getItem("token");

    if (!specializzazione) {
      navigate("/paziente/prenota/visita");
      return;
    }

    axios
      .get(`http://localhost:8080/medico/visita/${specializzazione}`, {
        headers: { Authorization: `Bearer ${token}` },
      })
      .then((res) => {
        setMedici(res.data);
        setLoading(false);
      })
      .catch((err) => {
        console.error("Errore nel caricamento medici:", err);
        setErrore("Errore nel caricamento medici. Riprova più tardi.");
        setLoading(false);
      });
  }, []);

  const handleNext = () => {
    if (!medicoSelezionato) return;
    navigate("/paziente/prenota/data");
  };

  if (loading) {
    return <p>Caricamento medici...</p>;
  }

  return (
    <div className="visita-container">
      <h2>Seleziona il medico</h2>
      <p className="visita-descrizione">
        Scegli il medico disponibile per la visita selezionata.
      </p>

      {errore && <p className="error-message">{errore}</p>}

      <div className="visita-grid">
        {medici.length > 0 ? (
          medici.map((medico) => (
            <div
              key={medico.id}
              className={`visita-card ${
                medicoSelezionato === medico.id ? "selected" : ""
              }`}
              onClick={() => {
                setMedicoSelezionato(medico.id);
                localStorage.setItem("idMedicoSelezionato", medico.id);
              }}
            >
              <h3>{medico.nomeCompleto}</h3>
              <p>{medico.specializzazione}</p>
              <p>{medico.email}</p>
            </div>
          ))
        ) : (
          <p>Nessun medico disponibile per questa visita.</p>
        )}
      </div>

      <div className="bottoni-navigazione">
        <button className="btn-indietro" onClick={() => navigate(-1)}>
          Indietro
        </button>

        <button
          className="btn-continua"
          disabled={!medicoSelezionato}
          onClick={handleNext}
        >
          Continua
        </button>
      </div>
    </div>
  );
}
