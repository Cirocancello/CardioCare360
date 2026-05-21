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

    const data = await login(email, password);

    if (!data) {
      alert("Credenziali non valide");
      return;
    }

    if (!data.token) {
      alert("Errore: il server non ha restituito un token");
      return;
    }

    // Salva token e idUtente
    localStorage.setItem("token", data.token);
    localStorage.setItem("idUtente", data.idUtente);

    // Redirect in base al ruolo
    if (data.ruolo === "PAZIENTE") {
      navigate("/dashboard-paziente");
    } else if (data.ruolo === "MEDICO") {
      navigate("/dashboard-medico");
    } else {
      navigate("/admin");
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

        </div>
      </div>

      <Footer />
    </>
  );
}
