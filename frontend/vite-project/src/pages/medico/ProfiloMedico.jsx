import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import SidebarMedico from "../../components/SidebarMedico.jsx";
import TopbarMedico from "../../components/TopbarMedico.jsx";
import "../../styles/medico/ProfiloMedico.css";

export default function ProfiloMedico() {
  const [medico, setMedico] = useState(null);
  const [editing, setEditing] = useState(false);

  const [formData, setFormData] = useState({
    nome: "",
    cognome: "",
    email: "",
    specializzazione: ""
  });

  const navigate = useNavigate();

  // ---------------------------------------------------------
  // CARICAMENTO PROFILO MEDICO
  // ---------------------------------------------------------
  useEffect(() => {
    const idMedico = localStorage.getItem("idMedico");
    const token = localStorage.getItem("token");

    if (!idMedico || !token) {
      navigate("/login");
      return;
    }

    fetch(`http://localhost:8080/medico/${idMedico}`, {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then(async (res) => {
        if (!res.ok) {
          const text = await res.text();
          console.error(`Errore HTTP ${res.status}: ${text}`);
          throw new Error(`Errore HTTP ${res.status}`);
        }
        return res.json();
      })
      .then((data) => {
        setMedico(data);
        setFormData({
          nome: data.nome,
          cognome: data.cognome,
          email: data.email,
          specializzazione: data.specializzazione
        });
      })
      .catch((err) => console.error("Errore caricamento medico:", err));
  }, [navigate]);

  // ---------------------------------------------------------
  // GESTIONE CAMBI INPUT
  // ---------------------------------------------------------
  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  // ---------------------------------------------------------
  // SALVATAGGIO MODIFICHE PROFILO
  // ---------------------------------------------------------
  const salvaModifiche = async () => {
    const token = localStorage.getItem("token");

    try {
      const res = await fetch(`http://localhost:8080/medico/${medico.id}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          nome: formData.nome,
          cognome: formData.cognome,
          email: formData.email,
          specializzazione: formData.specializzazione
        }),
      });

      if (!res.ok) throw new Error("Errore aggiornamento profilo");

      const updated = await res.json();
      setMedico(updated);
      setEditing(false);
      alert("Profilo aggiornato con successo");
    } catch (err) {
      console.error(err);
      alert("Errore durante l'aggiornamento");
    }
  };

  if (!medico) return <p>Caricamento profilo...</p>;

  // ---------------------------------------------------------
  // RENDER
  // ---------------------------------------------------------
  return (
    <div className="layout-medico">
      <SidebarMedico />

      <div className="profilo-medico-container">
        <TopbarMedico />

        <h1 className="profilo-title">Profilo Medico</h1>

        <div className="profilo-card">

          <div className="profilo-row">
            <label>Nome</label>
            <input
              type="text"
              name="nome"
              value={formData.nome}
              onChange={handleChange}
              disabled={!editing}
            />
          </div>

          <div className="profilo-row">
            <label>Cognome</label>
            <input
              type="text"
              name="cognome"
              value={formData.cognome}
              onChange={handleChange}
              disabled={!editing}
            />
          </div>

          <div className="profilo-row">
            <label>Email</label>
            <input
              type="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              disabled={!editing}
            />
          </div>

          <div className="profilo-row">
            <label>Specializzazione</label>
            <input
              type="text"
              name="specializzazione"
              value={formData.specializzazione}
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
