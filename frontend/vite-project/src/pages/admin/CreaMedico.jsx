import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import SidebarAdmin from "../../components/SidebarAdmin";
import TopbarAdmin from "../../components/TopbarAdmin";
import "../../styles/admin/CreaMedico.css";

export default function CreaMedico() {
  const navigate = useNavigate();
  const token = localStorage.getItem("token");

  const [form, setForm] = useState({
    nome: "",
    cognome: "",
    email: "",
    password: "",
    specializzazione: "",
    telefono: "",
  });

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const res = await fetch("http://localhost:8080/admin/medici", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(form),
      });

      if (res.ok) {
        alert("Medico creato con successo!");
        navigate("/admin/medici");
      } else {
        alert("Errore nella creazione del medico");
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
          <h1 className="title">Aggiungi Nuovo Medico</h1>

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

            <label>Password</label>
            <input
              type="password"
              name="password"
              value={form.password}
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
              Salva
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
