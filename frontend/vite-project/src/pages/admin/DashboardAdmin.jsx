import React, { useEffect, useState } from "react";
import SidebarAdmin from "../../components/SidebarAdmin";
import TopbarAdmin from "../../components/TopbarAdmin";
import "../../styles/admin/DashboardAdmin.css";
import { useNavigate } from "react-router-dom";

export default function DashboardAdmin() {
  const [medici, setMedici] = useState(0);
  const [pazienti, setPazienti] = useState(0);
  const [errore, setErrore] = useState(null);
  const [loading, setLoading] = useState(true);

  const token = localStorage.getItem("token");
  const navigate = useNavigate();

  useEffect(() => {
    if (!token) {
      navigate("/login");
      return;
    }

    const fetchMedici = async () => {
      try {
        const res = await fetch("http://localhost:8080/admin/medici", {
          headers: { Authorization: `Bearer ${token}` },
        });

        if (!res.ok) throw new Error("Errore caricamento medici");

        const data = await res.json();
        setMedici(Array.isArray(data) ? data.length : 0);
      } catch (err) {
        console.error("Errore caricamento medici", err);
        setErrore("Errore nel caricamento dei medici.");
      }
    };

    const fetchPazienti = async () => {
      try {
        const res = await fetch("http://localhost:8080/admin/pazienti", {
          headers: { Authorization: `Bearer ${token}` },
        });

        if (!res.ok) throw new Error("Errore caricamento pazienti");

        const data = await res.json();
        setPazienti(Array.isArray(data) ? data.length : 0);
      } catch (err) {
        console.error("Errore caricamento pazienti", err);
        setErrore("Errore nel caricamento dei pazienti.");
      }
    };

    Promise.all([fetchMedici(), fetchPazienti()]).finally(() =>
      setLoading(false)
    );
  }, [token, navigate]);

  // ---------------------------------------------------------
  // RENDER
  // ---------------------------------------------------------

  if (loading) return <p className="loading-message">Caricamento dati...</p>;
  if (errore) return <p className="error-message">{errore}</p>;

  return (
    <div className="layout-admin">
      <SidebarAdmin />

      <div className="dashboard-admin-container">
        <TopbarAdmin />

        <div className="dashboard-content">
          <h1 className="title">Dashboard Amministratore</h1>

          {/* CARD STATISTICHE */}
          <div className="cards-container">
            <div className="card">
              <h2>Medici Registrati</h2>
              <p>{medici}</p>
            </div>

            <div className="card">
              <h2>Pazienti Registrati</h2>
              <p>{pazienti}</p>
            </div>
          </div>

          {/* CARD AZIONI */}
          <h2 className="subtitle">Azioni Rapide</h2>

          <div className="cards-container actions">
            <div
              className="card action-card"
              onClick={() => navigate("/admin/medici")}
            >
              <h3>Gestione Medici</h3>
            </div>

            <div
              className="card action-card"
              onClick={() => navigate("/admin/pazienti")}
            >
              <h3>Gestione Pazienti</h3>
            </div>

            <div
              className="card action-card logout-card"
              onClick={() => {
                localStorage.removeItem("token");
                localStorage.removeItem("ruolo");
                navigate("/login");
              }}
            >
              <h3>Logout</h3>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
