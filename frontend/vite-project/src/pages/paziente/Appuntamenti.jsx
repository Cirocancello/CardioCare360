import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import "../../styles/paziente/appuntamenti.css";

export default function Appuntamenti() {
  const [appuntamenti, setAppuntamenti] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem("token");

    fetch("http://localhost:8080/appuntamenti/paziente", {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then(res => res.json())
      .then(setAppuntamenti)
      .catch(console.error);
  }, []);

  return (
    <div className="appuntamenti-container">
     <button
        className="btn-secondary back-button"
        onClick={() => navigate("/dashboard-paziente")}
      >
        Torna alla Dashboard
      </button>



      <h1 className="page-title">I tuoi appuntamenti</h1>

      <div className="appointments-grid">
        {appuntamenti.map(app => (
          <div key={app.id} className="appointment-card">
            <div className="appointment-header">
              <h3>{app.dataAppuntamento} – {app.oraAppuntamento}</h3>
              <span className={`status ${app.stato?.toLowerCase()}`}>
                {app.stato}
              </span>
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
        ))}
      </div>
    </div>
  );
}
