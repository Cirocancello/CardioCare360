import { useEffect, useState } from "react";
import { useParams, Link } from "react-router-dom";

import SidebarPaziente from "../../components/SidebarPaziente.jsx";
import TopbarPaziente from "../../components/TopbarPaziente.jsx";

import "../../styles/paziente/appuntamenti.css";

export default function DettaglioAppuntamento() {
  const { id } = useParams();
  const [appuntamento, setAppuntamento] = useState(null);
  const [loading, setLoading] = useState(true);
  const [errore, setErrore] = useState(null);

  useEffect(() => {
    const token = localStorage.getItem("token");

    if (!token) {
      setErrore("Token non trovato. Effettua di nuovo il login.");
      setLoading(false);
      return;
    }

    async function fetchData() {
      try {
        const res = await fetch(`http://localhost:8080/appuntamenti/${id}`, {
          headers: { Authorization: `Bearer ${token}` },
        });

        if (res.status === 403) {
          throw new Error("Non hai i permessi per visualizzare questo appuntamento");
        }

        if (!res.ok) {
          throw new Error("Errore nel caricamento dell'appuntamento");
        }

        const data = await res.json();

        let medico = null;
        if (data.idMedico) {
          try {
            const resMedico = await fetch(`http://localhost:8080/medici/${data.idMedico}`, {
              headers: { Authorization: `Bearer ${token}` },
            });
            medico = resMedico.ok ? await resMedico.json() : null;
          } catch {
            medico = null;
          }
        }

        setAppuntamento({ ...data, medico });
      } catch (err) {
        console.error(err);
        setErrore(err.message || "Errore imprevisto");
      } finally {
        setLoading(false);
      }
    }

    fetchData();
  }, [id]);

  if (loading) return <p className="loading">Caricamento appuntamento...</p>;
  if (errore) return <p className="error-message">{errore}</p>;
  if (!appuntamento) return <p>Appuntamento non trovato.</p>;

  const stato = appuntamento.stato?.toLowerCase() || "sconosciuto";
  const isModificabile = stato === "prenotato" || stato === "confermato";

  return (
    <div className="layout-paziente">

      {/* Sidebar */}
      <SidebarPaziente />

      {/* Contenuto */}
      <div className="appuntamenti-container">

        {/* Topbar */}
        <TopbarPaziente />

        <h1 className="page-title">Dettaglio Appuntamento</h1>

        <div className="appointment-detail-card">
          <h2>
            {appuntamento.dataAppuntamento} – {appuntamento.oraAppuntamento}
          </h2>

          <p><strong>Stato:</strong> {appuntamento.stato}</p>
          <p><strong>Tipo visita:</strong> {appuntamento.tipoVisita || "Non specificato"}</p>
          <p><strong>Note:</strong> {appuntamento.note || "Nessuna nota"}</p>

          <hr />

          <h3>Medico</h3>
          {appuntamento.medico ? (
            <>
              <p><strong>Nome:</strong> {appuntamento.medico.nomeCompleto || "N/D"}</p>
              <p><strong>Email:</strong> {appuntamento.medico.email || "N/D"}</p>
              <p><strong>Specializzazione:</strong> {appuntamento.medico.specializzazione || "N/D"}</p>
              <p><strong>Licenza:</strong> {appuntamento.medico.numeroLicenza || "N/D"}</p>
            </>
          ) : (
            <p>Informazioni medico non disponibili.</p>
          )}

          <div className="actions">
            <Link to="/paziente/appuntamenti" className="btn-secondary">
              ⬅ Torna agli appuntamenti
            </Link>

            {isModificabile && (
              <>
                <Link
                  to={`/paziente/appuntamenti/${id}/modifica`}
                  className="btn-primary"
                >
                  Modifica
                </Link>

                <Link
                  to={`/paziente/appuntamenti/${id}/annulla`}
                  className="btn-danger"
                >
                  Annulla
                </Link>
              </>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
