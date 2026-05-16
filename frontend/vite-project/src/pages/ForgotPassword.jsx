import { useState } from "react";
import { forgotPassword } from "../api/auth";
import { useNavigate } from "react-router-dom";
import "../styles/user.css";
import logo from "../assets/logo-CardioCare360.png";

function ForgotPassword() {
  const navigate = useNavigate();

  const [email, setEmail] = useState("");
  const [sent, setSent] = useState(false);
  const [error, setError] = useState("");

  const handleForgot = async (e) => {
  e.preventDefault();
  setError("");

  try {
    const message = await forgotPassword(email);

    // ✅ Se arriva qui, la chiamata è andata a buon fine
    console.log("Risposta backend:", message);
    setSent(true);

    // Redirect dopo 2 secondi
    setTimeout(() => navigate("/login"), 2000);

  } catch (err) {
    // 🔥 Mostra il messaggio esatto se disponibile
    setError(err.message || "Errore: impossibile inviare l'email.");
  }
};


  return (
    <div className="user-page">
      <div className="user-card">

        <img src={logo} alt="CardioCare360" className="user-logo" />

        <h2 className="user-title">Recupera la password</h2>

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

            <button type="submit" className="user-button">
              Invia email di recupero
            </button>

          </form>
        ) : (
          <p style={{ color: "green", marginTop: "10px" }}>
            Email inviata! Controlla la tua casella di posta.
          </p>
        )}

        {error && (
          <p style={{ color: "red", marginTop: "10px" }}>{error}</p>
        )}

        <a href="/login" className="user-link">Torna al login</a>

      </div>
    </div>
  );
}

export default ForgotPassword;
