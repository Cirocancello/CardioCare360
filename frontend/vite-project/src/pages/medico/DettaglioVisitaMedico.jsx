import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";

import SidebarMedico from "../../components/SidebarMedico";
import TopbarMedico from "../../components/TopbarMedico";
import "../../styles/medico/DettaglioVisitaMedico.css";

export default function DettaglioVisitaMedico() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [visita, setVisita] = useState(null);
  const [loading, setLoading] = useState(true);
  const [errore, setErrore] = useState(null);

  useEffect(() => {
    const fetchVisita = async () => {
      const token = localStorage.getItem("token");

      // 🔒 Token mancante → errore
      if (!token) {
        setErrore("Token mancante. Effettua nuovamente il login.");
        setLoading(false);
        return;
      }

      // 🔒 ID mancante → errore
      if (!id) {
        setErrore("ID visita non valido.");
        setLoading(false);
        return;
      }

      try {
        const res = await fetch(`http://localhost:8080/appuntamenti/${id}`, {
          headers: { Authorization: `Bearer ${token}` },
        });

        if (!res.ok) {
          throw new Error("Errore nella risposta del server");
        }

        const data = await res.json();

        if (!data) {
          setErrore("Visita non trovata.");
          return;
        }

        setVisita(data);
      } catch (err) {
        console.error("Errore caricamento dettaglio visita:", err);
        setErrore("Errore nel caricamento della visita.");
      } finally {
        setLoading(false);
      }
    };

    fetchVisita();
  }, [id]);

  // ---------------------------------------------------------
  // RENDER
  // ---------------------------------------------------------

  if (loading) {
    return (
      <div className="layout-medico">
        <SidebarMedico />
        <div className="dettaglio-visita-container">
          <TopbarMedico />
          <h2>Caricamento in corso...</h2>
        </div>
      </div>
    );
  }

  if (errore) {
    return (
      <div className="layout-medico">
        <SidebarMedico />
        <div className="dettaglio-visita-container">
          <TopbarMedico />
          <p className="error-message">{errore}</p>
        </div>
      </div>
    );
  }

  return (
    <div className="layout-medico">
      <SidebarMedico />

      <div className="dettaglio-visita-container">
        <TopbarMedico />

        <h1 className="title">Dettaglio Visita</h1>

        <div className="card-visita">
          <p><strong>Paziente:</strong> {visita.paziente?.nomeCompleto || "—"}</p>
          <p><strong>Tipo Visita:</strong> {visita.tipoVisita || "—"}</p>
          <p><strong>Data:</strong> {visita.dataAppuntamento || "—"}</p>
          <p><strong>Ora:</strong> {visita.oraAppuntamento || "—"}</p>
          <p><strong>Stato:</strong> {visita.stato || "—"}</p>
          <p><strong>Note Paziente:</strong> {visita.notePaziente || "Nessuna nota"}</p>
          <p><strong>Note Medico:</strong> {visita.noteMedico || "Nessuna nota"}</p>
        </div>

        <div className="azioni-visita">
          <button
            className="btn-indietro"
            onClick={() => navigate("/medico/visite")}
          >
            ← Torna alla lista
          </button>

          <button
            className="btn-completa"
            onClick={() => alert("Funzionalità in sviluppo")}
          >
            Completa Visita
          </button>
        </div>
      </div>
    </div>
  );
}
