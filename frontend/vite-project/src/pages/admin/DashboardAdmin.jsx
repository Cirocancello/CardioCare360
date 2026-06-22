import React, { useEffect, useState } from "react";
import SidebarAdmin from "../../components/SidebarAdmin";
import TopbarAdmin from "../../components/TopbarAdmin";
import "../../styles/admin/DashboardAdmin.css";

export default function DashboardAdmin() {
  const [medici, setMedici] = useState(0);
  const [pazienti, setPazienti] = useState(0);

  const token = localStorage.getItem("token");

  useEffect(() => {
    const fetchMedici = async () => {
      try {
        const res = await fetch("http://localhost:8080/admin/medici", {
          headers: { Authorization: `Bearer ${token}` },
        });
        const data = await res.json();
        setMedici(data.length);
      } catch (err) {
        console.error("Errore caricamento medici", err);
      }
    };

    const fetchPazienti = async () => {
      try {
        const res = await fetch("http://localhost:8080/admin/pazienti", {
          headers: { Authorization: `Bearer ${token}` },
        });
        const data = await res.json();
        setPazienti(data.length);
      } catch (err) {
        console.error("Errore caricamento pazienti", err);
      }
    };

    fetchMedici();
    fetchPazienti();
  }, [token]);

  return (
    <div className="layout-admin">
      <SidebarAdmin />

      <div className="dashboard-admin-container">
        <TopbarAdmin />

        <div className="dashboard-content">
          <h1 className="title">Dashboard Amministratore</h1>

          <div className="cards-container">
            <div className="card">
              <h2>Medici</h2>
              <p>{medici}</p>
            </div>

            <div className="card">
              <h2>Pazienti</h2>
              <p>{pazienti}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
