import React, { useEffect, useState, useRef } from "react";
import axios from "axios";
import { useParams } from "react-router-dom";
import { toast } from "react-toastify";
import "../../styles/paziente/dettaglioConversazione.css";

const DettaglioConversazione = () => {
  const { id } = useParams();
  const [messaggi, setMessaggi] = useState([]);
  const [testo, setTesto] = useState("");
  const [loading, setLoading] = useState(true);
  const bottomRef = useRef(null);

  const token = localStorage.getItem("token");

  const scrollToBottom = () => {
    bottomRef.current?.scrollIntoView({ behavior: "smooth" });
  };

  const fetchMessaggi = async () => {
    try {
      const response = await axios.get(
        `http://localhost:8080/api/messaggi/${id}`,
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );

      setMessaggi(response.data);
      setLoading(false);

      setTimeout(scrollToBottom, 100);

    } catch (err) {
      toast.error("Errore nel caricamento dei messaggi");
      console.error(err);
    }
  };

  useEffect(() => {
    fetchMessaggi();
  }, []);

  const inviaMessaggio = async () => {
    if (testo.trim() === "") return;

    try {
      await axios.post(
        "http://localhost:8080/api/messaggi/invia",
        null,
        {
          params: {
            conversazioneId: id,
            mittente: "PAZIENTE",
            testo: testo
          },
          headers: { Authorization: `Bearer ${token}` }
        }
      );

      setTesto("");
      fetchMessaggi();

    } catch (err) {
      toast.error("Errore nell'invio del messaggio");
      console.error(err);
    }
  };

  if (loading) return <p>Caricamento conversazione...</p>;

  return (
    <div className="chat-page">
      <div className="chat-container">

        <h2 className="chat-title">Conversazione con il medico</h2>

        <div className="chat-messages">
          {messaggi.map((m) => (
            <div
              key={m.id}
              className={`msg-bubble ${m.mittente === "PAZIENTE" ? "paziente" : "medico"}`}
            >
              <p>{m.testo}</p>
              <div className="msg-time">
                {new Date(m.timestamp).toLocaleString()}
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
