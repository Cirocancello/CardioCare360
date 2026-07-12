import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";

import SidebarPaziente from "../../components/SidebarPaziente.jsx";
import TopbarPaziente from "../../components/TopbarPaziente.jsx";

import "../../styles/paziente/appuntamenti.css";

export default function ModificaAppuntamento() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [form, setForm] = useState({
    dataAppuntamento: "",
    oraAppuntamento: "",
    note: ""
  });

  const [loading, setLoading] = useState(true);
  const [errore, setErrore] = useState(null);
  const [salvando, setSalvando] = useState(false);

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
          headers: { Authorization: `Bearer ${token}` }
        });

        if (res.status === 403) {
          throw new Error("Non hai i permessi per modificare questo appuntamento");
        }

        if (!res.ok) {
          throw new Error("Errore nel caricamento dell'appuntamento");
        }

        const data = await res.json();

        const stato = data.stato?.toLowerCase();
        if (stato === "completato" || stato === "annullato") {
          throw new Error("Questo appuntamento non può essere modificato");
        }

        setForm({
          dataAppuntamento: data.dataAppuntamento,
          oraAppuntamento: data.oraAppuntamento,
          note: data.note || ""
        });
      } catch (err) {
        setErrore(err.message);
      } finally {
        setLoading(false);
      }
    }

    fetchData();
  }, [id]);

  function handleChange(e) {
    setForm({ ...form, [e.target.name]: e.target.value });
  }

  async function handleSubmit(e) {
    e.preventDefault();

    if (!form.dataAppuntamento) {
      setErrore("La data è obbligatoria");
      return;
    }

    if (!form.oraAppuntamento) {
      setErrore("L'orario è obbligatorio");
      return;
    }

    if (form.note && form.note.length > 2000) {
      setErrore("Le note non possono superare 2000 caratteri");
      return;
    }

    setSalvando(true);
    setErrore(null);

    const token = localStorage.getItem("token");

    try {
      const res = await fetch(`http://localhost:8080/appuntamenti/${id}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`
        },
        body: JSON.stringify(form)
      });

      if (!res.ok) {
        const text = await res.text();
        throw new Error(text || "Errore durante il salvataggio");
      }

      navigate(`/paziente/appuntamenti/${id}`);
    } catch (err) {
      setErrore(err.message);
    } finally {
      setSalvando(false);
    }
  }

  if (loading) return <p>Caricamento appuntamento...</p>;
  if (errore) return <p className="error-message">{errore}</p>;

  return (
    <div className="layout-paziente">

      {/* Sidebar */}
      <SidebarPaziente />

      {/* Contenuto */}
      <div className="appuntamenti-container">

        {/* Topbar */}
        <TopbarPaziente />

        <h1 className="page-title">Modifica Appuntamento</h1>

        <form className="appointment-form" onSubmit={handleSubmit}>
          <label>Data</label>
          <input
            type="date"
            name="dataAppuntamento"
            value={form.dataAppuntamento}
            onChange={handleChange}
            required
          />

          <label>Ora</label>
          <input
            type="time"
            name="oraAppuntamento"
            value={form.oraAppuntamento}
            onChange={handleChange}
            required
          />

          <label>Note</label>
          <textarea
            name="note"
            value={form.note}
            onChange={handleChange}
            maxLength={2000}
          />

          <div className="actions">
            <button
              type="button"
              className="btn-secondary"
              onClick={() => navigate(`/paziente/appuntamenti/${id}`)}
            >
              ⬅ Annulla
            </button>

            <button className="btn-primary" type="submit" disabled={salvando}>
              {salvando ? "Salvataggio..." : "Salva modifiche"}
            </button>
          </div>
        </form>

      </div>
    </div>
  );
}
