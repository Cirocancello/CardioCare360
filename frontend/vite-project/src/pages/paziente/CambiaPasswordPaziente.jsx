import { useState } from "react";
import "../../styles/paziente/profilo.css";

export default function CambiaPasswordPaziente() {
  const [form, setForm] = useState({
    vecchiaPassword: "",
    nuovaPassword: "",
    confermaPassword: ""
  });

  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async () => {
    if (!form.vecchiaPassword || !form.nuovaPassword || !form.confermaPassword) {
      alert("Compila tutti i campi.");
      return;
    }

    if (form.nuovaPassword !== form.confermaPassword) {
      alert("La nuova password non coincide con la conferma.");
      return;
    }

    const idPaziente = localStorage.getItem("idPaziente");
    const token = localStorage.getItem("token");

    setLoading(true);

    try {
      const res = await fetch(`http://localhost:8080/paziente/${idPaziente}/cambia-password`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`
        },
        body: JSON.stringify({
          passwordAttuale: form.vecchiaPassword,
          nuovaPassword: form.nuovaPassword
        })
      });

      // Il backend restituisce solo testo, non JSON
      const responseText = await res.text();

      if (res.ok) {
        alert("Password aggiornata con successo!");
        setForm({
          vecchiaPassword: "",
          nuovaPassword: "",
          confermaPassword: ""
        });
      } else {
        alert(responseText || "Errore durante l'aggiornamento della password.");
      }
    } catch (err) {
      console.error("Errore:", err);
      alert("Errore di connessione al server.");
    }

    setLoading(false);
  };

  return (
    <div className="profilo-page">
      <div className="profilo-container">
        <h1 className="profilo-title">Cambia Password</h1>

        <div className="profilo-card">
          <label>Vecchia Password</label>
          <input
            type="password"
            name="vecchiaPassword"
            value={form.vecchiaPassword}
            onChange={handleChange}
          />

          <label>Nuova Password</label>
          <input
            type="password"
            name="nuovaPassword"
            value={form.nuovaPassword}
            onChange={handleChange}
          />

          <label>Conferma Nuova Password</label>
          <input
            type="password"
            name="confermaPassword"
            value={form.confermaPassword}
            onChange={handleChange}
          />
        </div>

        <div className="profilo-actions">
          <button className="btn-secondary" onClick={() => window.history.back()}>
            Annulla
          </button>

          <button className="btn-primary" onClick={handleSubmit} disabled={loading}>
            {loading ? "Salvataggio..." : "Aggiorna Password"}
          </button>
        </div>
      </div>
    </div>
  );
}
