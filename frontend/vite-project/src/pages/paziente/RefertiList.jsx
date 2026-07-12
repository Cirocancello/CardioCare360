import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../../api/api";

import "../../styles/paziente/referti.css";

export default function RefertiList() {
  const [referti, setReferti] = useState([]);
  const navigate = useNavigate();
  const pazienteId = localStorage.getItem("idPaziente");

  useEffect(() => {
    const fetchReferti = async () => {
      try {
        // 🔹 Recupero referti del paziente
        const res = await api.get(`/referti/paziente/${pazienteId}`);
        setReferti(res.data);
      } catch (err) {
        console.error("Errore nel recupero dei referti:", err);
      }
    };

    fetchReferti();
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
    navigate(`/paziente/referti/${id}`);
  };

  return (
    <div className="referti-container">
      <h2 className="referti-title">I tuoi referti</h2>

      {referti.length === 0 ? (
        <p>Nessun referto disponibile.</p>
      ) : (
        <table className="referti-table">
          <thead>
            <tr>
              <th>Tipo Esame</th>
              <th>Data Referto</th>
              <th>Medico</th>
              <th>Azioni</th>
            </tr>
          </thead>
          <tbody>
            {referti.map((referto) => (
              <tr key={referto.id}>
                <td>{referto.tipoEsame}</td>
                <td>{referto.dataReferto}</td>
                <td>
                  {referto.nomeMedico} {referto.cognomeMedico}
                </td>
                <td>
                  <button
                    className="btn-primary"
                    onClick={() => goToDettaglio(referto.id)}
                  >
                    Dettagli
                  </button>
                  <button
                    className="btn-success"
                    onClick={() => downloadPdf(referto.id)}
                  >
                    Scarica PDF
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
