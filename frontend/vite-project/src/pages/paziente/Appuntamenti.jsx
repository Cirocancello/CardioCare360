import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import "../../styles/paziente/Appuntamenti.css";

export default function Appuntamenti() {
  const [appuntamenti, setAppuntamenti] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem("token");

    fetch("http://localhost:8080/appuntamenti/paziente", {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then(res => {
        if (!res.ok) throw new Error("Errore nel caricamento appuntamenti");
        return res.json();
      })
      .then(async data => {
        if (Array.isArray(data)) {
          const enriched = await Promise.all(
            data.map(async app => {
              const idMedicoPulito = Number(app.idMedico);

              if (!idMedicoPulito || isNaN(idMedicoPulito)) {
                return { ...app, medico: null };
              }

              try {
                const resMedico = await fetch(
                  `http://localhost:8080/medico/public/${idMedicoPulito}`,
                  {
                    headers: { Authorization: `Bearer ${token}` },
                  }
                );

                const medico = resMedico.ok ? await resMedico.json() : null;
                return { ...app, medico };

              } catch (err) {
                console.error("Errore fetch medico:", err);
                return { ...app, medico: null };
              }
            })
          );

          setAppuntamenti(enriched);
        } else {
          setAppuntamenti([]);
        }
      })
      .catch(err => console.error("Errore caricamento appuntamenti:", err));
  }, []);

  return (
    <div className="appuntamenti-page">

      <div className="appuntamenti-container">

        <h1 className="page-title">I tuoi appuntamenti</h1>

        <div className="appointments-grid">
          {Array.isArray(appuntamenti) && appuntamenti.length > 0 ? (
            appuntamenti.map(app => (
              <div key={app.id} className="appointment-card">
                <div className="appointment-header">
                  <h3>{app.dataAppuntamento} – {app.oraAppuntamento}</h3>
                  <span className={`status ${app.stato?.toLowerCase() || "sconosciuto"}`}>
                    {app.stato || "Sconosciuto"}
                  </span>
                </div>

                <div className="appointment-body">
                  <p>
                    <strong>Medico:</strong>{" "}
                    {app.medico
                      ? app.medico.nomeCompleto
                      : app.idMedico
                      ? `Medico #${app.idMedico}`
                      : "Non disponibile"}
                  </p>
                  <p><strong>Tipo visita:</strong> {app.tipoVisita || "Non specificato"}</p>
                </div>

                <div className="appointment-actions">
                  <Link to={`/paziente/appuntamenti/${app.id}`} className="btn-primary">
                    Dettagli
                  </Link>
                  <Link to={`/paziente/appuntamenti/${app.id}/modifica`} className="btn-secondary">
                    Modifica
                  </Link>
                  <Link to={`/paziente/appuntamenti/${app.id}/annulla`} className="btn-danger">
                    Annulla
                  </Link>
                </div>
              </div>
            ))
          ) : (
            <p>Nessun appuntamento trovato.</p>
          )}
        </div>

      </div>

      {/* 🔙 Pulsante torna alla dashboard */}
      <div className="back-button-container">
        <button
          onClick={() => navigate("/dashboard-paziente")

      }
          className="back-button"
        >
          Torna indietro
        </button>
      </div>

    </div>
  );
}
