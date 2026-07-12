import React, { useEffect, useState, useRef } from "react";
import axios from "axios";
import { useParams, useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import "../../styles/paziente/dettaglioConversazione.css";

const DettaglioConversazione = () => {
  const { id } = useParams();
  const navigate = useNavigate();

  const [messaggi, setMessaggi] = useState([]);
  const [testo, setTesto] = useState("");
  const [loading, setLoading] = useState(true);
  const [errore, setErrore] = useState(null);

  const bottomRef = useRef(null);

  const token = localStorage.getItem("token");
  const idPaziente = localStorage.getItem("idPaziente");

  const scrollToBottom = () => {
    bottomRef.current?.scrollIntoView({ behavior: "smooth" });
  };

  const fetchMessaggi = async () => {
    if (!token || !id) return;

    try {
      const response = await axios.get(
        `http://localhost:8080/api/messaggi/${id}`,
        { headers: { Authorization: `Bearer ${token}` } }
      );

      const data = Array.isArray(response.data) ? response.data : [];
      setMessaggi(data);
      setLoading(false);

      setTimeout(scrollToBottom, 100);
    } catch (err) {
      console.error("Errore nel caricamento dei messaggi:", err);
      setErrore("Errore nel caricamento dei messaggi.");
      toast.error("Errore nel caricamento dei messaggi");
      setLoading(false);
    }
  };

  const inviaMessaggio = async () => {
    if (testo.trim() === "" || !token) return;

    try {
      await axios.post(
        "http://localhost:8080/api/messaggi/invia",
        null,
        {
          params: {
            conversazioneId: id,
            mittente: "PAZIENTE",
            testo: testo,
          },
          headers: { Authorization: `Bearer ${token}` },
        }
      );

      setTesto("");
      fetchMessaggi();
    } catch (err) {
      console.error("Errore invio messaggio:", err);
      toast.error("Errore nell'invio del messaggio");
    }
  };

  useEffect(() => {
    if (!token || !idPaziente) {
      setErrore("Sessione scaduta. Effettua nuovamente il login.");
      setLoading(false);
      return;
    }

    if (!id) {
      setErrore("Conversazione non valida.");
      setLoading(false);
      return;
    }

    fetchMessaggi();
  }, [id]);

  if (loading) return <p>Caricamento conversazione...</p>;
  if (errore) return <p className="error-message">{errore}</p>;

  return (
    <div className="chat-page">
      <div className="chat-container">

        <h2 className="chat-title">Conversazione con il medico</h2>

        <div className="chat-messages">
          {messaggi.map((m) => (
            <div
              key={m.id}
              className={`msg-bubble ${
                m.mittente === "PAZIENTE" ? "paziente" : "medico"
              }`}
            >
              <p>{m.testo}</p>

              <div className="msg-time">
                {m.timestamp
                  ? new Date(m.timestamp.replace(" ", "T")).toLocaleString(
                      "it-IT",
                      {
                        day: "2-digit",
                        month: "2-digit",
                        year: "numeric",
                        hour: "2-digit",
                        minute: "2-digit",
                      }
                    )
                  : ""}
              </div>
            </div>
          ))}

          <div ref={bottomRef}></div>
        </div>

        <div className="chat-input">
          <input
            type="text"
            placeholder="Scrivi un messaggio..."
            value={testo}
            onChange={(e) => setTesto(e.target.value)}
          />
          <button onClick={inviaMessaggio}>Invia</button>
        </div>

      </div>
    </div>
  );
};

export default DettaglioConversazione;
