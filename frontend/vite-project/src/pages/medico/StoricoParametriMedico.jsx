import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";
import SidebarMedico from "../../components/SidebarMedico";
import TopbarMedico from "../../components/TopbarMedico";
import "../../styles/medico/storicoParametriMedico.css";

const StoricoParametriMedico = () => {
  const { idPaziente } = useParams();
  const [storico, setStorico] = useState([]);
  const [loading, setLoading] = useState(true);

  const token = localStorage.getItem("token");
  const idMedico = localStorage.getItem("idMedico");


  useEffect(() => {
    const fetchStorico = async () => {
      try {
        const response = await axios.get(
          `http://localhost:8080/api/medici/${idMedico}/parametri/storico/${idPaziente}`,
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        );
        setStorico(response.data);
      } catch (error) {
        console.error("Errore nel recupero dello storico:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchStorico();
  }, [idPaziente, idMedico, token]);

  if (loading) {
    return <div className="lista-visite-container">Caricamento...</div>;
  }

  return (
    <div className="layout-medico">
      <SidebarMedico />
      <div className="lista-visite-container">
        <TopbarMedico />
        <h2 className="lista-parametri-title">Storico Parametri Paziente</h2>

        {storico.length === 0 ? (
          <div className="no-parametri">Nessun parametro registrato.</div>
        ) : (
          <table className="tabella-parametri">
            <thead>
              <tr>
                <th>Data rilevazione</th>
                <th>Parametro</th>
                <th>Valore</th>
                <th>Unità</th>
              </tr>
            </thead>
            <tbody>
              {storico.map((p) => (
                <tr key={p.id}>
                  <td>{p.dataRilevazione}</td>
                  <td>{p.nome}</td>
                  <td>{p.valore}</td>
                  <td>{p.unitaMisura}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
};

export default StoricoParametriMedico;
