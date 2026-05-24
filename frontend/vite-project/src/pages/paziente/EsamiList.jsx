import React, { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

import "../../styles/paziente/esami.css";

const EsamiList = () => {
  const [esami, setEsami] = useState([]);
  const navigate = useNavigate();

  const token = localStorage.getItem("token");
  const pazienteId = localStorage.getItem("idPaziente");

  useEffect(() => {
    const fetchEsami = async () => {
      try {
        const response = await axios.get(
          `http://localhost:8080/esami/paziente/${pazienteId}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        setEsami(response.data);
      } catch (error) {
        console.error("Errore nel recupero degli esami:", error);
      }
    };

    fetchEsami();
  }, [pazienteId, token]);

  const goToDettaglio = (id) => {
    navigate(`/paziente/esami/${id}`);
  };

  return (
    <div className="esami-container">
      <h2 className="esami-title">I tuoi esami</h2>

      {esami.length === 0 ? (
        <p>Nessun esame trovato.</p>
      ) : (
        <table className="esami-table">
          <thead>
            <tr>
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
                <td>{esame.tipoEsame}</td>
                <td>{esame.dataEsame}</td>
                <td>{esame.oraEsame}</td>
                <td>
                  <span
                    className={`badge ${
                      esame.stato === "REFERTATO"
                        ? "bg-success"
                        : esame.stato === "IN_ATTESA"
                        ? "bg-warning"
                        : "bg-secondary"
                    }`}
                  >
                    {esame.stato}
                  </span>
                </td>
                <td>
                  <button
                    className="btn-primary"
                    onClick={() => goToDettaglio(esame.id)}
                  >
                    Dettagli
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default EsamiList;
