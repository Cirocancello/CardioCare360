import { useParams, useNavigate } from "react-router-dom";
import { useState, useEffect } from "react";

export default function ConfermaAnnullamento() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [loading, setLoading] = useState(false);
  const [errore, setErrore] = useState(null);
  const [controllo, setControllo] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem("token");

    if (!token) {
      setErrore("Token non trovato. Effettua di nuovo il login.");
      setControllo(false);
      return;
    }

    // 🔹 Controllo stato appuntamento prima di annullare
    async function checkAppuntamento() {
      try {
        const res = await fetch(`http://localhost:8080/appuntamenti/${id}`, {
          headers: { Authorization: `Bearer ${token}` }
        });

        if (res.status === 403) {
          throw new Error("Non hai i permessi per annullare questo appuntamento");
        }

        if (!res.ok) {
          throw new Error("Errore nel recupero dell'appuntamento");
        }

        const data = await res.json();
        const stato = data.stato?.toLowerCase();

        if (stato === "completato" || stato === "annullato") {
          throw new Error("Questo appuntamento non può essere annullato");
        }
      } catch (err) {
        setErrore(err.message);
        setControllo(false);
      }
    }

    checkAppuntamento();
  }, [id]);

  async function handleConferma() {
    setLoading(true);
    setErrore(null);

    const token = localStorage.getItem("token");

    try {
      const res = await fetch(`http://localhost:8080/appuntamenti/${id}`, {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${token}`
        }
      });

      if (!res.ok) {
        const text = await res.text();
        throw new Error(text || "Non è possibile annullare questo appuntamento");
      }

      navigate("/paziente/appuntamenti");
    } catch (err) {
      setErrore(err.message);
      navigate("/paziente/appuntamenti");
    } finally {
      setLoading(false);
    }
  }

  function handleAnnulla() {
    navigate(`/paziente/appuntamenti/${id}`);
  }

  if (errore) {
    return <p className="error-message">{errore}</p>;
  }

  if (!controllo) {
    return <p className="error-message">Operazione non consentita.</p>;
  }

  return (
    <div className="page-container">
      <h1 className="page-title">Conferma Annullamento</h1>

      <div className="confirm-box">
        <p>Sei sicuro di voler annullare questo appuntamento?</p>

        <div className="btn-group">
          <button
            className="btn-danger"
            onClick={handleConferma}
            disabled={loading}
          >
            {loading ? "Annullamento..." : "Conferma annullamento"}
          </button>

          <button className="btn-secondary" onClick={handleAnnulla}>
            Torna indietro
          </button>
        </div>
      </div>
    </div>
  );
}
