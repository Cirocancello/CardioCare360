import React, { useEffect, useState, useRef } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";

// Layout components
import SidebarMedico from "../../components/SidebarMedico";
import TopbarMedico from "../../components/TopbarMedico";

// CSS
import "../../styles/medico/ChatMedico.css";

export default function ChatMedico() {
  const { idConversazione } = useParams();
  const [messaggi, setMessaggi] = useState([]);
  const [testo, setTesto] = useState("");
  const [pazienteNome, setPazienteNome] = useState("");
  const navigate = useNavigate();
  const bottomRef = useRef(null);

  const scrollToBottom = () => {
    bottomRef.current?.scrollIntoView({ behavior: "smooth" });
  };

  const fetchMessaggi = async () => {
    try {
      const token = localStorage.getItem("token");

      const response = await axios.get(
        `http://localhost:8080/api/messaggi/${idConversazione}`,
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );

      setMessaggi(response.data);

      if (response.data.length > 0) {
        setPazienteNome(response.data[0].pazienteNomeCompleto || "Paziente");
      }

      scrollToBottom();
    } catch (err) {
      console.error("Errore caricamento messaggi:", err);
    }
  };

  const segnaLetti = async () => {
    try {
      const token = localStorage.getItem("token");
      await axios.put(
        `http://localhost:8080/api/messaggi/segna-letti/${idConversazione}`,
        {},
        { headers: { Authorization: `Bearer ${token}` } }
      );
    } catch (err) {
      console.error("Errore segna letti:", err);
    }
  };

  const inviaMessaggio = async () => {
    if (testo.trim() === "") return;

    try {
      const token = localStorage.getItem("token");

      await axios.post(
        `http://localhost:8080/api/messaggi/invia`,
        {},
        {
          params: {
            conversazioneId: idConversazione,
            mittente: "MEDICO",
            testo: testo,
          },
          headers: { Authorization: `Bearer ${token}` },
        }
      );

      setTesto("");
      fetchMessaggi();
    } catch (err) {
      console.error("Errore invio messaggio:", err);
    }
  };

  useEffect(() => {
    fetchMessaggi();
    segnaLetti();

    const interval = setInterval(fetchMessaggi, 3000);
    return () => clearInterval(interval);
  }, []);

  return (
    <div className="layout-medico">
      <SidebarMedico />

      <div className="chat-medico-container">
        <TopbarMedico />

        <div className="chat-header">
          <button
            className="back-btn"
            onClick={() => navigate("/medico/conversazioni")}
          >
            ←
          </button>
          <h3>Chat con {pazienteNome}</h3>
        </div>

        <div className="chat-messages">
          {messaggi.map((m) => (
            <div
              key={m.id}
              className={`chat-bubble ${
                m.mittente === "MEDICO" ? "medico" : "paziente"
              }`}
            >
              <p>{m.testo}</p>
              <span className="timestamp">
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
              </span>
            </div>
          ))}

          <div ref={bottomRef}></div>
        </div>

        <div className="chat-input">
          <input
            type="text"
            value={testo}
            onChange={(e) => setTesto(e.target.value)}
            placeholder="Scrivi un messaggio..."
          />
          <button onClick={inviaMessaggio}>Invia</button>
        </div>
      </div>
    </div>
  );
}
