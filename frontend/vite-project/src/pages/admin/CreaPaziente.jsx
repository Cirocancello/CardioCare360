import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import SidebarAdmin from "../../components/SidebarAdmin";
import TopbarAdmin from "../../components/TopbarAdmin";
import "../../styles/admin/creapaziente.css";

export default function CreaPaziente() {
  const navigate = useNavigate();
  const token = localStorage.getItem("token");

  const [form, setForm] = useState({
    nome: "",
    cognome: "",
    email: "",
    password: "",
    telefono: "",
    codiceFiscale: "",
    luogoNascita: "",
    dataNascita: "",
    indirizzo: "",
  });

  const [error, setError] = useState("");

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  // ⭐ VALIDAZIONI FRONTEND
  const validateForm = () => {
    if (!form.nome.trim()) return "Il nome è obbligatorio";
    if (!form.cognome.trim()) return "Il cognome è obbligatorio";

    // Email valida
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(form.email)) return "Email non valida";

    // Password valida
    if (form.password.length < 6) return "La password deve avere almeno 6 caratteri";

    // Codice fiscale valido
    if (form.codiceFiscale.length !== 16)
      return "Il codice fiscale deve essere di 16 caratteri";

    // Telefono valido
    if (form.telefono && !/^\d{8,15}$/.test(form.telefono))
      return "Il telefono deve contenere solo numeri (8-15 cifre)";

    // Data di nascita valida
    if (form.dataNascita) {
      const data = new Date(form.dataNascita);
      if (data > new Date()) return "La data di nascita non può essere futura";
    }

    return null;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    // ⭐ VALIDAZIONE PRIMA DELL’INVIO
    const validationError = validateForm();
    if (validationError) {
      setError(validationError);
      return;
    }

    setError("");

    try {
      const res = await fetch("http://localhost:8080/admin/pazienti", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(form),
      });

      if (res.ok) {
        alert("Paziente creato con successo!");
        navigate("/admin/pazienti");
      } else {
        const text = await res.text();

        // ⭐ GESTIONE ERRORI BACKEND
        if (text.includes("EMAIL_DUPLICATA")) setError("Email già registrata");
        else if (text.includes("CODICE_FISCALE_DUPLICATO"))
          setError("Codice fiscale già registrato");
        else if (text.includes("TELEFONO_NON_VALIDO"))
          setError("Telefono non valido");
        else if (text.includes("DATA_NASCITA_NON_VALIDA"))
          setError("Data di nascita non valida");
        else setError("Errore nella creazione del paziente");
      }
    } catch (err) {
      console.error("Errore:", err);
      setError("Errore di connessione al server");
    }
  };

  return (
    <div className="layout-admin">
      <SidebarAdmin />
      <div className="dashboard-admin-container">
        <TopbarAdmin />

        <div className="crea-paziente-content">
          <h1 className="title">Aggiungi Nuovo Paziente</h1>

          {/* ⭐ MESSAGGIO DI ERRORE */}
          {error && <div className="error-box">{error}</div>}

          <form className="crea-form" onSubmit={handleSubmit}>
            <label>Nome</label>
            <input type="text" name="nome" value={form.nome} onChange={handleChange} required />

            <label>Cognome</label>
            <input type="text" name="cognome" value={form.cognome} onChange={handleChange} required />

            <label>Email</label>
            <input type="email" name="email" value={form.email} onChange={handleChange} required />

            <label>Password</label>
            <input type="password" name="password" value={form.password} onChange={handleChange} required />

            <label>Telefono</label>
            <input type="text" name="telefono" value={form.telefono} onChange={handleChange} />

            <label>Codice Fiscale</label>
            <input type="text" name="codiceFiscale" value={form.codiceFiscale} onChange={handleChange} required />

            <label>Luogo di nascita</label>
            <input type="text" name="luogoNascita" value={form.luogoNascita} onChange={handleChange} required />

            <label>Data di nascita</label>
            <input type="date" name="dataNascita" value={form.dataNascita} onChange={handleChange} required />

            <label>Indirizzo</label>
            <input type="text" name="indirizzo" value={form.indirizzo} onChange={handleChange} />

            <div className="form-buttons">
              <button type="submit" className="btn-save">Crea Paziente</button>
              <button type="button" className="btn-cancel" onClick={() => navigate("/admin/pazienti")}>
                Annulla
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}
