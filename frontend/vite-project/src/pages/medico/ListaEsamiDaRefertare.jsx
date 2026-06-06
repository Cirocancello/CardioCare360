import React, { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import SidebarMedico from "../../components/SidebarMedico";
import TopbarMedico from "../../components/TopbarMedico";
import "../../styles/medico/listaEsamiDaRefertare.css";

const ListaEsamiDaRefertare = () => {
  const [esami, setEsami] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchEsami = async () => {
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get(
          "http://localhost:8080/esami/medico/da-refertare",
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        );
        setEsami(response.data);
      } catch (err) {
        setError("Errore nel caricamento degli esami");
      } finally {
        setLoading(false);
      }
    };
    fetchEsami();
  }, []);

  const handleReferta = (idEsame) => {
    navigate(`/medico/esami/referta/${idEsame}`);
  };

  if (loading) return <p className="loading-message">Caricamento...</p>;
  if (error) return <p className="error-message">{error}</p>;

  return (
    <div className="layout-medico">
      <SidebarMedico />
      <div className="dashboard-medico-container">
        <TopbarMedico />

        <div className="lista-esami-container">
          <h2 className="lista-esami-title">Esami da Refertare</h2>

          {esami.length === 0 ? (
            <p className="empty-message">Nessun esame da refertare.</p>
          ) : (
            <table className="lista-esami-table">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Paziente</th>
                  <th>Tipo Esame</th>
                  <th>Data</th>
                  <th>Ora</th>
                  <th>Stato</th>
                  <th>Azioni</th>
                </tr>
              </thead>
              <tbody>
                {esami.map((esame) => (
                  <tr key={esame.id}>
                    <td>{esame.id}</td>
                    <td>{esame.nomePaziente} {esame.cognomePaziente}</td>
                    <td>{esame.tipoEsame}</td>
                    <td>{esame.dataEsame}</td>
                    <td>{esame.oraEsame}</td>
                    <td>
                      <span className="badge-eseguito">ESEGUITO</span>
                    </td>
                    <td>
                      <button
                        className="btn-referta"
                        onClick={() => handleReferta(esame.id)}
                      >
                        Referta
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      </div>
    </div>
  );
};

export default ListaEsamiDaRefertare;
