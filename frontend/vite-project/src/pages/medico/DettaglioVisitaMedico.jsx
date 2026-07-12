import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import SidebarMedico from "../../components/SidebarMedico";
import TopbarMedico from "../../components/TopbarMedico";
import "../../styles/medico/DettaglioVisitaMedico.css";

export default function DettaglioVisitaMedico() {
  const { id } = useParams();
  const [visita, setVisita] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem("token");

    fetch(`http://localhost:8080/appuntamenti/${id}`, {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then((res) => res.json())
      .then((data) => setVisita(data))
      .catch((err) => console.error("Errore caricamento dettaglio visita:", err));
  }, [id]);

  if (!visita) {
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

  const completaVisita = async () => {
  const token = localStorage.getItem("token");

  try {
    await fetch(`http://localhost:8080/appuntamenti/${id}/completa`, {
      method: "PUT",
      headers: { Authorization: `Bearer ${token}` },
    });

    alert("Visita completata con successo!");
    navigate("/medico/visite");
  } catch (err) {
    console.error("Errore completamento visita:", err);
    alert("Errore durante il completamento della visita");
  }
};


  return (
    <div className="layout-medico">
      <SidebarMedico />
      <div className="dettaglio-visita-container">
        <TopbarMedico />
        <h1 className="title">Dettaglio Visita</h1>

        <div className="card-visita">
          <p><strong>Paziente:</strong> {visita.paziente?.nomeCompleto}</p>
          <p><strong>Tipo Visita:</strong> {visita.tipoVisita}</p>
          <p><strong>Data:</strong> {visita.dataAppuntamento}</p>
          <p><strong>Ora:</strong> {visita.oraAppuntamento}</p>
          <p><strong>Stato:</strong> {visita.stato}</p>
          <p><strong>Note Paziente:</strong> {visita.notePaziente || "Nessuna nota"}</p>
          <p><strong>Note Medico:</strong> {visita.noteMedico || "Nessuna nota"}</p>
        </div>

        <div className="azioni-visita">
          <button className="btn-indietro" onClick={() => navigate("/medico/visite")}>
            ← Torna alla lista
          </button>
          <button className="btn-completa" onClick={completaVisita}>
            Completa Visita
          </button>

        </div>
      </div>
    </div>
  );
}
