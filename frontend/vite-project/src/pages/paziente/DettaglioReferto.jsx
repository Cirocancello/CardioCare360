import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import api from "../../api/api";
import "../../styles/paziente/referti.css";

export default function DettaglioReferto() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [referto, setReferto] = useState(null);
  const [pdfUrl, setPdfUrl] = useState(null);

  useEffect(() => {
    const fetchReferto = async () => {
      try {
        const res = await api.get(`/referti/${id}`);
        setReferto(res.data);
      } catch (err) {
        console.error("Errore nel recupero del referto:", err);
        navigate("/login");
      }
    };
    fetchReferto();
  }, [id, navigate]);

  const apriPdf = async () => {
    try {
      const res = await api.get(`/referti/preview/${id}`, {
        responseType: "arraybuffer",
      });
      const blob = new Blob([res.data], { type: "application/pdf" });
      const url = URL.createObjectURL(blob);
      setPdfUrl(url);
    } catch (err) {
      console.error("Errore apertura PDF:", err);
    }
  };

  const scaricaPdf = async () => {
    try {
      const res = await api.get(`/referti/download/${id}`, {
        responseType: "arraybuffer",
      });
      const blob = new Blob([res.data], { type: "application/pdf" });
      const url = URL.createObjectURL(blob);
      const link = document.createElement("a");
      link.href = url;
      link.download = `referto_${id}.pdf`;
      link.click();
      URL.revokeObjectURL(url);
    } catch (err) {
      console.error("Errore download PDF:", err);
    }
  };

  if (!referto) return <p>Caricamento referto...</p>;

  return (
    <div className="referto-container">
      <h2 className="referto-title">Dettaglio Referto</h2>

      <div className="info-card-referto">
        <p><strong>Tipo Esame:</strong> {referto.tipoEsame}</p>
        <p><strong>Data Referto:</strong> {referto.dataReferto}</p>
        <p><strong>Medico:</strong> {referto.nomeMedico} {referto.cognomeMedico}</p>
        <p><strong>Note Medico:</strong> {referto.noteMedico || "—"}</p>
      </div>

      <div className="azioni-referto">
        <button className="btn-view-pdf" onClick={apriPdf}>Visualizza PDF</button>
        <button className="btn-download-pdf" onClick={scaricaPdf}>Scarica PDF</button>
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

      <div className="azioni-referto">
        <button className="btn-primary" onClick={() => navigate("/paziente/referti")}>
          Torna ai referti
        </button>
      </div>
    </div>
  );
}
