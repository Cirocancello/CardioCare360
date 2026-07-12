import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../../api/api";

import "../../styles/paziente/esami.css";

const EsamiList = () => {
  const [esami, setEsami] = useState([]);
  const [referti, setReferti] = useState({}); // referti per ogni esame

  const navigate = useNavigate();

  const pazienteId = localStorage.getItem("idPaziente");

  useEffect(() => {
    const fetchEsami = async () => {
      try {
        // 🔹 Recupero esami del paziente
        const response = await api.get(`/esami/paziente/${pazienteId}`);
        setEsami(response.data);

        // 🔹 Per ogni esame, prova a recuperare il referto
        response.data.forEach(async (esame) => {
          try {
            const refertoRes = await api.get(`/referti/esame/${esame.id}`);
            setReferti((prev) => ({
              ...prev,
              [esame.id]: refertoRes.data,
            }));
          } catch (err) {
            // Nessun referto → ignora
          }
        });
      } catch (error) {
        console.error("Errore nel recupero degli esami:", error);
      }
    };

    fetchEsami();
  }, [pazienteId]);

  // 🔹 Download PDF con token
  const downloadPdf = async (idReferto) => {
    try {
      const res = await api.get(`/referti/download/${idReferto}`, {
        responseType: "arraybuffer",
      });

      const blob = new Blob([res.data], { type: "application/pdf" });
      const url = URL.createObjectURL(blob);

      const link = document.createElement("a");
      link.href = url;
      link.download = `referto_${idReferto}.pdf`;
      link.click();

      URL.revokeObjectURL(url);
    } catch (err) {
      console.error("Errore download PDF:", err);
    }
  };

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
                        : esame.stato === "IN_ATTESA"
                        ? "bg-warning"
                        : "bg-secondary"
                    }`}
                  >
                    {esame.stato}
                  </span>
                </td>

                {/* 🔹 Se il referto esiste → pulsante download */}
                <td>
                  {referti[esame.id] ? (
                    <button
                      className="btn-success"
                      onClick={() => downloadPdf(referti[esame.id].id)}
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

      {/* 🔙 Pulsante torna indietro */}
      <button
        onClick={() => navigate(-1)}
        style={{
          marginTop: "25px",
          padding: "10px 16px",
          borderRadius: "8px",
          backgroundColor: "#e0e0e0",
          border: "none",
          cursor: "pointer",
          fontSize: "15px"
        }}
      >
        Torna indietro
      </button>
    </div>
  );
};

export default EsamiList;
