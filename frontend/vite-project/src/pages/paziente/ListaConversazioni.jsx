import React, { useEffect, useState } from "react";
import axios from "axios";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";
import "../../styles/paziente/ListaConversazioni.css";

const ListaConversazioni = () => {
  const [conversazioni, setConversazioni] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchConversazioni = async () => {
      try {
        const token = localStorage.getItem("token");
        const idPaziente = localStorage.getItem("idPaziente");

        const response = await axios.get(
          `http://localhost:8080/api/conversazioni/paziente/${idPaziente}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );

        setConversazioni(response.data);

      } catch (err) {
        toast.error("Errore nel caricamento delle conversazioni");
        console.error(err);
      }
    };

    fetchConversazioni();
  }, []);

  const apriChat = (id) => {
    navigate(`/paziente/conversazioni/${id}`);
  };

  return (
    <div className="conv-page">
      <div className="conv-container">

        <h2 className="conv-title">Le tue Conversazioni</h2>

        {conversazioni.length === 0 ? (
          <p className="conv-empty">Nessuna conversazione disponibile.</p>
        ) : (
          conversazioni.map((c) => (
            <div
              key={c.id}
              className="conv-card"
              onClick={() => apriChat(c.id)}
            >
              <h5 className="conv-doctor">
                Dr. {c.medicoNome} {c.medicoCognome}
              </h5>

              <p className="conv-lastmsg">
                {c.ultimoMessaggio ? c.ultimoMessaggio : "Nessun messaggio ancora"}
              </p>

              <small className="conv-date">
                Ultimo aggiornamento:{" "}
                {new Date(c.ultimoAggiornamento).toLocaleString()}
              </small>

              {c.nonLetti > 0 && (
                <span className="conv-badge">{c.nonLetti} non letti</span>
              )}
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

export default ListaConversazioni;
