import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { forgotPassword } from "../../api/auth";
import "../../styles/public/user.css";
import logo from "../../assets/logo-CardioCare360.png";

function ForgotPassword() {
  const navigate = useNavigate();

  const [email, setEmail] = useState("");
  const [sent, setSent] = useState(false);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handleForgot = async (e) => {
    e.preventDefault();
    setError("");
    setLoading(true);

    if (!email || !email.includes("@")) {
      setError("Inserisci un'email valida.");
      setLoading(false);
      return;
    }

    try {
      const message = await forgotPassword(email);

      console.log("Risposta backend:", message);
      setSent(true);

      setTimeout(() => navigate("/login"), 2000);

    } catch (err) {
      setError(
        err.message === "404"
          ? "Email non trovata."
          : err.message || "Errore: impossibile inviare l'email."
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="user-page">
      <div className="user-card">

        <img src={logo} alt="CardioCare360" className="user-logo" />

        <h2 className="user-title">Recupera la password</h2>

        {loading && <p className="loading-message">Invio in corso...</p>}
        {error && <p className="error-message">{error}</p>}

        {!sent ? (
          <form onSubmit={handleForgot}>

            <input
              type="email"
              placeholder="Inserisci la tua email"
              className="user-input-full"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />

            <button type="submit" className="user-button" disabled={loading}>
              Invia email di recupero
            </button>

          </form>
        ) : (
          <p className="success-message">
            Email inviata! Controlla la tua casella di posta.
          </p>
        )}

        <a href="/login" className="user-link">Torna al login</a>

      </div>
    </div>
  );
}

export default ForgotPassword;
