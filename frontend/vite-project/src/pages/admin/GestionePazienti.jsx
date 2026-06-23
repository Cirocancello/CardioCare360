import React, { useEffect, useState } from "react";
import SidebarAdmin from "../../components/SidebarAdmin";
import TopbarAdmin from "../../components/TopbarAdmin";
import "../../styles/admin/GestionePazienti.css";

export default function GestionePazienti() {
  const [pazienti, setPazienti] = useState([]);
  const token = localStorage.getItem("token");

  useEffect(() => {
    const fetchPazienti = async () => {
      try {
        const res = await fetch("http://localhost:8080/admin/pazienti", {
          headers: { Authorization: `Bearer ${token}` },
        });
        const data = await res.json();
        setPazienti(data);
      } catch (err) {
        console.error("Errore caricamento pazienti", err);
      }
    };

    fetchPazienti();
  }, [token]);

  return (
    <div className="layout-admin">
      <SidebarAdmin />

      <div className="dashboard-admin-container">
        <TopbarAdmin />

        <div className="gestione-pazienti-content">
          <h1 className="title">Gestione Pazienti</h1>

          <table className="pazienti-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Nome</th>
                <th>Email</th>
                <th>Telefono</th>
                <th>Azioni</th>
              </tr>
            </thead>

            <tbody>
              {pazienti.map((p) => (
                <tr key={p.id}>
                  <td>{p.id}</td>
                  <td>{p.nome}</td>
                  <td>{p.email}</td>
                  <td>{p.telefono}</td>
                  <td>
                    <button className="btn-edit">Modifica</button>
                    <button className="btn-delete">Elimina</button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>

        </div>
      </div>
    </div>
  );
}
