import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import SidebarPaziente from "../../components/SidebarPaziente.jsx";
import TopbarPaziente from "../../components/TopbarPaziente.jsx";

import "../../styles/paziente/Appuntamenti.css"; // 🔥 layout paziente

const PrenotaEsame = () => {
  const [tipoEsame, setTipoEsame] = useState("");
  const [disponibilita, setDisponibilita] = useState(null);
  const [loadingDisponibilita, setLoadingDisponibilita] = useState(false);

  const [note, setNote] = useState("");

  const [medici, setMedici] = useState([]);
  const [medicoSelezionato, setMedicoSelezionato] = useState("");

  const [error, setError] = useState(null);

  const user = JSON.parse(localStorage.getItem("user"));
  const token = localStorage.getItem("token");

  const navigate = useNavigate();

  const fetchDisponibilita = async (tipo) => {
    if (!tipo) return;

    try {
      setLoadingDisponibilita(true);
      setError(null);

      const response = await fetch(
        `http://localhost:8080/esami/disponibilita/prossima?tipo=${tipo}`,
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );

      if (!response.ok) throw new Error("Errore nel calcolo della disponibilità");

      const data = await response.json();
      setDisponibilita(data);
    } catch (error) {
      console.error(error);
      setError("Impossibile calcolare la disponibilità.");
    } finally {
      setLoadingDisponibilita(false);
    }
  };

  const fetchMedici = async (tipo) => {
  if (!tipo) return;

  try {
    const response = await fetch(
      `http://localhost:8080/medico/esami?tipo=${tipo}`,
      {
        headers: { Authorization: `Bearer ${token}` },
      }
    );

    if (!response.ok) throw new Error("Errore nel caricamento dei medici");

    const data = await response.json();
    setMedici(data || []);
  } catch (error) {
    console.error(error);
    setError("Impossibile caricare i medici disponibili.");
  }
};


  useEffect(() => {
    if (tipoEsame !== "") {
      fetchDisponibilita(tipoEsame);
      fetchMedici(tipoEsame);
    } else {
      setDisponibilita(null);
      setMedici([]);
      setMedicoSelezionato("");
    }
  }, [tipoEsame]);

  const prenotaEsame = async () => {
    setError(null);

    if (!user || !user.idPaziente) {
      setError("Utente non valido. Effettua nuovamente il login.");
      return;
    }

    if (!tipoEsame) {
      setError("Seleziona un tipo di esame.");
      return;
    }

    if (!medicoSelezionato) {
      setError("Seleziona un medico.");
      return;
    }

    if (!disponibilita) {
      setError("Disponibilità non trovata.");
      return;
    }

    const body = {
      idPaziente: user.idPaziente,
      idMedico: medicoSelezionato,
      tipoEsame,
      dataEsame: disponibilita.data,
      oraEsame: disponibilita.ora,
      note,
    };

    try {
      const response = await fetch("http://localhost:8080/esami/prenota", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(body),
      });

      if (!response.ok) throw new Error("Errore durante la prenotazione dell'esame");

      const result = await response.json();

      navigate("/paziente/prenota-esame/confermata", {
        state: { esame: result },
      });
    } catch (error) {
      console.error(error);
      setError("Errore durante la prenotazione dell'esame.");
    }
  };

  return (
    <div className="layout-paziente">

      {/* Sidebar */}
      <SidebarPaziente />

      {/* Contenuto */}
      <div className="appuntamenti-container">

        {/* Topbar */}
        <TopbarPaziente />

        <h1 className="page-title">Prenota Esame</h1>

        {error && <p className="error-message">{error}</p>}

        {/* Tipo esame */}
        <div className="form-group">
          <label>Tipo di esame</label>
          <select
            value={tipoEsame}
            onChange={(e) => setTipoEsame(e.target.value)}
          >
            <option value="">Seleziona esame</option>
            <option value="ECG">ECG</option>
            <option value="HOLTER">Holter</option>
            <option value="ECOCARDIOGRAMMA">Ecocardiogramma</option>
          </select>
        </div>

        {/* Medico dinamico */}
        {medici.length > 0 && (
          <div className="form-group">
            <label>Medico</label>
            <select
              value={medicoSelezionato}
              onChange={(e) => setMedicoSelezionato(e.target.value)}
            >
              <option value="">Seleziona medico</option>
              {medici.map((m) => (
                <option key={m.id} value={m.id}>
                  {m.nomeCompleto}
                </option>
              ))}
            </select>
          </div>
        )}

        {loadingDisponibilita && <p>Calcolo disponibilità...</p>}

        {disponibilita && (
          <div className="box-disponibilita">
            <h4>Prossima disponibilità</h4>
            <p><strong>Data:</strong> {disponibilita.data}</p>
            <p><strong>Ora:</strong> {disponibilita.ora}</p>
          </div>
        )}

        <div className="form-group">
          <label>Note (opzionali)</label>
          <textarea
            value={note}
            onChange={(e) => setNote(e.target.value)}
            placeholder="Inserisci eventuali note..."
          />
        </div>

        <button
          disabled={!disponibilita || !medicoSelezionato}
          onClick={prenotaEsame}
          className="btn-primary"
        >
          Conferma prenotazione
        </button>

      </div>
    </div>
  );
};

export default PrenotaEsame;
