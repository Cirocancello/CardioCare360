import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import SidebarAdmin from "../../components/SidebarAdmin";
import TopbarAdmin from "../../components/TopbarAdmin";
import "../../styles/admin/CambiaPasswordAdmin.css";

export default function CambiaPasswordAdmin() {
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
    const idUtente = localStorage.getItem("idUtente");

    try {
      const res = await fetch(
        `http://localhost:8080/admin/${idUtente}/cambia-password`,
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

      alert("Password aggiornata con successo");
      navigate("/admin/profilo"); // 🔥 redirect corretto

    } catch (err) {
      console.error(err);
      alert("Errore: " + err.message);
    }
  };

  return (
    <div className="layout-admin">
      <SidebarAdmin />

      <div className="cambia-password-container">
        <TopbarAdmin />

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
            Salva nuova password
          </button>
        </form>
      </div>
    </div>
  );
}
