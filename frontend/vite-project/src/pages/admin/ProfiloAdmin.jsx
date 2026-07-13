import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import SidebarAdmin from "../../components/SidebarAdmin.jsx";
import TopbarAdmin from "../../components/TopbarAdmin.jsx";
import "../../styles/admin/ProfiloAdmin.css";

export default function ProfiloAdmin() {
  const [admin, setAdmin] = useState(null);
  const [editing, setEditing] = useState(false);

  const [formData, setFormData] = useState({
    nome: "",
    cognome: "",
    email: ""
  });

  const navigate = useNavigate();

  useEffect(() => {
    const idUtente = localStorage.getItem("idUtente");
    const token = localStorage.getItem("token");

    if (!idUtente || !token) {
      navigate("/login");
      return;
    }

    fetch(`http://localhost:8080/admin/${idUtente}`, {
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
        setAdmin(data);
        setFormData({
          nome: data.nome || "",
          cognome: data.cognome || "",
          email: data.email || ""
        });
      })
      .catch((err) => console.error("Errore caricamento admin:", err));
  }, [navigate]);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const salvaModifiche = async () => {
    const token = localStorage.getItem("token");

    try {
      const res = await fetch(`http://localhost:8080/admin/${admin.id}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(formData),
      });

      if (!res.ok) throw new Error("Errore aggiornamento profilo");

      const updated = await res.json();
      setAdmin(updated);
      setEditing(false);
      alert("Profilo aggiornato con successo");
    } catch (err) {
      console.error(err);
      alert("Errore durante l'aggiornamento");
    }
  };

  if (!admin) return <p>Caricamento profilo...</p>;

  return (
  <div className="layout-admin">
    <SidebarAdmin />

    <div className="admin-main">
      <TopbarAdmin />

      <div className="admin-content">
        <h1 className="profilo-title">Profilo Amministratore</h1>

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

          <div className="profilo-buttons">
            {!editing ? (
              <button className="btn-modifica" onClick={() => setEditing(true)}>
                Modifica Profilo
              </button>
            ) : (
              <>
                <button className="btn-salva" onClick={salvaModifiche}>
                  Salva
                </button>
                <button className="btn-annulla" onClick={() => setEditing(false)}>
                  Annulla
                </button>
              </>
            )}

            <button
              className="btn-password"
              onClick={() => navigate("/admin/cambia-password")}
            >
              Cambia Password
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
);
}