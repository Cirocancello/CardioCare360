import React, { useEffect, useState, useRef } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";

import SidebarMedico from "../../components/SidebarMedico";
import TopbarMedico from "../../components/TopbarMedico";

import "../../styles/medico/ChatMedico.css";

export default function ChatMedico() {
  const { idConversazione } = useParams();
  const [messaggi, setMessaggi] = useState([]);
  const [testo, setTesto] = useState("");
  const [pazienteNome, setPazienteNome] = useState("Paziente");
  const [errore, setErrore] = useState(null);

  const navigate = useNavigate();
  const bottomRef = useRef(null);

  const token = localStorage.getItem("token");
  const idMedico = localStorage.getItem("idMedico");

  const scrollToBottom = () => {
    bottomRef.current?.scrollIntoView({ behavior: "smooth" });
  };

  const fetchMessaggi = async () => {
    if (!token || !idConversazione) return;

    try {
      const response = await axios.get(
        `http://localhost:8080/api/messaggi/${idConversazione}`,
        { headers: { Authorization: `Bearer ${token}` } }
      );

      const data = Array.isArray(response.data) ? response.data : [];
      setMessaggi(data);

      if (data.length > 0) {
        setPazienteNome(data[0].pazienteNomeCompleto || "Paziente");
      }

      scrollToBottom();
    } catch (err) {
      console.error("Errore caricamento messaggi:", err);
      setErrore("Errore nel caricamento dei messaggi.");
    }
  };

  const segnaLetti = async () => {
    if (!token || !idConversazione) return;

    try {
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
    if (testo.trim() === "" || !token) return;

    try {
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
      setErrore("Errore durante l'invio del messaggio.");
    }
  };

  useEffect(() => {
    if (!token || !idMedico) {
      setErrore("Sessione scaduta. Effettua nuovamente il login.");
      return;
    }

    if (!idConversazione) {
      setErrore("Conversazione non valida.");
      return;
    }

    fetchMessaggi();
    segnaLetti();

    const interval = setInterval(fetchMessaggi, 3000);
    return () => clearInterval(interval);
  }, [idConversazione]);

  if (errore) {
    return (
      <div className="layout-medico">
        <SidebarMedico />
        <div className="chat-medico-container">
          <TopbarMedico />
          <p className="error-message">{errore}</p>
        </div>
      </div>
    );
  }

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
