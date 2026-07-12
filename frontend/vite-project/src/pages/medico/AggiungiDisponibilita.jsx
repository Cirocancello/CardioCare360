import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

import SidebarMedico from "../../components/SidebarMedico";
import TopbarMedico from "../../components/TopbarMedico";
import "../../styles/medico/AggiungiDisponibilita.css";

export default function AggiungiDisponibilita() {
  const [giornoSettimana, setGiornoSettimana] = useState("");
  const [oraInizio, setOraInizio] = useState("");
  const [oraFine, setOraFine] = useState("");
  const [errore, setErrore] = useState(null);
  const [loading, setLoading] = useState(false);

  const navigate = useNavigate();
  const token = localStorage.getItem("token");

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErrore(null);

    if (!token) {
      setErrore("Token mancante. Effettua nuovamente il login.");
      return;
    }

    if (!giornoSettimana || !oraInizio || !oraFine) {
      setErrore("Compila tutti i campi.");
      return;
    }

    if (oraFine <= oraInizio) {
      setErrore("L'orario di fine deve essere successivo all'orario di inizio.");
      return;
    }

    try {
      setLoading(true);

      const res = await fetch("http://localhost:8080/disponibilita", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          giornoSettimana,
          oraInizio,
          oraFine,
        }),
      });

      if (!res.ok) throw new Error("Errore durante la creazione della disponibilità");

      navigate("/medico/disponibilita");
    } catch (err) {
      console.error(err);
      setErrore("Errore: " + err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="layout-medico">
      <SidebarMedico />

      <div className="aggiungi-disponibilita-container">
        <TopbarMedico />

        <h1 className="title">Aggiungi disponibilità</h1>

        {errore && <p className="error-message">{errore}</p>}
        {loading && <p className="loading-message">Salvataggio in corso...</p>}

        <form className="form-disponibilita" onSubmit={handleSubmit}>
          <label>Giorno</label>
          <select
            value={giornoSettimana}
            onChange={(e) => setGiornoSettimana(e.target.value)}
            required
          >
            <option value="">Seleziona...</option>
            <option value="LUNEDI">Lunedì</option>
            <option value="MARTEDI">Martedì</option>
            <option value="MERCOLEDI">Mercoledì</option>
            <option value="GIOVEDI">Giovedì</option>
            <option value="VENERDI">Venerdì</option>
            <option value="SABATO">Sabato</option>
          </select>

          <label>Ora inizio</label>
          <input
            type="time"
            value={oraInizio}
            onChange={(e) => setOraInizio(e.target.value)}
            required
          />

          <label>Ora fine</label>
          <input
            type="time"
            value={oraFine}
            onChange={(e) => setOraFine(e.target.value)}
            required
          />

          <button type="submit" className="btn-salva" disabled={loading}>
            💾 Salva disponibilità
          </button>
        </form>
      </div>
    </div>
  );
}
