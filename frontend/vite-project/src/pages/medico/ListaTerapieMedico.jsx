import React, { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

import SidebarMedico from "../../components/SidebarMedico";
import TopbarMedico from "../../components/TopbarMedico";

import "../../styles/medico/listaTerapieMedico.css";

export default function ListaTerapieMedico() {
  const [terapie, setTerapie] = useState([]);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(true);

  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem("token");
    const medicoId = localStorage.getItem("idMedico");

    if (!token || !medicoId) {
      setError("Sessione scaduta. Effettua nuovamente il login.");
      setLoading(false);
      return;
    }

    const fetchTerapie = async () => {
      try {
        const res = await axios.get(
          `http://localhost:8080/api/terapie/medico/${medicoId}`,
          { headers: { Authorization: `Bearer ${token}` } }
        );

        const data = res.data;
        setTerapie(Array.isArray(data) ? data : []);
      } catch (err) {
        console.error("Errore nel caricamento delle terapie:", err);
        setError("Errore nel caricamento delle terapie.");
      } finally {
        setLoading(false);
      }
    };

    fetchTerapie();
  }, []);

  if (loading) return <p className="loading-message">Caricamento terapie...</p>;
  if (error) return <p className="error-message">{error}</p>;

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
