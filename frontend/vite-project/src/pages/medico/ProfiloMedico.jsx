import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import SidebarMedico from "../../components/SidebarMedico.jsx";
import TopbarMedico from "../../components/TopbarMedico.jsx";
import "../../styles/medico/ProfiloMedico.css";

export default function ProfiloMedico() {
  const [medico, setMedico] = useState(null);
  const [editing, setEditing] = useState(false);
  const [formData, setFormData] = useState({});
  const [loading, setLoading] = useState(true);
  const [errore, setErrore] = useState(null);

  const navigate = useNavigate();

  useEffect(() => {
    const idMedico = localStorage.getItem("idMedico");
    const token = localStorage.getItem("token");

    // 🔒 Blindatura: token o id mancanti
    if (!idMedico || !token) {
      navigate("/login");
      return;
    }

    const fetchMedico = async () => {
      try {
        const res = await fetch(`http://localhost:8080/medico/${idMedico}`, {
          headers: { Authorization: `Bearer ${token}` },
        });

        if (!res.ok) {
          const text = await res.text();
          console.error(`Errore HTTP ${res.status}: ${text}`);
          throw new Error("Errore nel caricamento del profilo");
        }

        const data = await res.json();
        setMedico(data);
        setFormData(data);
      } catch (err) {
        console.error("Errore caricamento medico:", err);
        setErrore("Errore nel caricamento del profilo.");
      } finally {
        setLoading(false);
      }
    };

    fetchMedico();
  }, [navigate]);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const salvaModifiche = async () => {
    const token = localStorage.getItem("token");

    if (!token) {
      setErrore("Token mancante. Effettua nuovamente il login.");
      return;
    }

    try {
      const res = await fetch(`http://localhost:8080/medico/${medico.id}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(formData),
      });

      if (!res.ok) throw new Error("Errore aggiornamento profilo");

      const updated = await res.json();
      setMedico(updated);
      setEditing(false);
      alert("Profilo aggiornato con successo");
    } catch (err) {
      console.error(err);
      setErrore("Errore durante l'aggiornamento del profilo.");
    }
  };

  // ---------------------------------------------------------
  // RENDER
  // ---------------------------------------------------------

  if (loading) return <p className="loading-message">Caricamento profilo...</p>;

  if (errore) {
    return (
      <div className="layout-medico">
        <SidebarMedico />
        <div className="profilo-medico-container">
          <TopbarMedico />
          <p className="error-message">{errore}</p>
        </div>
      </div>
    );
  }

  if (!medico) {
    return (
      <div className="layout-medico">
        <SidebarMedico />
        <div className="profilo-medico-container">
          <TopbarMedico />
          <p className="error-message">Medico non trovato.</p>
        </div>
      </div>
    );
  }

  return (
    <div className="layout-medico">
      <SidebarMedico />

      <div className="profilo-medico-container">
        <TopbarMedico />

        <h1 className="profilo-title">Profilo Medico</h1>

        <div className="profilo-card">
          <div className="profilo-row">
            <label>Nome Completo</label>
            <input
              type="text"
              name="nomeCompleto"
              value={formData.nomeCompleto || ""}
              onChange={handleChange}
              disabled={!editing}
            />
          </div>

          <div className="profilo-row">
            <label>Email</label>
            <input
              type="email"
              name="email"
              value={formData.email || ""}
              onChange={handleChange}
              disabled={!editing}
            />
          </div>

          <div className="profilo-row">
            <label>Specializzazione</label>
            <input
              type="text"
              name="specializzazione"
              value={formData.specializzazione || ""}
              onChange={handleChange}
              disabled={!editing}
            />
          </div>

          <div className="profilo-buttons">
            {!editing ? (
              <button className="btn-modifica" onClick={() => setEditing(true)}>
                ✏️ Modifica Profilo
              </button>
            ) : (
              <>
                <button className="btn-salva" onClick={salvaModifiche}>
                  💾 Salva
                </button>
                <button className="btn-annulla" onClick={() => setEditing(false)}>
                  ❌ Annulla
                </button>
              </>
            )}

            <button
              className="btn-password"
              onClick={() => navigate("/medico/cambia-password")}
            >
              🔐 Cambia Password
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
