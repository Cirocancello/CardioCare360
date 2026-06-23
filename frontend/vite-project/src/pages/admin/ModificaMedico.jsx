import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import SidebarAdmin from "../../components/SidebarAdmin";
import TopbarAdmin from "../../components/TopbarAdmin";
import "../../styles/admin/CreaMedico.css";

export default function ModificaMedico() {
  const { id } = useParams();
  const navigate = useNavigate();
  const token = localStorage.getItem("token");

  const [form, setForm] = useState({
    nome: "",
    cognome: "",
    email: "",
    specializzazione: "",
    telefono: "",
  });

  // Carica i dati del medico
  useEffect(() => {
    const fetchMedico = async () => {
      try {
        const res = await fetch(`http://localhost:8080/admin/medici/${id}`, {
          headers: { Authorization: `Bearer ${token}` },
        });

        if (!res.ok) {
          alert("Errore nel caricamento del medico");
          return;
        }

        const data = await res.json();
        setForm(data);
      } catch (err) {
        console.error("Errore caricamento medico:", err);
      }
    };

    fetchMedico();
  }, [id, token]);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const res = await fetch(`http://localhost:8080/admin/medici/${id}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(form),
      });

      if (res.ok) {
        alert("Medico modificato con successo!");
        navigate("/admin/medici");
      } else {
        alert("Errore nella modifica del medico");
      }
    } catch (err) {
      console.error("Errore:", err);
      alert("Errore di connessione al server");
    }
  };

  return (
    <div className="layout-admin">
      <SidebarAdmin />
      <div className="dashboard-admin-container">
        <TopbarAdmin />

        <div className="crea-medico-content">
          <h1 className="title">Modifica Medico</h1>

          <form className="crea-medico-form" onSubmit={handleSubmit}>
            <label>Nome</label>
            <input
              type="text"
              name="nome"
              value={form.nome}
              onChange={handleChange}
              required
            />

            <label>Cognome</label>
            <input
              type="text"
              name="cognome"
              value={form.cognome}
              onChange={handleChange}
              required
            />

            <label>Email</label>
            <input
              type="email"
              name="email"
              value={form.email}
              onChange={handleChange}
              required
            />

            <label>Specializzazione</label>
            <input
              type="text"
              name="specializzazione"
              value={form.specializzazione}
              onChange={handleChange}
              required
            />           

            <button type="submit" className="btn-save">
              Salva Modifiche
            </button>

            <button
              type="button"
              className="btn-cancel"
              onClick={() => navigate("/admin/medici")}
            >
              Annulla
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}
