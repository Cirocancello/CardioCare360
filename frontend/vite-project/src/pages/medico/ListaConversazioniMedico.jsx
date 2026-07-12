import React, { useEffect, useState } from "react";
import axios from "axios";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";

import SidebarMedico from "../../components/SidebarMedico";
import TopbarMedico from "../../components/TopbarMedico";

import "../../styles/medico/ListaConversazioniMedico.css";

const ListaConversazioniMedico = () => {
  const [conversazioni, setConversazioni] = useState([]);
  const [loading, setLoading] = useState(true);
  const [errore, setErrore] = useState(null);

  const navigate = useNavigate();

  useEffect(() => {
    const fetchConversazioni = async () => {
      const token = localStorage.getItem("token");
      const idMedico = localStorage.getItem("idMedico");

      if (!token || !idMedico) {
        setErrore("Sessione scaduta. Effettua nuovamente il login.");
        setLoading(false);
        return;
      }

      try {
        const response = await axios.get(
          `http://localhost:8080/api/conversazioni/medico/${idMedico}`,
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        );

        const data = response.data;
        setConversazioni(Array.isArray(data) ? data : []);
      } catch (err) {
        console.error("Errore nel caricamento conversazioni:", err);
        toast.error("Errore nel caricamento delle conversazioni");
        setErrore("Errore nel caricamento delle conversazioni.");
      } finally {
        setLoading(false);
      }
    };

    fetchConversazioni();
  }, []);

  const apriChat = (id) => {
    navigate(`/medico/conversazioni/${id}`);
  };

  if (loading) return <div className="lista-conversazioni-container">Caricamento...</div>;
  if (errore) return <div className="error-message">{errore}</div>;

  return (
    <div className="layout-medico">
      <SidebarMedico />

      <div className="lista-conversazioni-container">
        <TopbarMedico />

        <h2 className="lista-conversazioni-title">Conversazioni con i Pazienti</h2>

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
                    {c.ultimoAggiornamento
                      ? new Date(c.ultimoAggiornamento).toLocaleString()
                      : "—"}
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
    </div>
  );
};

export default ListaConversazioniMedico;
