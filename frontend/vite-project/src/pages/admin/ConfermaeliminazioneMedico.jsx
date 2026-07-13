import React from "react";
import { useParams, useNavigate } from "react-router-dom";
import SidebarAdmin from "../../components/SidebarAdmin";
import TopbarAdmin from "../../components/TopbarAdmin";
import { toast } from "react-toastify";
import "../../styles/admin/ConfermaElimnazione.css";

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
        toast.success("Medico eliminato con successo!");

        setTimeout(() => {
          navigate("/admin/medici");
        }, 600);

      } else {
        toast.error("Errore durante l'eliminazione del medico");
      }
    } catch (err) {
      console.error("Errore:", err);
      toast.error("Errore di connessione al server");
    }
  };

  return (
    <div className="layout-admin">
     
      <div className="dashboard-admin-container">
       

        <div className="confirm-delete-container">
          <div className="confirm-box">
            <h1 className="title">Conferma Eliminazione</h1>

            <p className="warning-text">
              Sei sicuro di voler eliminare il medico con ID <strong>{id}</strong>?<br />
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
    </div>
  );
}
