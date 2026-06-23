import React from "react";
import { useParams, useNavigate } from "react-router-dom";
import SidebarAdmin from "../../components/SidebarAdmin";
import TopbarAdmin from "../../components/TopbarAdmin";
import "../../styles/admin/ConfermaEliminazione.css";

export default function ConfermaEliminazioneMedico() {
  const { id } = useParams();
  const navigate = useNavigate();
  const token = localStorage.getItem("token");

  const handleDelete = async () => {
    try {
      const res = await fetch(`http://localhost:8080/admin/medici/${id}`, {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (res.ok) {
        alert("Medico eliminato con successo!");
        navigate("/admin/medici");
      } else {
        alert("Errore durante l'eliminazione del medico");
      }
    } catch (err) {
      console.error("Errore:", err);
      alert("Errore di connessione al server");
    }
  };

  return (
    <div className="layout-admin">
      <SidebarAdmin />
      <div className="dashboard-admin-container">
        <TopbarAdmin />

        <div className="confirm-delete-container">
          <h1 className="title">Conferma Eliminazione</h1>

          <p className="warning-text">
            Sei sicuro di voler eliminare il medico con ID <strong>{id}</strong>?
            <br />
            Questa azione è irreversibile.
          </p>

          <div className="button-group">
            <button className="btn-delete" onClick={handleDelete}>
              Elimina
            </button>

            <button
              className="btn-cancel"
              onClick={() => navigate("/admin/medici")}
            >
              Annulla
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
