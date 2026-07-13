import React, { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { toast } from "react-toastify";
import "../../styles/admin/eliminapaziente.css";

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
        toast.error("Sessione scaduta. Effettua di nuovo il login.");
        setLoading(false);
        return;
      }

      if (res.status === 404) {
        toast.error("Paziente non trovato.");
        setLoading(false);
        return;
      }

      if (!res.ok) {
        const text = await res.text();

        if (text.includes("PAZIENTE_CON_APPUNTAMENTI")) {
          toast.error("Impossibile eliminare: il paziente ha appuntamenti attivi.");
        } else {
          toast.error("Errore durante l'eliminazione del paziente.");
        }

        setLoading(false);
        return;
      }

      // ⭐ Eliminazione riuscita
      toast.success("Paziente eliminato con successo!");

      setTimeout(() => {
        navigate("/admin/pazienti");
      }, 600);

    } catch (err) {
      console.error("Errore:", err);
      toast.error("Errore di connessione al server.");
      setLoading(false);
    }
  };

  return (
    <div className="confirm-container">
      <div className="confirm-box">
        <h2>Conferma Eliminazione</h2>
        <p>Sei sicuro di voler eliminare questo paziente?</p>

        {error && <div className="error-box">{error}</div>}

        <div className="button-group">
          <button
            className="btn-delete"
            onClick={eliminaPaziente}
            disabled={loading}
          >
            {loading ? "Eliminazione in corso..." : "Conferma"}
          </button>

          <button
            className="btn-cancel"
            onClick={() => navigate("/admin/pazienti")}
            disabled={loading}
          >
            Annulla
          </button>
        </div>
      </div>
    </div>
  );
}
