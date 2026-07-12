import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import SidebarMedico from "../../components/SidebarMedico";
import TopbarMedico from "../../components/TopbarMedico";
import "../../styles/medico/ListaVisiteMedico.css";

export default function ListaVisiteMedico() {
  const [visite, setVisite] = useState([]);
  const [loading, setLoading] = useState(true);
  const [errore, setErrore] = useState(null);

  const navigate = useNavigate();

  useEffect(() => {
    const fetchVisite = async () => {
      const token = localStorage.getItem("token");

      // 🔒 Blindatura: token mancante → errore
      if (!token) {
        setErrore("Token mancante. Effettua nuovamente il login.");
        setLoading(false);
        return;
      }

      try {
        const res = await fetch("http://localhost:8080/appuntamenti/medico", {
          headers: { Authorization: `Bearer ${token}` },
        });

        if (!res.ok) {
          throw new Error("Errore nella risposta del server");
        }

        const data = await res.json();
        setVisite(data || []);
      } catch (err) {
        console.error("Errore caricamento visite:", err);
        setErrore("Errore nel caricamento delle visite.");
      } finally {
        setLoading(false);
      }
    };

    fetchVisite();
  }, []);

  // ---------------------------------------------------------
  // RENDER
  // ---------------------------------------------------------

  if (loading) return <p className="loading-message">Caricamento visite...</p>;

  if (errore) return <p className="error-message">{errore}</p>;

  return (
    <div className="layout-medico">
      <SidebarMedico />

      <div className="lista-visite-container">
        <TopbarMedico />

        <h1 className="title">Gestione Visite</h1>

        {visite.length === 0 ? (
          <p className="empty-message">Nessuna visita trovata.</p>
        ) : (
          <table className="tabella-visite">
            <thead>
              <tr>
                <th>Paziente</th>
                <th>Tipo Visita</th>
                <th>Data</th>
                <th>Ora</th>
                <th>Stato</th>
                <th>Azioni</th>
              </tr>
            </thead>

            <tbody>
              {visite.map((v) => (
                <tr key={v.id}>
                  <td>{v.nomePaziente} {v.cognomePaziente}</td>
                  <td>{v.tipoVisita}</td>
                  <td>{v.dataAppuntamento}</td>
                  <td>{v.oraAppuntamento}</td>
                  <td>{v.stato}</td>

                  <td>
                    <button
                      className="btn-dettagli"
                      onClick={() => navigate(`/medico/visite/${v.id}`)}
                    >
                      Dettagli
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}
