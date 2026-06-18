import React, { useEffect, useState } from "react";
import axios from "axios";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";

// Import del CSS
import "../../styles/medico/ListaConversazioniMedico.css";

  const ListaConversazioniMedico = () => {
  const [conversazioni, setConversazioni] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchConversazioni = async () => {
      try {
        const token = localStorage.getItem("token");
        const idMedico = localStorage.getItem("idMedico");

        const response = await axios.get(
            `http://localhost:8080/api/conversazioni/medico/${idMedico}`,
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
    navigate(`/medico/conversazioni/${id}`);
  };

  return (
    <div className="lista-conversazioni-container">
      <h2 className="mb-3">Conversazioni con i Pazienti</h2>

      {conversazioni.length === 0 ? (
        <p>Nessuna conversazione disponibile.</p>
      ) : (
        <>
          {conversazioni.map((c) => (
            <div
              key={c.id}
              className="conversazione-card"
              onClick={() => apriChat(c.id)}
            >
              <div className="conversazione-header">
                <span className="conversazione-nome">
                  {c.pazienteNome} {c.pazienteCognome}
                </span>

                <span className="conversazione-data">
                  {new Date(c.ultimoAggiornamento).toLocaleString()}
                </span>
              </div>

              <p className="conversazione-messaggio">
                {c.ultimoMessaggio || "Nessun messaggio ancora"}
              </p>

              {c.nonLetti > 0 && (
                <span className="badge-non-letti">
                  {c.nonLetti} non letti
                </span>
              )}
            </div>
          ))}
        </>
      )}
    </div>
  );
};

export default ListaConversazioniMedico;
