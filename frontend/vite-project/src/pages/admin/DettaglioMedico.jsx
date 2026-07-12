import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import SidebarAdmin from "../../components/SidebarAdmin";
import TopbarAdmin from "../../components/TopbarAdmin";
import "../../styles/admin/DettaglioMedico.css";

export default function DettaglioMedico() {
  const { id } = useParams();
  const navigate = useNavigate();
  const token = localStorage.getItem("token");

  const [medico, setMedico] = useState(null);
  const [errorMsg, setErrorMsg] = useState("");

  useEffect(() => {
    const fetchMedico = async () => {
      try {
        const res = await fetch(`http://localhost:8080/admin/medici/${id}`, {
          headers: { Authorization: `Bearer ${token}` },
        });

        if (!res.ok) {
          setErrorMsg("Errore nel caricamento del medico");
          return;
        }

        const data = await res.json();
        setMedico(data);
      } catch (err) {
        console.error("Errore:", err);
        setErrorMsg("Errore di connessione al server");
      }
    };

    fetchMedico();
  }, [id, token]);

  if (errorMsg) {
    return (
      <div className="layout-admin">
        <SidebarAdmin />
        <div className="dashboard-admin-container">
          <TopbarAdmin />
          <p className="error-message">{errorMsg}</p>
          <button className="btn-back" onClick={() => navigate("/admin/medici")}>
            Torna alla lista
          </button>
        </div>
      </div>
    );
  }

  if (!medico) return <p>Caricamento...</p>;

  return (
    <div className="layout-admin">
      <SidebarAdmin />
      <div className="dashboard-admin-container">
        <TopbarAdmin />

        <div className="dettaglio-medico-content">
          <h1 className="title">Dettaglio Medico</h1>

          <div className="info-box">
            <p><strong>ID:</strong> {medico.id}</p>
            <p><strong>Nome Completo:</strong> {medico.nomeCompleto}</p>
            <p><strong>Email:</strong> {medico.email}</p>
            <p><strong>Specializzazione:</strong> {medico.specializzazione}</p>
          </div>

          <div className="button-group">
            <button
              className="btn-edit"
              onClick={() => navigate(`/admin/medici/${id}/modifica`)}
            >
              Modifica
            </button>

            <button
              className="btn-delete"
              onClick={() => navigate(`/admin/medici/${id}/elimina`)}
            >
              Elimina
            </button>

            <button
              className="btn-back"
              onClick={() => navigate("/admin/medici")}
            >
              Torna alla lista
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
