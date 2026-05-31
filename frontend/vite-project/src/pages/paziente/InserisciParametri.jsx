import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import { toast } from "react-toastify";
import "../../styles/paziente/inserisciParametri.css";

const InserisciParametri = () => {
  const navigate = useNavigate();

  const [form, setForm] = useState({
    pressioneSistolica: "",
    pressioneDiastolica: "",
    battiti: "",
    glicemia: "",
    saturazione: "",
    peso: "",
    temperatura: ""
  });

  const handleChange = (campo, valore) => {
    setForm({ ...form, [campo]: valore });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const token = localStorage.getItem("token");
      const idPaziente = localStorage.getItem("idPaziente");

      if (!token || !idPaziente) {
        toast.error("Sessione scaduta. Effettua di nuovo il login.");
        return;
      }

      // 🔥 Conversione corretta dei valori
      const payload = {
        pressioneSistolica: form.pressioneSistolica ? Number(form.pressioneSistolica) : null,
        pressioneDiastolica: form.pressioneDiastolica ? Number(form.pressioneDiastolica) : null,
        battiti: form.battiti ? Number(form.battiti) : null,
        glicemia: form.glicemia ? Number(form.glicemia) : null,
        saturazione: form.saturazione ? Number(form.saturazione) : null,
        peso: form.peso ? Number(form.peso) : null,
        temperatura: form.temperatura ? Number(form.temperatura) : null,
        dataRilevazione: new Date().toISOString()
      };

      await axios.post(
        `http://localhost:8080/api/pazienti/${idPaziente}/parametri`,
        payload,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      toast.success("Parametri salvati con successo");
      navigate("/paziente/storico-parametri");

    } catch (err) {
      toast.error("Errore durante il salvataggio dei parametri");
      console.error(err);
    }
  };

  return (
    <div className="parametri-page">
      <div className="parametri-container">

        <h2 className="parametri-title">Inserisci Parametri Vitali</h2>

        <form onSubmit={handleSubmit} className="parametri-form">

          <label className="parametri-label">Pressione Sistolica (mmHg)</label>
          <input
            type="number"
            className="parametri-input"
            value={form.pressioneSistolica}
            onChange={(e) => handleChange("pressioneSistolica", e.target.value)}
            required
          />

          <label className="parametri-label">Pressione Diastolica (mmHg)</label>
          <input
            type="number"
            className="parametri-input"
            value={form.pressioneDiastolica}
            onChange={(e) => handleChange("pressioneDiastolica", e.target.value)}
            required
          />

          <label className="parametri-label">Battiti (bpm)</label>
          <input
            type="number"
            className="parametri-input"
            value={form.battiti}
            onChange={(e) => handleChange("battiti", e.target.value)}
            required
          />

          <label className="parametri-label">Glicemia (mg/dL)</label>
          <input
            type="number"
            className="parametri-input"
            value={form.glicemia}
            onChange={(e) => handleChange("glicemia", e.target.value)}
            required
          />

          <label className="parametri-label">Saturazione (%)</label>
          <input
            type="number"
            className="parametri-input"
            value={form.saturazione}
            onChange={(e) => handleChange("saturazione", e.target.value)}
            required
          />

          <label className="parametri-label">Peso (kg)</label>
          <input
            type="number"
            className="parametri-input"
            value={form.peso}
            onChange={(e) => handleChange("peso", e.target.value)}
            required
          />

          <label className="parametri-label">Temperatura (°C)</label>
          <input
            type="number"
            className="parametri-input"
            value={form.temperatura}
            onChange={(e) => handleChange("temperatura", e.target.value)}
            required
          />

          <button type="submit" className="parametri-button">
            Salva Parametri
          </button>

        </form>
      </div>
    </div>
  );
};

export default InserisciParametri;
