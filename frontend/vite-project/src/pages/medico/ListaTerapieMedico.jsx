import React, { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

import SidebarMedico from "../../components/SidebarMedico";
import TopbarMedico from "../../components/TopbarMedico";

import "../../styles/medico/listaTerapieMedico.css";

export default function ListaTerapieMedico() {
  const [terapie, setTerapie] = useState([]);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem("token");
    const medicoId = localStorage.getItem("idMedico");

    axios
      .get(`http://localhost:8080/api/terapie/medico/${medicoId}`, {
        headers: { Authorization: `Bearer ${token}` },
      })
      .then((res) => setTerapie(res.data))
      .catch(() => setError("Errore nel caricamento delle terapie"));
  }, []);

  return (
    <div className="layout-medico">
      <SidebarMedico />

      <div className="lista-visite-container">
        <TopbarMedico />

        {/* Header con titolo e pulsante */}
        <div className="lista-terapie-header">
          <h1 className="lista-terapie-title">Lista Terapie</h1>
          <button
            className="btn-crea-terapia"
            onClick={() => navigate("/medico/terapie/crea")}
          >
            + Crea Terapia
          </button>
        </div>

        {error && <p className="error-message">{error}</p>}

        <table className="tabella-visite">
          <thead>
            <tr>
              <th>ID</th>
              <th>Paziente</th>
              <th>Farmaco</th>
              <th>Dosaggio</th>
              <th>Data Inizio</th>
              <th>Data Fine</th>
            </tr>
          </thead>

          <tbody>
            {terapie.length > 0 ? (
              terapie.map((t) => (
                <tr key={t.id}>
                  <td>{t.id}</td>
                  <td>{t.paziente?.nome} {t.paziente?.cognome}</td>
                  <td>{t.farmaco?.nome}</td>
                  <td>{t.dosaggio}</td>
                  <td>{t.dataInizio}</td>
                  <td>{t.dataFine || "-"}</td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan="6" className="no-terapie">
                  Nessuna terapia trovata.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
