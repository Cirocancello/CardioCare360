import React, { useEffect, useState } from "react";
import axios from "axios";
import { toast } from "react-toastify";
import "../../styles/paziente/storicoParametri.css";

const StoricoParametri = () => {
  const [gruppi, setGruppi] = useState({});

  useEffect(() => {
    const fetchParametri = async () => {
      try {
        const token = localStorage.getItem("token");
        const idPaziente = localStorage.getItem("idPaziente");

        if (!token || !idPaziente) {
          toast.error("Sessione scaduta. Effettua di nuovo il login.");
          return;
        }

        const response = await axios.get(
          `http://localhost:8080/api/pazienti/${idPaziente}/parametri`,
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        );

        // Raggruppa per data
        const grouped = response.data.reduce((acc, p) => {
          const data = new Date(p.dataRilevazione).toLocaleString();
          if (!acc[data]) acc[data] = [];
          acc[data].push(p);
          return acc;
        }, {});

        setGruppi(grouped);
      } catch (err) {
        toast.error("Errore nel caricamento dello storico parametri");
        console.error(err);
      }
    };

    fetchParametri();
  }, []);

  // Funzione per ottenere il nome del parametro
  const getNomeParametro = (tipo) => {
    switch (tipo) {
      case "PRESSIONE_SIS": return "Pressione Sistolica";
      case "PRESSIONE_DIA": return "Pressione Diastolica";
      case "BATTITI": return "Battiti";
      case "GLICEMIA": return "Glicemia";
      case "SATURAZIONE": return "Saturazione";
      case "PESO": return "Peso";
      case "TEMPERATURA": return "Temperatura";
      default: return tipo;
    }
  };

  // Funzione per ottenere il valore corretto
  const getValoreParametro = (p) => {
    switch (p.tipo) {
      case "PRESSIONE_SIS": return p.pressioneSistolica;
      case "PRESSIONE_DIA": return p.pressioneDiastolica;
      case "BATTITI": return p.battiti;
      case "GLICEMIA": return p.glicemia;
      case "SATURAZIONE": return p.saturazione;
      case "PESO": return p.peso;
      case "TEMPERATURA": return p.temperatura;
      default: return "";
    }
  };

  return (
    <div className="storico-page">
      <div className="storico-container">
        <h2 className="storico-title">Storico Parametri Vitali</h2>

        {Object.keys(gruppi).length === 0 ? (
          <p className="storico-empty">Nessun parametro registrato.</p>
        ) : (
          Object.entries(gruppi).map(([data, parametri]) => (
            <div key={data} className="parametro-card">
              <h5 className="parametro-data">{data}</h5>

              {parametri.map((p) => (
                <div key={p.id} className="parametro-info">
                  
                  {/* Nome parametro */}
                  <p>
                    <strong>{getNomeParametro(p.tipo)}:</strong>
                  </p>

                  {/* Valore parametro */}
                  <p>
                    {getValoreParametro(p)} {p.unitaMisura || ""}
                  </p>

                  {/* Alert */}
                  {p.alert && (
                    <p style={{ color: "red", fontWeight: "bold" }}>
                      ⚠️ {p.alert}
                    </p>
                  )}
                </div>
              ))}
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

export default StoricoParametri;