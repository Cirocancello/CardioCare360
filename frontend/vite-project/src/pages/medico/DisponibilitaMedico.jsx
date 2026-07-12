import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import SidebarMedico from "../../components/SidebarMedico";
import TopbarMedico from "../../components/TopbarMedico";
import "../../styles/medico/DisponibilitaMedico.css";

export default function DisponibilitaMedico() {
  const [disponibilita, setDisponibilita] = useState([]);
  const [loading, setLoading] = useState(true);
  const [errore, setErrore] = useState(null);

  const navigate = useNavigate();

  const idMedico = localStorage.getItem("idMedico");
  const token = localStorage.getItem("token");

  useEffect(() => {
    fetchDisponibilita();
  }, []);

  const fetchDisponibilita = async () => {
    setErrore(null);

    if (!token) {
      setErrore("Token mancante. Effettua nuovamente il login.");
      setLoading(false);
      return;
    }

    try {
      const res = await fetch(`http://localhost:8080/disponibilita/medico`, {
        headers: { Authorization: `Bearer ${token}` },
      });

      if (!res.ok) throw new Error("Errore nel caricamento delle disponibilità");

      const data = await res.json();
      setDisponibilita(data || []);
    } catch (err) {
      console.error(err);
      setErrore("Errore durante il caricamento delle disponibilità.");
    } finally {
      setLoading(false);
    }
  };

  const eliminaDisponibilita = async (id) => {
    if (!id) return;

    if (!window.confirm("Sei sicuro di voler eliminare questa disponibilità?")) {
      return;
    }

    try {
      const res = await fetch(`http://localhost:8080/disponibilita/${id}`, {
        method: "DELETE",
        headers: { Authorization: `Bearer ${token}` },
      });

      if (!res.ok) throw new Error("Errore durante l'eliminazione");

      fetchDisponibilita();
    } catch (err) {
      console.error(err);
      setErrore("Errore durante l'eliminazione della disponibilità.");
    }
  };

  // ---------------------------------------------------------
  // RENDER
  // ---------------------------------------------------------

  if (loading) {
    return (
      <div className="layout-medico">
        <SidebarMedico />
        <div className="disponibilita-container">
          <TopbarMedico />
          <p className="loading-message">Caricamento disponibilità...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="layout-medico">
      <SidebarMedico />

      <div className="disponibilita-container">
        <TopbarMedico />

        <h1 className="title">Le mie disponibilità</h1>

        {errore && <p className="error-message">{errore}</p>}

        <button
          className="btn-aggiungi"
          onClick={() => navigate("/medico/disponibilita/aggiungi")}
        >
          ➕ Aggiungi disponibilità
        </button>

        <table className="tabella-disponibilita">
          <thead>
            <tr>
              <th>Giorno</th>
              <th>Ora inizio</th>
              <th>Ora fine</th>
              <th>Azioni</th>
            </tr>
          </thead>

          <tbody>
            {disponibilita.length === 0 ? (
              <tr>
                <td colSpan="4" className="vuoto">
                  Nessuna disponibilità presente
                </td>
              </tr>
            ) : (
              disponibilita.map((d) => (
                <tr key={d.id}>
                  <td>{d.giorno || "—"}</td>
                  <td>{d.oraInizio || "—"}</td>
                  <td>{d.oraFine || "—"}</td>

                  <td>
                    <button
                      className="btn-elimina"
                      onClick={() => eliminaDisponibilita(d.id)}
                    >
                      🗑 Elimina
                    </button>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
