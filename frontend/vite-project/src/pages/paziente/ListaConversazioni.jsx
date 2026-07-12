import React, { useEffect, useState } from "react";
import axios from "axios";
import { toast } from "react-toastify";
import "../../styles/paziente/listaTerapie.css";

const ListaTerapie = () => {
  const [terapie, setTerapie] = useState([]);
  const [loading, setLoading] = useState(true);
  const [errore, setErrore] = useState(null);

  useEffect(() => {
    const fetchTerapie = async () => {
      const token = localStorage.getItem("token");
      const idPaziente = localStorage.getItem("idPaziente");

      if (!token || !idPaziente) {
        setErrore("Sessione scaduta. Effettua nuovamente il login.");
        setLoading(false);
        return;
      }

      try {
        const response = await axios.get(
          `http://localhost:8080/api/terapie/paziente/${idPaziente}`,
          { headers: { Authorization: `Bearer ${token}` } }
        );

        const data = response.data;
        setTerapie(Array.isArray(data) ? data : []);
      } catch (err) {
        console.error("Errore nel caricamento delle terapie:", err);
        toast.error("Errore nel caricamento delle terapie");
        setErrore("Errore nel caricamento delle terapie.");
      } finally {
        setLoading(false);
      }
    };

    fetchTerapie();
  }, []);

  if (loading) return <p>Caricamento terapie...</p>;
  if (errore) return <p className="error-message">{errore}</p>;

  return (
    <div className="lista-terapie-page">
      <div className="lista-terapie-container">

        <h2 className="lista-terapie-title">Le tue Terapie</h2>

        {terapie.length === 0 ? (
          <p>Nessuna terapia disponibile.</p>
        ) : (
          terapie.map((t) => (
            <div key={t.id} className="terapia-card">

              <h5>{t.tipoTerapia || "Terapia"}</h5>

              <p className="terapia-info">
                <span>Medico:</span> {t.medicoNome || ""} {t.medicoCognome || ""}
              </p>

              <p className="terapia-info">
                <span>Data inizio:</span> {t.dataInizio || "—"}
              </p>

              <p className="terapia-info">
                <span>Data fine:</span> {t.dataFine || "—"}
              </p>

              <p className="terapia-info">
                <span>Dosaggio:</span> {t.dosaggio || "—"}
              </p>

              {Array.isArray(t.farmaci) && t.farmaci.length > 0 && (
                <p className="terapia-info">
                  <span>Farmaci:</span> {t.farmaci.join(", ")}
                </p>
              )}

              <span
                className={
                  "terapia-stato " +
                  (t.stato === "ATTIVA" ? "attiva" : "conclusa")
                }
              >
                {t.stato || "—"}
              </span>

            </div>
          ))
        )}
      </div>
    </div>
  );
};

export default ListaTerapie;
