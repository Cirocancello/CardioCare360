import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "../../styles/paziente/prenotazione.css";

const PrenotaEsame = () => {
  const [tipoEsame, setTipoEsame] = useState("");
  const [disponibilita, setDisponibilita] = useState(null);
  const [loadingDisponibilita, setLoadingDisponibilita] = useState(false);
  const [note, setNote] = useState("");

  const [medici, setMedici] = useState([]);
  const [medicoSelezionato, setMedicoSelezionato] = useState("");

  const idPaziente = localStorage.getItem("idPaziente");
  const navigate = useNavigate();

  const fetchDisponibilita = async (tipo) => {
    try {
      setLoadingDisponibilita(true);
      const response = await fetch(
        `http://localhost:8080/esami/disponibilita/prossima?tipo=${tipo}`,
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        }
      );

      if (!response.ok) throw new Error("Errore disponibilità");
      const data = await response.json();
      setDisponibilita(data);
    } catch (error) {
      console.error("Errore nel calcolo disponibilità", error);
      setDisponibilita(null);
    } finally {
      setLoadingDisponibilita(false);
    }
  };

  const fetchMedici = async (tipo) => {
    try {
      const response = await fetch(
        `http://localhost:8080/medico/esami?tipo=${tipo}`,
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        }
      );

      if (!response.ok) throw new Error("Errore medici");
      const data = await response.json();
      setMedici(data);
    } catch (error) {
      console.error("Errore nel caricamento medici", error);
      setMedici([]);
    }
  };

  useEffect(() => {
    if (tipoEsame !== "") {
      fetchDisponibilita(tipoEsame);
      fetchMedici(tipoEsame);
    }
  }, [tipoEsame]);

  const prenotaEsame = async () => {
    if (!idPaziente) {
      alert("Errore: utente non trovato. Effettua nuovamente il login.");
      return;
    }

    if (!medicoSelezionato) {
      alert("Seleziona un medico.");
      return;
    }

    const body = {
      idPaziente,
      idMedico: medicoSelezionato,
      tipoEsame,
      dataEsame: disponibilita.data,
      oraEsame: disponibilita.ora,
      note,
    };

    const response = await fetch("http://localhost:8080/esami/prenota", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${localStorage.getItem("token")}`,
      },
      body: JSON.stringify(body),
    });

    const result = await response.json();
    navigate("/paziente/prenota-esame/confermata", {
      state: { esame: result },
    });
  };

  return (
    <div className="visita-container">
      <h2>Prenota Esame</h2>
      <p className="visita-descrizione">
        Seleziona il tipo di esame, il medico e conferma la prenotazione.
      </p>

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
      {medici.length > 0 ? (
        <div className="form-group">
          <label>Medico disponibile</label>
          <select
            value={medicoSelezionato}
            onChange={(e) => setMedicoSelezionato(e.target.value)}
          >
            <option value="">Seleziona medico</option>
            {medici.map((m) => (
              <option key={m.id} value={m.id}>
                {m.nomeCompleto} — {m.specializzazione}
              </option>
            ))}
          </select>
        </div>
      ) : (
        tipoEsame && (
          <p className="info-message">
            Nessun medico disponibile per questo esame.
          </p>
        )
      )}

      {/* Disponibilità */}
      {loadingDisponibilita && <p>Calcolo disponibilità...</p>}

      {disponibilita && (
        <div className="box-disponibilita">
          <h4>Prossima disponibilità</h4>
          <p><strong>Data:</strong> {disponibilita.data}</p>
          <p><strong>Ora:</strong> {disponibilita.ora}</p>
        </div>
      )}

      {/* Note */}
      <div className="form-group">
        <label>Note (opzionali)</label>
        <textarea
          value={note}
          onChange={(e) => setNote(e.target.value)}
          placeholder="Inserisci eventuali note..."
        />
      </div>

      {/* Pulsante conferma */}
      <button
        disabled={!disponibilita || !medicoSelezionato}
        onClick={prenotaEsame}
        className="btn-continua"
      >
        Conferma prenotazione
      </button>

      {/* 🔙 Pulsante torna indietro */}
      <button
        onClick={() => navigate(-1)}
        style={{
          marginTop: "25px",
          padding: "10px 16px",
          borderRadius: "8px",
          backgroundColor: "#e0e0e0",
          border: "none",
          cursor: "pointer",
          fontSize: "15px"
        }}
      >
        Torna indietro
      </button>

    </div>
  );
};

export default PrenotaEsame;
