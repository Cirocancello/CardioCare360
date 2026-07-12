import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import SidebarMedico from "../../components/SidebarMedico.jsx";
import TopbarMedico from "../../components/TopbarMedico.jsx";
import "../../styles/medico/ListaPazienti.css";

export default function ListaPazienti() {
  const [pazienti, setPazienti] = useState([]);
  const [search, setSearch] = useState("");
  const [loading, setLoading] = useState(true);
  const [errore, setErrore] = useState(null);

  const navigate = useNavigate();

  useEffect(() => {
    const fetchPazienti = async () => {
      const token = localStorage.getItem("token");

      // 🔒 Blindatura: token mancante
      if (!token) {
        setErrore("Token mancante. Effettua nuovamente il login.");
        setLoading(false);
        return;
      }

      try {
        const res = await fetch("http://localhost:8080/paziente/all", {
          headers: { Authorization: `Bearer ${token}` },
        });

        if (!res.ok) {
          throw new Error("Errore nella risposta del server");
        }

        const data = await res.json();
        setPazienti(data || []);
      } catch (err) {
        console.error("Errore caricamento pazienti:", err);
        setErrore("Errore nel caricamento dei pazienti.");
      } finally {
        setLoading(false);
      }
    };

    fetchPazienti();
  }, []);

  // 🔒 Filtraggio sicuro
  const pazientiFiltrati = pazienti.filter((p) => {
    const nome = p.nomeCompleto || "";
    return nome.toLowerCase().includes(search.toLowerCase());
  });

  // ---------------------------------------------------------
  // RENDER
  // ---------------------------------------------------------

  if (loading) return <p className="loading-message">Caricamento pazienti...</p>;

  if (errore) return <p className="error-message">{errore}</p>;

  return (
    <div className="layout-medico">
      <SidebarMedico />

      <div className="lista-visite-container">
        <TopbarMedico />

        <h1 className="title">Lista Pazienti</h1>

        <input
          type="text"
          placeholder="Cerca paziente..."
          className="search-input"
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />

        {pazientiFiltrati.length === 0 ? (
          <p className="empty-message">Nessun paziente trovato.</p>
        ) : (
          <table className="tabella-visite">
            <thead>
              <tr>
                <th>Nome</th>
                <th>Codice Fiscale</th>
                <th>Email</th>
                <th>Telefono</th>
                <th>Azioni</th>
              </tr>
            </thead>

            <tbody>
              {pazientiFiltrati.map((p) => (
                <tr key={p.id}>
                  <td>{p.nomeCompleto || "—"}</td>
                  <td>{p.codiceFiscale || "—"}</td>
                  <td>{p.email || "—"}</td>
                  <td>{p.telefono || "—"}</td>

                  <td>
                    <button
                      className="btn-dettagli"
                      onClick={() => navigate(`/medico/pazienti/${p.id}`)}
                    >
                      Dettaglio
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}
