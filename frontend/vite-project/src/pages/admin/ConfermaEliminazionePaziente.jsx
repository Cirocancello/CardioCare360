import React, { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";

export default function ConfermaEliminazionePaziente() {
  const { id } = useParams();
  const token = localStorage.getItem("token");
  const navigate = useNavigate();

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const eliminaPaziente = async () => {
    setLoading(true);
    setError("");

    try {
      const res = await fetch(`http://localhost:8080/admin/pazienti/${id}`, {
        method: "DELETE",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
      });

      if (res.status === 401) {
        setError("Sessione scaduta. Effettua di nuovo il login.");
        setLoading(false);
        return;
      }

      if (res.status === 404) {
        setError("Paziente non trovato.");
        setLoading(false);
        return;
      }

      if (!res.ok) {
        const text = await res.text();

        if (text.includes("PAZIENTE_CON_APPUNTAMENTI")) {
          setError("Impossibile eliminare: il paziente ha appuntamenti attivi.");
        } else {
          setError("Errore durante l'eliminazione del paziente.");
        }

        setLoading(false);
        return;
      }

      // Eliminazione riuscita
      navigate("/admin/pazienti");

    } catch (err) {
      console.error("Errore:", err);
      setError("Errore di connessione al server.");
      setLoading(false);
    }
  };

  return (
    <div className="confirm-container">
      <div className="confirm-box">
        <h2>Conferma Eliminazione</h2>
        <p>Sei sicuro di voler eliminare questo paziente?</p>

        {/* ⭐ Messaggio di errore */}
        {error && <div className="error-box">{error}</div>}

        <button
          className="btn-delete"
          onClick={eliminaPaziente}
          disabled={loading}
        >
          {loading ? "Eliminazione in corso..." : "Conferma"}
        </button>

        <button
          className="btn-edit"
          onClick={() => navigate("/admin/pazienti")}
          disabled={loading}
        >
          Annulla
        </button>
      </div>
    </div>
  );
}
