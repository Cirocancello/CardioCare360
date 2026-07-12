import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";

import SidebarMedico from "../../components/SidebarMedico";
import TopbarMedico from "../../components/TopbarMedico";
import "../../styles/medico/refertaEsame.css";

export default function RefertaEsame() {
  const { idEsame } = useParams();
  const navigate = useNavigate();

  const [esame, setEsame] = useState(null);
  const [noteMedico, setNoteMedico] = useState("");
  const [file, setFile] = useState(null);

  const [loading, setLoading] = useState(true);
  const [errore, setErrore] = useState(null);
  const [successo, setSuccesso] = useState(null);

  // ---------------------------------------------------------
  // 🔍 CARICA DETTAGLI ESAME (blindato)
  // ---------------------------------------------------------
  useEffect(() => {
    const fetchEsame = async () => {
      const token = localStorage.getItem("token");

      if (!token) {
        setErrore("Token mancante. Effettua nuovamente il login.");
        setLoading(false);
        return;
      }

      if (!idEsame) {
        setErrore("ID esame non valido.");
        setLoading(false);
        return;
      }

      try {
        const response = await axios.get(
          `http://localhost:8080/esami/${idEsame}`,
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        );

        if (!response.data) {
          setErrore("Esame non trovato.");
          return;
        }

        setEsame(response.data);
      } catch (err) {
        console.error(err);
        setErrore("Errore nel caricamento dell'esame.");
      } finally {
        setLoading(false);
      }
    };

    fetchEsame();
  }, [idEsame]);

  // ---------------------------------------------------------
  // 🔥 SALVA REFERTAZIONE (blindato)
  // ---------------------------------------------------------
  const handleSubmit = async (e) => {
    e.preventDefault();
    setErrore(null);
    setSuccesso(null);

    const token = localStorage.getItem("token");
    const medicoId = localStorage.getItem("idMedico");

    if (!token) {
      setErrore("Token mancante. Effettua nuovamente il login.");
      return;
    }

    if (!medicoId) {
      setErrore("Errore: medico non riconosciuto. Riesegui il login.");
      return;
    }

    if (!noteMedico.trim()) {
      setErrore("Inserisci le note del medico.");
      return;
    }

    if (!file) {
      setErrore("Carica un file PDF per il referto.");
      return;
    }

    try {
      // 1️⃣ Salva referto
      const formData = new FormData();
      formData.append("medicoId", medicoId);
      formData.append("noteMedico", noteMedico);
      formData.append("file", file);

      await axios.post(
        `http://localhost:8080/referti/esame/${idEsame}`,
        formData,
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );

      // 2️⃣ Aggiorna stato esame → REFERTATO
      await axios.put(
        `http://localhost:8080/esami/${idEsame}/stato?nuovoStato=REFERTATO`,
        {},
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );

      // 3️⃣ Messaggio di successo
      setSuccesso("Referto salvato correttamente!");

      // 4️⃣ Redirect dopo 1.5s
      setTimeout(() => {
        navigate("/medico/esami");
      }, 1500);
    } catch (err) {
      console.error(err);
      setErrore("Errore durante il salvataggio del referto.");
    }
  };

  // ---------------------------------------------------------
  // UI
  // ---------------------------------------------------------
  if (loading) return <p className="loading-message">Caricamento...</p>;
  if (errore) return <p className="error-message">{errore}</p>;

  return (
    <div className="layout-medico">
      <SidebarMedico />

      <div className="dashboard-medico-container">
        <TopbarMedico />

        <div className="referta-container">
          <h2 className="referta-title">
            Referta Esame #{idEsame} — {esame?.tipoEsame || "—"}
          </h2>

          {successo && <p className="success-message">{successo}</p>}
          {errore && <p className="error-message">{errore}</p>}

          <form className="referta-form" onSubmit={handleSubmit}>
            <label>Note del Medico</label>
            <textarea
              rows="5"
              value={noteMedico}
              onChange={(e) => setNoteMedico(e.target.value)}
              required
            ></textarea>

            <label>Carica PDF Referto</label>
            <input
              type="file"
              accept="application/pdf"
              onChange={(e) => setFile(e.target.files[0])}
              required
            />

            <button type="submit" className="btn-salva-referto">
              Salva Referto
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}
