import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import SidebarAdmin from "../../components/SidebarAdmin";
import TopbarAdmin from "../../components/TopbarAdmin";
import "../../styles/admin/DettaglioPaziente.css";

export default function DettaglioPaziente() {
  const { id } = useParams();
  const navigate = useNavigate();
  const token = localStorage.getItem("token");

  const [paziente, setPaziente] = useState(null);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchPaziente = async () => {
      try {
        const res = await fetch(`http://localhost:8080/admin/pazienti/${id}`, {
          headers: { Authorization: `Bearer ${token}` },
        });

        if (res.status === 401) {
          setError("Sessione scaduta. Effettua di nuovo il login.");
          return;
        }

        if (res.status === 404) {
          setError("Paziente non trovato.");
          return;
        }

        if (!res.ok) {
          setError("Errore nel caricamento del paziente.");
          return;
        }

        const data = await res.json();

        // ⭐ Normalizzazione dei campi null
        const normalized = {
          id: data.id,
          nome: data.nome || "—",
          cognome: data.cognome || "—",
          email: data.email || "—",
          telefono: data.telefono || "—",
          codiceFiscale: data.codiceFiscale || "—",
          luogoNascita: data.luogoNascita || "—",
          dataNascita: data.dataNascita || "—",
          indirizzo: data.indirizzo || "—",
        };

        setPaziente(normalized);
      } catch (err) {
        console.error("Errore:", err);
        setError("Errore di connessione al server.");
      }
    };

    fetchPaziente();
  }, [id, token]);

  if (error) {
    return (
      <div className="layout-admin">
        <SidebarAdmin />
        <div className="dashboard-admin-container">
          <TopbarAdmin />
          <div className="dettaglio-paziente-content">
            <h1 className="title">Dettaglio Paziente</h1>
            <div className="error-box">{error}</div>
            <button className="btn-back" onClick={() => navigate("/admin/pazienti")}>
              Torna alla lista
            </button>
          </div>
        </div>
      </div>
    );
  }

  if (!paziente) return <p>Caricamento...</p>;

  return (
    <div className="layout-admin">
      <SidebarAdmin />
      <div className="dashboard-admin-container">
        <TopbarAdmin />

        <div className="dettaglio-paziente-content">
          <h1 className="title">Dettaglio Paziente</h1>

          <div className="info-box">
            <p><strong>ID:</strong> {paziente.id}</p>
            <p><strong>Nome:</strong> {paziente.nome}</p>
            <p><strong>Cognome:</strong> {paziente.cognome}</p>
            <p><strong>Email:</strong> {paziente.email}</p>
            <p><strong>Telefono:</strong> {paziente.telefono}</p>
            <p><strong>Codice Fiscale:</strong> {paziente.codiceFiscale}</p>
            <p><strong>Luogo di nascita:</strong> {paziente.luogoNascita}</p>
            <p><strong>Data di nascita:</strong> {paziente.dataNascita}</p>
            <p><strong>Indirizzo:</strong> {paziente.indirizzo}</p>
          </div>

          <div className="button-group">
            <button
              className="btn-edit"
              onClick={() => navigate(`/admin/pazienti/${id}/modifica`)}
            >
              Modifica
            </button>

            <button
              className="btn-delete"
              onClick={() => navigate(`/admin/pazienti/${id}/conferma-eliminazione`)}
            >
              Elimina
            </button>

            <button
              className="btn-back"
              onClick={() => navigate("/admin/pazienti")}
            >
              Torna alla lista
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
