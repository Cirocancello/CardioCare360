import React, { useEffect, useState } from "react";
import axios from "axios";
import { toast } from "react-toastify";
import "../../styles/paziente/ListaTerapie.css";

const ListaTerapie = () => {
  const [terapie, setTerapie] = useState([]);

  useEffect(() => {
    const fetchTerapie = async () => {
      try {
        const token = localStorage.getItem("token");
        const idPaziente = localStorage.getItem("idPaziente");

        const response = await axios.get(
          `http://localhost:8080/api/terapie/paziente/${idPaziente}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );

        setTerapie(response.data);

      } catch (err) {
        toast.error("Errore nel caricamento delle terapie");
        console.error(err);
      }
    };

    fetchTerapie();
  }, []);

  return (
    <div className="lista-terapie-page">
      <div className="lista-terapie-container">

        <h2 className="lista-terapie-title">Le tue Terapie</h2>

        {terapie.length === 0 ? (
          <p>Nessuna terapia disponibile.</p>
        ) : (
          terapie.map((t) => (
            <div key={t.id} className="terapia-card">

              <h5>{t.tipoTerapia}</h5>

              <p className="terapia-info">
                <span>Medico:</span> {t.medicoNome} {t.medicoCognome}
              </p>

              <p className="terapia-info">
                <span>Data inizio:</span> {t.dataInizio}
              </p>

              <p className="terapia-info">
                <span>Data fine:</span> {t.dataFine}
              </p>

              <p className="terapia-info">
                <span>Dosaggio:</span> {t.dosaggio}
              </p>

              {t.farmaci && t.farmaci.length > 0 && (
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
                {t.stato}
              </span>

            </div>
          ))
        )}
      </div>

       {/* 🔙 Pulsante torna indietro */}
      <div className="back-button-container">
        <button onClick={() => window.history.back()} className="back-button">
          Torna indietro
        </button>
      </div>
    </div>
  );
};

export default ListaTerapie;
