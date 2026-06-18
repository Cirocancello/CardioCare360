import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import SidebarMedico from "../../components/SidebarMedico";
import TopbarMedico from "../../components/TopbarMedico";
import "../../styles/medico/CambiaPasswordMedico.css";

export default function CambiaPasswordMedico() {
  const [passwordAttuale, setPasswordAttuale] = useState("");
  const [nuovaPassword, setNuovaPassword] = useState("");
  const [confermaPassword, setConfermaPassword] = useState("");
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (nuovaPassword !== confermaPassword) {
      alert("Le password non coincidono");
      return;
    }

    const token = localStorage.getItem("token");
    const idMedico = localStorage.getItem("idMedico");

    try {
      const res = await fetch(`http://localhost:8080/medico/${idMedico}/cambia-password`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          passwordAttuale,
          nuovaPassword,
        }),
      });

      if (!res.ok) {
        const msg = await res.text();
        throw new Error(msg || "Errore durante il cambio password");
      }

      alert("Password aggiornata con successo");
      navigate("/medico/profilo"); // 🔥 redirect corretto

    } catch (err) {
      console.error(err);
      alert("Errore: " + err.message);
    }
  };

  return (
    <div className="layout-medico">
      <SidebarMedico />

      <div className="cambia-password-container">
        <TopbarMedico />

        <h1 className="cambia-password-title">Cambia Password</h1>

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

          <button type="submit" className="btn-salva">
            💾 Salva nuova password
          </button>
        </form>
      </div>
    </div>
  );
}
