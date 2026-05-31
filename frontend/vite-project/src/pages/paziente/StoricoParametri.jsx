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
                <p key={p.id} className="parametro-info">
                  <span>{p.nome}:</span> {p.valore} {p.unitaMisura}
                </p>
              ))}
            </div>
          ))
        )}
      </div>
    </div>
  );
};

export default StoricoParametri;
