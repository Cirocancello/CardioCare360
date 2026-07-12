import React, { useEffect, useState } from "react";
import SidebarAdmin from "../../components/SidebarAdmin";
import TopbarAdmin from "../../components/TopbarAdmin";
import "../../styles/admin/GestionePazienti.css";
import { useNavigate } from "react-router-dom";

export default function GestionePazienti() {
  const [pazienti, setPazienti] = useState([]);
  const [error, setError] = useState("");
  const token = localStorage.getItem("token");
  const navigate = useNavigate();

  useEffect(() => {
    const fetchPazienti = async () => {
      try {
        const res = await fetch("http://localhost:8080/admin/pazienti", {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        });

        if (res.status === 401) {
          setError("Sessione scaduta. Effettua di nuovo il login.");
          return;
        }

        if (!res.ok) {
          setError("Errore nel caricamento dei pazienti");
          return;
        }

        const data = await res.json();

        // ⭐ Normalizzazione dei dati
        const normalized = data.map((p) => ({
          id: p.id,
          nome: p.nome || "—",
          email: p.email || "—",
          telefono: p.telefono || "—",
        }));

        setPazienti(normalized);
      } catch (err) {
        console.error("Errore caricamento pazienti:", err);
        setError("Errore di connessione al server");
      }
    };

    if (token) fetchPazienti();
  }, [token]);

  return (
    <div className="layout-admin">
      <SidebarAdmin />

      <div className="dashboard-admin-container">
        <TopbarAdmin />

        <div className="gestione-pazienti-content">
          <h1 className="title">Gestione Pazienti</h1>

          {/* ⭐ Messaggio di errore */}
          {error && <div className="error-box">{error}</div>}

          <button
            className="btn-add"
            onClick={() => navigate("/admin/paziente/crea")}
          >
            Aggiungi Paziente
          </button>

          <table className="pazienti-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Nome</th>
                <th>Email</th>
                <th>Telefono</th>
                <th>Azioni</th>
              </tr>
            </thead>

            <tbody>
              {pazienti.length > 0 ? (
                pazienti.map((p) => (
                  <tr key={p.id}>
                    <td>{p.id}</td>
                    <td>{p.nome}</td>
                    <td>{p.email}</td>
                    <td>{p.telefono}</td>
                    <td>
                      <button
                        className="btn-details"
                        onClick={() => navigate(`/admin/pazienti/${p.id}`)}
                      >
                        Dettagli
                      </button>

                      <button
                        className="btn-edit"
                        onClick={() =>
                          navigate(`/admin/pazienti/${p.id}/modifica`)
                        }
                      >
                        Modifica
                      </button>

                      <button
                        className="btn-delete"
                        onClick={() =>
                          navigate(`/admin/pazienti/${p.id}/conferma-eliminazione`)
                        }
                      >
                        Elimina
                      </button>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="5" style={{ textAlign: "center", padding: "20px" }}>
                    Nessun paziente trovato.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
