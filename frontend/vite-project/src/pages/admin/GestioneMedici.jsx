import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import SidebarAdmin from "../../components/SidebarAdmin";
import TopbarAdmin from "../../components/TopbarAdmin";
import "../../styles/admin/GestioneMedici.css";

export default function GestioneMedici() {
  const [medici, setMedici] = useState([]);
  const [loading, setLoading] = useState(true);
  const [errorMsg, setErrorMsg] = useState("");
  const token = localStorage.getItem("token");
  const navigate = useNavigate();

  useEffect(() => {
    const fetchMedici = async () => {
      try {
        const res = await fetch("http://localhost:8080/admin/medici", {
          headers: { Authorization: `Bearer ${token}` },
        });

        if (!res.ok) {
          setErrorMsg("Errore nel caricamento dei medici");
          setLoading(false);
          return;
        }

        const data = await res.json();
        setMedici(data);
      } catch (err) {
        console.error("Errore caricamento medici", err);
        setErrorMsg("Errore di connessione al server");
      } finally {
        setLoading(false);
      }
    };

    fetchMedici();
  }, [token]);

  if (loading) return <p>Caricamento...</p>;

  if (errorMsg) {
    return (
      <div className="layout-admin">
        <SidebarAdmin />
        <div className="dashboard-admin-container">
          <TopbarAdmin />
          <p className="error-message">{errorMsg}</p>
        </div>
      </div>
    );
  }

  return (
    <div className="layout-admin">
      <SidebarAdmin />
      <div className="dashboard-admin-container">
        <TopbarAdmin />

        <div className="gestione-medici-content">
          <h1 className="title">Gestione Medici</h1>

          <div className="gestione-medici-header">
            <button
              className="btn-add"
              onClick={() => navigate("/admin/medici/crea")}
            >
              + Aggiungi Medico
            </button>
          </div>

          <table className="medici-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Nome Completo</th>
                <th>Email</th>
                <th>Specializzazione</th>
                <th>Azioni</th>
              </tr>
            </thead>
            <tbody>
              {medici.map((medico) => (
                <tr key={medico.id}>
                  <td>{medico.id}</td>
                  <td>{medico.nomeCompleto}</td>
                  <td>{medico.email}</td>
                  <td>{medico.specializzazione}</td>
                  <td>
                    <button
                      className="btn-details"
                      onClick={() => navigate(`/admin/medici/${medico.id}`)}
                    >
                      Dettagli
                    </button>

                    <button
                      className="btn-edit"
                      onClick={() => navigate(`/admin/medici/${medico.id}/modifica`)}
                    >
                      Modifica
                    </button>

                    <button
                      className="btn-delete"
                      onClick={() => navigate(`/admin/medici/${medico.id}/elimina`)}
                    >
                      Elimina
                    </button>
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
