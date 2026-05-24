import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";

import "../../styles/paziente/esami.css";

export default function DettaglioEsame() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [esame, setEsame] = useState(null);
  const [referto, setReferto] = useState(null);

  const token = localStorage.getItem("token");

  useEffect(() => {
    const fetchData = async () => {
      try {
        // 1️⃣ Recupero dati esame
        const resEsame = await axios.get(`http://localhost:8080/esami/${id}`, {
          headers: { Authorization: `Bearer ${token}` },
        });
        setEsame(resEsame.data);

        // 2️⃣ Se l’esame è refertato → recupero referto
        if (resEsame.data.stato === "REFERTATO") {
          const resReferto = await axios.get(
            `http://localhost:8080/esami/${id}/referto`,
            {
              headers: { Authorization: `Bearer ${token}` },
            }
          );
          setReferto(resReferto.data);
        }
      } catch (error) {
        console.error("Errore nel recupero dei dati:", error);
      }
    };

    fetchData();
  }, [id, token]);

  if (!esame) return <p>Caricamento...</p>;

  return (
    <div className="esami-container">
      <h2 className="esami-title">Dettaglio Esame</h2>

      {/* === DATI ESAME === */}
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

        <p>
          <strong>Note Paziente:</strong>{" "}
          {esame.notePaziente || "Nessuna nota"}
        </p>
        <p>
          <strong>Note Medico:</strong>{" "}
          {esame.noteMedico || "Nessuna nota"}
        </p>
      </div>

      {/* === DATI REFERTO (solo se presente) === */}
      {referto && (
        <div className="info-card-esame">
          <h3>Referto</h3>

          <p><strong>Titolo:</strong> {referto.titolo || "—"}</p>
          <p><strong>Descrizione:</strong> {referto.descrizione || "—"}</p>
          <p><strong>Diagnosi:</strong> {referto.diagnosi1 || "—"}</p>
          <p><strong>Note Medico:</strong> {referto.noteMedico || "—"}</p>
          <p><strong>Data Referto:</strong> {referto.dataReferto || "—"}</p>

          <p>
            <strong>Medico:</strong>{" "}
            {referto.nomeMedico} {referto.cognomeMedico}
          </p>

          {referto.filePath && (
            <>
              <a
                href={`http://localhost:8080/files/${referto.filePath}`}
                target="_blank"
                rel="noopener noreferrer"
                className="btn-primary"
              >
                Scarica PDF Referto
              </a>

              {/* Anteprima PDF */}
              <div className="pdf-preview">
                <iframe
                  src={`http://localhost:8080/files/${referto.filePath}`}
                  title="Anteprima Referto"
                  width="100%"
                  height="600px"
                  style={{
                    border: "1px solid #ddd",
                    borderRadius: "8px",
                    marginTop: "15px",
                  }}
                ></iframe>
              </div>
            </>
          )}
        </div>
      )}

      {/* === AZIONI === */}
      <div className="azioni-esame">
        <button
          className="btn-primary"
          onClick={() => navigate("/paziente/esami")}
        >
          Torna alla lista
        </button>
      </div>
    </div>
  );
}
