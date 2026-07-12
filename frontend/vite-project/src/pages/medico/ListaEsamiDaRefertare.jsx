import React, { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

import SidebarMedico from "../../components/SidebarMedico";
import TopbarMedico from "../../components/TopbarMedico";
import "../../styles/medico/listaEsamiDaRefertare.css";

const ListaEsamiDaRefertare = () => {
  const [esami, setEsami] = useState([]);
  const [loading, setLoading] = useState(true);
  const [errore, setErrore] = useState(null);

  const navigate = useNavigate();

  useEffect(() => {
    const fetchEsami = async () => {
      const token = localStorage.getItem("token");

      // 🔒 Blindatura: token mancante → redirect
      if (!token) {
        setErrore("Token mancante. Effettua nuovamente il login.");
        setLoading(false);
        return;
      }

      try {
        const response = await axios.get(
          "http://localhost:8080/esami/medico/da-refertare",
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        );

        setEsami(response.data || []);
      } catch (err) {
        console.error("Errore nel caricamento esami:", err);
        setErrore("Errore nel caricamento degli esami da refertare.");
      } finally {
        setLoading(false);
      }
    };

    fetchEsami();
  }, []);

  const handleReferta = (idEsame) => {
    if (!idEsame) return;
    navigate(`/medico/esami/referta/${idEsame}`);
  };

  // ---------------------------------------------------------
  // RENDER
  // ---------------------------------------------------------

  if (loading) return <p className="loading-message">Caricamento...</p>;

  if (errore) return <p className="error-message">{errore}</p>;

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
                    <td>
                      {esame.nomePaziente} {esame.cognomePaziente}
                    </td>
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
