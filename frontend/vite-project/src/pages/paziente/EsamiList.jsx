import React, { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

import "../../styles/paziente/esami.css";

const EsamiList = () => {
  const [esami, setEsami] = useState([]);
  const [referti, setReferti] = useState({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const navigate = useNavigate();

  const token = localStorage.getItem("token");
  const pazienteId = localStorage.getItem("idPaziente");

  useEffect(() => {
    const fetchEsami = async () => {
      // 🔒 Controlli iniziali
      if (!token) {
        setError("Token non presente. Effettua di nuovo il login.");
        setLoading(false);
        return;
      }

      if (!pazienteId) {
        setError("ID paziente non trovato.");
        setLoading(false);
        return;
      }

      try {
        const response = await axios.get(
          `http://localhost:8080/esami/paziente/${pazienteId}`,
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        );

        const listaEsami = response.data || [];
        setEsami(listaEsami);

        // 🔥 Recupero referti in modo sicuro
        listaEsami.forEach(async (esame) => {
          try {
            const refertoRes = await axios.get(
              `http://localhost:8080/referti/esame/${esame.id}`,
              {
                headers: { Authorization: `Bearer ${token}` },
              }
            );

            if (refertoRes.data) {
              setReferti((prev) => ({
                ...prev,
                [esame.id]: refertoRes.data,
              }));
            }
          } catch {
            // Nessun referto → ignora
          }
        });
      } catch (err) {
        console.error("Errore nel recupero degli esami:", err);
        setError("Errore nel recupero degli esami.");
      } finally {
        setLoading(false);
      }
    };

    fetchEsami();
  }, [pazienteId, token]);

  const goToDettaglio = (id) => {
    if (!id) return;
    navigate(`/paziente/esami/${id}`);
  };

  // ---------------------------------------------------------
  // RENDER
  // ---------------------------------------------------------

  if (loading) {
    return <p>Caricamento in corso...</p>;
  }

  if (error) {
    return <p className="error-message">{error}</p>;
  }

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
              <th>Referto</th>
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
                        : esame.stato === "ESEGUITO"
                        ? "bg-warning"
                        : "bg-secondary"
                    }`}
                  >
                    {esame.stato}
                  </span>
                </td>

                {/* REFERTI */}
                <td>
                  {referti[esame.id] ? (
                    <button
                      className="btn-success"
                      onClick={() =>
                        window.open(
                          `http://localhost:8080/referti/download/${referti[esame.id].id}`
                        )
                      }
                    >
                      Scarica PDF
                    </button>
                  ) : (
                    <span className="badge bg-secondary">Non disponibile</span>
                  )}
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
