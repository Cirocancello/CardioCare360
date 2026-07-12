import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

import SidebarMedico from "../../components/SidebarMedico";
import TopbarMedico from "../../components/TopbarMedico";
import "../../styles/medico/CambiaPasswordMedico.css";

export default function CambiaPasswordMedico() {
  const [passwordAttuale, setPasswordAttuale] = useState("");
  const [nuovaPassword, setNuovaPassword] = useState("");
  const [confermaPassword, setConfermaPassword] = useState("");

  const [errore, setErrore] = useState(null);
  const [loading, setLoading] = useState(false);

  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErrore(null);

    const token = localStorage.getItem("token");
    const idMedico = localStorage.getItem("idMedico");

    // 🔒 Validazioni base
    if (!token || !idMedico) {
      setErrore("Sessione scaduta. Effettua nuovamente il login.");
      return;
    }

    if (!passwordAttuale || !nuovaPassword || !confermaPassword) {
      setErrore("Compila tutti i campi.");
      return;
    }

    if (nuovaPassword !== confermaPassword) {
      setErrore("Le password non coincidono.");
      return;
    }

    if (nuovaPassword.length < 6) {
      setErrore("La nuova password deve contenere almeno 6 caratteri.");
      return;
    }

    try {
      setLoading(true);

      const res = await fetch(
        `http://localhost:8080/medico/${idMedico}/cambia-password`,
        {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify({
            passwordAttuale,
            nuovaPassword,
          }),
        }
      );

      if (!res.ok) {
        const msg = await res.text();
        throw new Error(msg || "Errore durante il cambio password");
      }

      navigate("/medico/profilo");
    } catch (err) {
      console.error(err);
      setErrore(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="layout-medico">
      <SidebarMedico />

      <div className="cambia-password-container">
        <TopbarMedico />

        <h1 className="cambia-password-title">Cambia Password</h1>

        {errore && <p className="error-message">{errore}</p>}
        {loading && <p className="loading-message">Aggiornamento in corso...</p>}

        <form className="cambia-password-form" onSubmit={handleSubmit}>
          <label>Password attuale</label>
          <input
            type="password"
            value={passwordAttuale}
            onChange={(e) => setPasswordAttuale(e.target.value)}
            required
          />

          <label>Nuova password</label>
          <input
            type="password"
            value={nuovaPassword}
            onChange={(e) => setNuovaPassword(e.target.value)}
            required
          />

          <label>Conferma nuova password</label>
          <input
            type="password"
            value={confermaPassword}
            onChange={(e) => setConfermaPassword(e.target.value)}
            required
          />

          <button type="submit" className="btn-salva" disabled={loading}>
            💾 Salva nuova password
          </button>
        </form>
      </div>
    </div>
  );
}
