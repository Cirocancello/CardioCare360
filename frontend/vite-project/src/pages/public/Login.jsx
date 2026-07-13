import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { login } from "../../api/auth";
import "../../styles/public/user.css";
import logo from "../../assets/logo-CardioCare360.png";
import Navbar from "../../components/Navbar";
import Footer from "../../components/Footer";

export default function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
  e.preventDefault();

  try {
    console.log("TRY PARTITO");

    const data = await login(email, password);
    console.log("RISPOSTA LOGIN:", data);

    if (!data) {
      alert("Credenziali non valide");
      return;
    }

    // 🔥 Salva dati comuni
    localStorage.setItem("token", data.token);
    localStorage.setItem("ruolo", data.ruolo);
    localStorage.setItem("idUtente", data.idUtente);

    // 🔥 Redirect e salvataggio specifico per ruolo
    if (data.ruolo === "PAZIENTE") {
      localStorage.setItem("idPaziente", data.idPaziente);
      navigate("/dashboard-paziente");
      return;
    }

    if (data.ruolo === "MEDICO") {
      localStorage.setItem("idMedico", data.idMedico);
      navigate("/dashboard-medico");
      return;
    }

    if (data.ruolo === "ADMIN") {
      navigate("/admin");
      return;
    }

    alert("Ruolo non riconosciuto");

  } catch (err) {
    console.error("Errore durante il login:", err);
    alert("Errore di connessione al server");
  }
};


  return (
    <>
      <Navbar />

      <div className="user-page">
        <div className="user-card">
          <img src={logo} alt="CardioCare360" className="user-logo" />
          <h2 className="user-title">Accedi al tuo account</h2>

          <form onSubmit={handleSubmit}>
            <input
              type="email"
              placeholder="Email"
              className="user-input-full"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />

            <input
              type="password"
              placeholder="Password"
              className="user-input-full"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />

            <button type="submit" className="user-button">
              Login
            </button>
          </form>

          <a href="/forgot-password" className="user-link">
            Password dimenticata?
          </a>

          <a href="/register-paziente" className="user-link">
            Non hai ancora un account? Registrati
          </a>

        </div>
      </div>

      <Footer />
    </>
  );
}
