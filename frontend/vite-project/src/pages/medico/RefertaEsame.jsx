import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import SidebarMedico from "../../components/SidebarMedico";
import TopbarMedico from "../../components/TopbarMedico";
import "../../styles/medico/refertaEsame.css";

export default function RefertaEsame() {
  const { idEsame } = useParams();
  const navigate = useNavigate();

  // Stato inizializzato correttamente
  const [esame, setEsame] = useState({
    tipoEsame: "",
    notePaziente: "",
    stato: "",
    dataEsame: "",
    oraEsame: ""
  });

  const [noteMedico, setNoteMedico] = useState("");
  const [file, setFile] = useState(null);

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  // ---------------------------------------------------------
  // 🔍 CARICA DETTAGLI ESAME
  // ---------------------------------------------------------
  useEffect(() => {
    const fetchEsame = async () => {
      try {
        const token = localStorage.getItem("token");

        const response = await axios.get(
          `http://localhost:8080/esami/${idEsame}`,
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        );

        // Imposta esame con valori sicuri
        setEsame({
          tipoEsame: response.data.tipoEsame || "",
          notePaziente: response.data.notePaziente || "",
          stato: response.data.stato || "",
          dataEsame: response.data.dataEsame || "",
          oraEsame: response.data.oraEsame || ""
        });

      } catch (err) {
        console.error(err);
        setError("Errore nel caricamento dell'esame");
      } finally {
        setLoading(false);
      }
    };

    fetchEsame();
  }, [idEsame]);

  // ---------------------------------------------------------
  // 🔥 SALVA REFERTAZIONE
  // ---------------------------------------------------------
  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const token = localStorage.getItem("token");
      const medicoId = localStorage.getItem("idMedico");

      if (!medicoId) {
        setError("Errore: medicoId non trovato. Riesegui il login.");
        return;
      }

      if (!file) {
        setError("Carica un file PDF prima di salvare.");
        return;
      }

      // 1️⃣ Salva il referto (note + PDF)
      const formData = new FormData();
      formData.append("medicoId", medicoId);
      formData.append("noteMedico", noteMedico || "");
      formData.append("file", file);

      await axios.post(
        `http://localhost:8080/referti/esame/${idEsame}`,
        formData,
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );

      // 2️⃣ Aggiorna lo stato dell’esame → REFERTATO
      await axios.put(
        `http://localhost:8080/esami/${idEsame}/stato?nuovoStato=REFERTATO`,
        {},
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );

      // ⭐ 3️⃣ Mostra messaggio di successo
      setSuccess("Referto salvato correttamente!");

      // ⭐ 4️⃣ Redirect dopo 1.5 secondi
      setTimeout(() => {
        navigate("/medico/esami");
      }, 1500);

    } catch (err) {
      console.error(err);
      setError("Errore durante il salvataggio del referto");
    }
  };

  // ---------------------------------------------------------
  // UI
  // ---------------------------------------------------------
  if (loading) return <p className="loading-message">Caricamento...</p>;
  if (error) return <p className="error-message">{error}</p>;

  return (
    <div className="layout-medico">
      <SidebarMedico />

      <div className="dashboard-medico-container">
        <TopbarMedico />

        <div className="referta-container">
          <h2 className="referta-title">
            Referta Esame #{idEsame} — {esame.tipoEsame}
          </h2>

          {success && <p className="success-message">{success}</p>}

          <form className="referta-form" onSubmit={handleSubmit}>
            <label>Note del Medico</label>
            <textarea
              rows="5"
              value={noteMedico || ""}
              onChange={(e) => setNoteMedico(e.target.value)}
              required
            ></textarea>

            <label>Carica PDF Referto</label>
            <input
              type="file"
              accept="application/pdf"
              onChange={(e) => setFile(e.target.files[0] || null)}
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
