import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import api from "../../api/api";

import "../../styles/paziente/esami.css";

export default function DettaglioEsame() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [esame, setEsame] = useState(null);
  const [referto, setReferto] = useState(null);
  const [pdfUrl, setPdfUrl] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const token = localStorage.getItem("token");

        // 🔹 Recupero esame
        const resEsame = await api.get(`/esami/${id}`, {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });
        setEsame(resEsame.data);

        // 🔹 Se refertato → recupero referto
        if (resEsame.data.stato === "REFERTATO") {
          const resReferto = await api.get(`/referti/esame/${id}`, {
            headers: {
              Authorization: `Bearer ${token}`
            }
          });
          setReferto(resReferto.data);
        }
      } catch (error) {
        console.error("Errore nel recupero dei dati:", error);
       
      }
    };

    fetchData();
  }, [id, navigate]);

  // 🔹 Anteprima PDF
  const apriPdf = async () => {
    try {
      const token = localStorage.getItem("token");

      const res = await api.get(`/referti/preview/${id}`, {
        responseType: "arraybuffer",
        headers: {
          Authorization: `Bearer ${token}`
        }
      });

      const bytes = new Uint8Array(res.data);
      const blob = new Blob([bytes], { type: "application/pdf" });
      const fileURL = URL.createObjectURL(blob);
      setPdfUrl(fileURL);
    } catch (err) {
      console.error("Errore apertura PDF:", err);
    }
  };

  // 🔹 Download PDF
  const scaricaPdf = async () => {
    try {
      const token = localStorage.getItem("token");

      const res = await api.get(`/referti/download/${referto.id}`, {
        responseType: "arraybuffer",
        headers: {
          Authorization: `Bearer ${token}`
        }
      });

      const blob = new Blob([res.data], { type: "application/pdf" });
      const fileURL = URL.createObjectURL(blob);

      const link = document.createElement("a");
      link.href = fileURL;
      link.download = `referto_${referto.id}.pdf`;
      document.body.appendChild(link);
      link.click();
      link.remove();

      URL.revokeObjectURL(fileURL);
    } catch (err) {
      console.error("Errore download PDF:", err);
    }
  };

  if (!esame) return <p>Caricamento...</p>;

  return (
    <div className="esami-container">
      <h2 className="esami-title">Dettaglio Esame</h2>

      <div className="info-card-esame">
        <h3>Dati Esame</h3>
        <p><strong>Tipo Esame:</strong> {esame.tipoEsame}</p>
        <p><strong>Data:</strong> {esame.dataEsame}</p>
        <p><strong>Ora:</strong> {esame.oraEsame}</p>

        <p>
          <strong>Stato:</strong>{" "}
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
        </p>

        <p><strong>Note Paziente:</strong> {esame.notePaziente || "Nessuna nota"}</p>
        <p><strong>Note Medico:</strong> {esame.noteMedico || "Nessuna nota"}</p>
      </div>

      {referto && (
        <div className="info-card-esame">
          <h3>Referto</h3>
          <p><strong>Note Medico:</strong> {referto.noteMedico || "—"}</p>
          <p><strong>Medico:</strong> {referto.nomeMedico} {referto.cognomeMedico}</p>

          <div className="azioni-referto">
            <button className="btn-view-pdf" onClick={apriPdf}>
              Visualizza PDF
            </button>

            <button className="btn-download-pdf" onClick={scaricaPdf}>
              Scarica PDF
            </button>
          </div>

          {pdfUrl && (
            <iframe
              src={pdfUrl}
              width="100%"
              height="600px"
              style={{ border: "none", marginTop: "20px" }}
              title="Anteprima Referto"
            ></iframe>
          )}
        </div>
      )}

      <div className="azioni-esame">
        <button
          className="btn-primary"
          onClick={() => navigate("/dashboard-paziente")}
        >
          Torna indietro
        </button>
      </div>
    </div>
  );
}
