import { useParams, useNavigate } from "react-router-dom";
import { useState } from "react";

export default function ConfermaAnnullamento() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);

  async function handleConferma() {
    setLoading(true);

    const token = localStorage.getItem("token");

    const res = await fetch(`http://localhost:8080/appuntamenti/${id}`, {
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${token}`
      }
    });

    setLoading(false);

    if (res.ok) {
      navigate("/paziente/appuntamenti");
    } else {
      alert("Non è possibile annullare questo appuntamento");
      navigate("/paziente/appuntamenti");
    }
  }

  function handleAnnulla() {
    navigate(`/paziente/appuntamenti/${id}`);
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
