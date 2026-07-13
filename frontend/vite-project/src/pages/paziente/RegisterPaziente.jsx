import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../../api/api";   // 🔥 usa axios configurato
import "../../styles/public/user.css";
import logo from "../../assets/logo-CardioCare360.png";

export default function RegisterPaziente() {
  const navigate = useNavigate();

  const [form, setForm] = useState({
    nome: "",
    cognome: "",
    luogoNascita: "",
    dataNascita: "",
    codiceFiscale: "",
    email: "",
    password: "",
    confermaPassword: "",
  });

  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleRegister = async (e) => {
    e.preventDefault();
    setError("");
    setSuccess("");

    if (form.password !== form.confermaPassword) {
      setError("Le password non coincidono.");
      return;
    }

    try {
      // 🔥 Endpoint ufficiale: registra SOLO pazienti
      await api.post("/auth/register-paziente", form);

      setSuccess("Registrazione completata! Reindirizzamento in corso...");
      setTimeout(() => navigate("/login"), 1500);

    } catch (err) {
      console.error("Errore registrazione:", err);

      if (err.response?.data === "EMAIL_DUPLICATA") {
        setError("Questa email è già registrata.");
      } else if (err.response?.data === "CF_DUPLICATO") {
        setError("Codice fiscale già presente nel sistema.");
      } else if (err.response?.data === "DATA_NON_VALIDA") {
        setError("Formato data non valido. Usa YYYY-MM-DD.");
      } else {
        setError("Registrazione fallita. Riprova.");
      }
    }
  };

  return (
    <div className="user-page">
      <div className="user-card">

        <img src={logo} alt="CardioCare360" className="user-logo" />

        <h2 className="user-title">Crea il tuo account paziente</h2>

        {error && (
          <p style={{ color: "red", marginBottom: "10px" }}>{error}</p>
        )}

        {success && (
          <p style={{ color: "green", marginBottom: "10px" }}>{success}</p>
        )}

        <form onSubmit={handleRegister}>

          <div className="user-field">
            <label className="user-label">Nome</label>
            <input
              name="nome"
              className="user-input"
              value={form.nome}
              onChange={handleChange}
              required
            />
          </div>

          <div className="user-field">
            <label className="user-label">Cognome</label>
            <input
              name="cognome"
              className="user-input"
              value={form.cognome}
              onChange={handleChange}
              required
            />
          </div>

          <div className="user-field">
            <label className="user-label">Luogo di nascita</label>
            <input
              name="luogoNascita"
              className="user-input"
              value={form.luogoNascita}
              onChange={handleChange}
              required
            />
          </div>

          <div className="user-field">
            <label className="user-label">Data di nascita</label>
            <input
              name="dataNascita"
              type="date"
              className="user-input"
              value={form.dataNascita}
              onChange={handleChange}
              required
            />
          </div>

          <div className="user-field">
            <label className="user-label">Codice Fiscale</label>
            <input
              name="codiceFiscale"
              className="user-input"
              value={form.codiceFiscale}
              onChange={handleChange}
              required
            />
          </div>

          <div className="user-field">
            <label className="user-label">Email</label>
            <input
              name="email"
              type="email"
              className="user-input"
              value={form.email}
              onChange={handleChange}
              required
            />
          </div>

          <div className="user-field">
            <label className="user-label">Password</label>
            <input
              name="password"
              type="password"
              className="user-input"
              value={form.password}
              onChange={handleChange}
              required
            />
          </div>

          <div className="user-field">
            <label className="user-label">Conferma Password</label>
            <input
              name="confermaPassword"
              type="password"
              className="user-input"
              value={form.confermaPassword}
              onChange={handleChange}
              required
            />
          </div>

          <button type="submit" className="user-button">
            Registrati
          </button>
        </form>

        <a href="/login" className="user-link">
          Hai già un account? Accedi
        </a>

      </div>
    </div>
  );
}
