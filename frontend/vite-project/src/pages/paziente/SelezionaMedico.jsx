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
    // 🔥 ORA PRENDIAMO LA SPECIALIZZAZIONE, NON LA VISITA
    const visita = localStorage.getItem("specializzazioneVisita");
    const token = localStorage.getItem("token");

    if (!visita) {
      navigate("/paziente/prenota/visita");
      return;
    }

    if (!token) {
      setErrore("Token mancante. Effettua di nuovo il login.");
      setLoading(false);
      return;
    }

    axios
      .get(`http://localhost:8080/medici/visita/${visita}`, {
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
  }, [navigate]);

  const handleNext = () => {
    if (!medicoSelezionato) return;
    navigate("/paziente/prenota/data");
  };

  if (loading) {
    return <p>Caricamento medici...</p>;
  }

  return (
    <div className="prenotazione-container">
      <h1>Seleziona il medico</h1>

      {errore && <p className="error-message">{errore}</p>}

      <div className="steps-grid">
        {medici.length > 0 ? (
          medici.map((medico) => (
            <div
              key={medico.id}
              className={`step-card ${
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

      {/* 🔙 Pulsante Indietro + Continua */}
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
          disabled={!medicoSelezionato}
          onClick={handleNext}
        >
          Continua
        </button>
      </div>
    </div>
  );
}
