import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

import SidebarPaziente from "../../components/SidebarPaziente.jsx";
import TopbarPaziente from "../../components/TopbarPaziente.jsx";

import "../../styles/paziente/Appuntamenti.css";

export default function SelezionaMedico() {
  const navigate = useNavigate();
  const [medici, setMedici] = useState([]);
  const [medicoSelezionato, setMedicoSelezionato] = useState("");
  const [loading, setLoading] = useState(true);
  const [errore, setErrore] = useState("");

  useEffect(() => {
    const visita = localStorage.getItem("specializzazioneVisita");
    const token = localStorage.getItem("token");

    if (!visita) {
      setErrore("Specializzazione non selezionata.");
      navigate("/paziente/prenota/visita");
      return;
    }

    if (!token) {
      setErrore("Token mancante. Effettua di nuovo il login.");
      setLoading(false);
      return;
    }

    const fetchMedici = async () => {
      try {
        const res = await axios.get(
          `http://localhost:8080/medico/visita/${visita}`, // 🔥 CORRETTO
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        );
        setMedici(res.data || []);
      } catch (err) {
        console.error("Errore nel caricamento medici:", err);
        setErrore("Errore nel caricamento medici. Riprova più tardi.");
      } finally {
        setLoading(false);
      }
    };

    fetchMedici();
  }, [navigate]);

  const handleNext = () => {
    if (!medicoSelezionato) return;
    localStorage.setItem("idMedicoSelezionato", medicoSelezionato);
    navigate("/paziente/prenota/data");
  };

  if (loading) {
    return <p>Caricamento medici...</p>;
  }

  return (
    <div className="layout-paziente">
      <SidebarPaziente />

      <div className="appuntamenti-container">
        <TopbarPaziente />

        <h1 className="page-title">Seleziona il medico</h1>

        {errore && <p className="error-message">{errore}</p>}

        <div className="appointments-grid">
          {medici.length > 0 ? (
            medici.map((medico) => (
              <div
                key={medico.id}
                className={`appointment-card ${
                  medicoSelezionato === medico.id ? "selected" : ""
                }`}
                onClick={() => setMedicoSelezionato(medico.id)}
                style={{ cursor: "pointer" }}
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
    </div>
  );
}
