import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";

import SidebarPaziente from "../../components/SidebarPaziente.jsx";
import TopbarPaziente from "../../components/TopbarPaziente.jsx";

import "../../styles/paziente/Appuntamenti.css";

export default function Appuntamenti() {
  const [appuntamenti, setAppuntamenti] = useState([]);
  const [loading, setLoading] = useState(true);
  const [errore, setErrore] = useState(null);

  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (!token) {
      setErrore("Token non trovato. Effettua di nuovo il login.");
      setLoading(false);
      return;
    }

    fetch("http://localhost:8080/appuntamenti/paziente", {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then(res => {
        if (!res.ok) throw new Error("Errore nel caricamento degli appuntamenti");
        return res.json();
      })
      .then(async data => {
        if (!Array.isArray(data)) {
          setAppuntamenti([]);
          return;
        }

        const enriched = await Promise.all(
          data.map(async app => {
            if (!app.idMedico) return { ...app, medico: null };

            try {
              const resMedico = await fetch(`http://localhost:8080/medici/${app.idMedico}`, {
                headers: { Authorization: `Bearer ${token}` },
              });

              const medico = resMedico.ok ? await resMedico.json() : null;
              return { ...app, medico };
            } catch {
              return { ...app, medico: null };
            }
          })
        );

        setAppuntamenti(enriched);
      })
      .catch(err => {
        console.error(err);
        setErrore("Impossibile caricare gli appuntamenti.");
      })
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <p className="loading">Caricamento appuntamenti...</p>;
  if (errore) return <p className="error-message">{errore}</p>;

  return (
    <div className="layout-paziente">

      {/* Sidebar */}
      <SidebarPaziente />

      <div className="appuntamenti-container">

        {/* Topbar */}
        <TopbarPaziente />

        {/* Titolo */}
        <h1 className="page-title">I tuoi appuntamenti</h1>

        

        <div className="appointments-grid">
          {appuntamenti.length > 0 ? (
            appuntamenti.map(app => {
              const stato = app.stato?.toLowerCase() || "sconosciuto";
              const medicoNome =
                app.medico?.nomeCompleto ||
                (app.idMedico ? `Medico #${app.idMedico}` : "Non disponibile");

              const isModificabile =
                stato === "prenotato" || stato === "confermato";

              return (
                <div key={app.id} className="appointment-card">
                  <div className="appointment-header">
                    <h3>
                      {app.dataAppuntamento} – {app.oraAppuntamento}
                    </h3>
                    <span className={`status ${stato}`}>{app.stato}</span>
                  </div>

                  <div className="appointment-body">
                    <p><strong>Medico:</strong> {medicoNome}</p>
                    <p><strong>Tipo visita:</strong> {app.tipoVisita || "Non specificato"}</p>
                  </div>

                  <div className="appointment-actions">
                    <Link
                      to={`/paziente/appuntamenti/${app.id}`}
                      className="btn-primary"
                    >
                      Dettagli
                    </Link>

                    {isModificabile && (
                      <>
                        <Link
                          to={`/paziente/appuntamenti/${app.id}/modifica`}
                          className="btn-secondary"
                        >
                          Modifica
                        </Link>

                        <Link
                          to={`/paziente/appuntamenti/${app.id}/annulla`}
                          className="btn-danger"
                        >
                          Annulla
                        </Link>
                      </>
                    )}
                  </div>
                </div>
              );
            })
          ) : (
            <p>Nessun appuntamento trovato.</p>
          )}
        </div>
      </div>
    </div>
  );
}
