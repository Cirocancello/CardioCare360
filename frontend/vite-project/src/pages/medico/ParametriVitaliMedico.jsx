import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import SidebarMedico from "../../components/SidebarMedico";
import TopbarMedico from "../../components/TopbarMedico";
import "../../styles/medico/parametriMedico.css";

const ParametriVitaliMedico = () => {
  const [parametri, setParametri] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  const medicoId = localStorage.getItem("idMedico");
  const token = localStorage.getItem("token");

  useEffect(() => {
    const fetchParametri = async () => {
      try {
        const response = await axios.get(
          `http://localhost:8080/api/medici/${medicoId}/parametri/recenti`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        setParametri(response.data);
      } catch (error) {
        console.error("Errore nel recupero dei parametri:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchParametri();
  }, [medicoId, token]);

  const getBadgeColor = (stato) => {
    switch (stato) {
      case "OK":
        return "badge-ok";
      case "ALERT":
        return "badge-alert";
      case "NODATA":
        return "badge-nodata";
      default:
        return "badge-nodata";
    }
  };

  if (loading) {
    return <div className="lista-visite-container">Caricamento...</div>;
  }

  return (
    <div className="layout-medico">
      <SidebarMedico />
      <div className="lista-visite-container">
        <TopbarMedico />
        <div className="lista-parametri-header">
          <h2 className="lista-parametri-title">Monitoraggio Parametri Vitali</h2>
        </div>

        {parametri.length === 0 ? (
          <div className="no-parametri">
            Nessun paziente assegnato o nessun parametro disponibile.
          </div>
        ) : (
          <table className="tabella-parametri">
            <thead>
              <tr>
                <th>Paziente</th>
                <th>Ultima rilevazione</th>
                <th>Temperatura</th>
                <th>Stato</th>
                <th>Storico</th>
              </tr>
            </thead>
            <tbody>
              {parametri.map((p) => (
                <tr key={p.idPaziente}>
                  <td>{p.nome} {p.cognome}</td>
                  <td>{p.dataUltimaRilevazione || "—"}</td>
                  <td>{p.temperatura || "—"}</td>
                  <td>
                    <span className={getBadgeColor(p.stato)}>
                      {p.stato}
                    </span>
                  </td>
                  <td>
                    <button
                      className="btn-storico"
                      onClick={() =>
                        navigate(`/medico/parametri/storico/${p.idPaziente}`)
                      }
                    >
                      Vedi storico
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
};

export default ParametriVitaliMedico;
