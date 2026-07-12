import React, { useEffect, useState } from "react";
import axios from "axios";
import { toast } from "react-toastify";
import "../../styles/paziente/storicoParametri.css";

const StoricoParametri = () => {
  const [gruppi, setGruppi] = useState({});
  const [loading, setLoading] = useState(true);
  const [errore, setErrore] = useState(null);

  useEffect(() => {
    const fetchParametri = async () => {
      const token = localStorage.getItem("token");
      const idPaziente = localStorage.getItem("idPaziente");

      if (!token || !idPaziente) {
        setErrore("Sessione scaduta. Effettua di nuovo il login.");
        setLoading(false);
        return;
      }

      try {
        const response = await axios.get(
          `http://localhost:8080/api/pazienti/${idPaziente}/parametri`,
          { headers: { Authorization: `Bearer ${token}` } }
        );

        const data = Array.isArray(response.data) ? response.data : [];

        // Raggruppa per data in modo sicuro
        const grouped = data.reduce((acc, p) => {
          const rawDate = p.dataRilevazione;
          const data = rawDate
            ? new Date(rawDate).toLocaleString()
            : "Data non disponibile";

          if (!acc[data]) acc[data] = [];
          acc[data].push({
            id: p.id,
            nome: p.nome || "Parametro",
            valore: p.valore ?? "—",
            unitaMisura: p.unitaMisura || "",
          });

          return acc;
        }, {});

        setGruppi(grouped);
      } catch (err) {
        console.error("Errore storico parametri:", err);
        toast.error("Errore nel caricamento dello storico parametri");
        setErrore("Errore nel caricamento dello storico parametri.");
      } finally {
        setLoading(false);
      }
    };

    fetchParametri();
  }, []);

  if (loading) return <p className="storico-empty">Caricamento parametri...</p>;
  if (errore) return <p className="storico-empty">{errore}</p>;

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
