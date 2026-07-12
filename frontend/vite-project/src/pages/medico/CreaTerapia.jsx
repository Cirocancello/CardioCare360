import React, { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import SidebarMedico from "../../components/SidebarMedico";
import TopbarMedico from "../../components/TopbarMedico";
import "../../styles/medico/creaTerapia.css";

export default function CreaTerapia() {
  const navigate = useNavigate();

  const [pazienti, setPazienti] = useState([]);
  const [farmaci, setFarmaci] = useState([]);
  const [appuntamenti, setAppuntamenti] = useState([]);

  const [pazienteId, setPazienteId] = useState("");
  const medicoId = localStorage.getItem("idMedico");
  const [farmacoId, setFarmacoId] = useState("");
  const [appuntamentoId, setAppuntamentoId] = useState("");
  const [dosaggio, setDosaggio] = useState("");
  const [note, setNote] = useState("");
  const [dataInizio, setDataInizio] = useState("");
  const [dataFine, setDataFine] = useState("");

  const [success, setSuccess] = useState(null);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(true);

  const authHeader = () => {
    const token = localStorage.getItem("token");
    return token
      ? { headers: { Authorization: `Bearer ${token}` } }
      : { headers: {} };
  };

  useEffect(() => {
    const token = localStorage.getItem("token");

    if (!token || !medicoId) {
      setError("Sessione scaduta. Effettua di nuovo il login.");
      setLoading(false);
      return;
    }

    const fetchData = async () => {
      try {
        const [pazRes, farmRes, appRes] = await Promise.all([
          axios.get("http://localhost:8080/paziente/all", authHeader()),
          axios.get("http://localhost:8080/farmaci/all", authHeader()),
          axios.get("http://localhost:8080/appuntamenti/medico/disponibili", authHeader()),
        ]);

        setPazienti(Array.isArray(pazRes.data) ? pazRes.data : []);
        setFarmaci(Array.isArray(farmRes.data) ? farmRes.data : []);
        setAppuntamenti(Array.isArray(appRes.data) ? appRes.data : []);
        setError(null);
      } catch (err) {
        console.error("Errore Axios:", err);
        setError("Errore nel caricamento dei dati.");
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [medicoId]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    setSuccess(null);

    const token = localStorage.getItem("token");
    if (!token) {
      setError("Token non trovato, effettua di nuovo il login.");
      return;
    }

    if (!pazienteId || !farmacoId || !appuntamentoId || !dosaggio || !dataInizio) {
      setError("Compila tutti i campi obbligatori.");
      return;
    }

    try {
      const formData = new FormData();
      formData.append("pazienteId", pazienteId);
      formData.append("medicoId", medicoId);
      formData.append("farmacoId", farmacoId);
      formData.append("appuntamentoId", appuntamentoId);
      formData.append("dosaggio", dosaggio);
      formData.append("note", note);
      formData.append("dataInizio", dataInizio);
      formData.append("dataFine", dataFine);

      await axios.post("http://localhost:8080/api/terapie", formData, {
        headers: { Authorization: `Bearer ${token}` },
      });

      setSuccess("Terapia creata con successo!");
      setTimeout(() => navigate("/medico/terapie"), 1500);
    } catch (err) {
      console.error("Errore POST:", err);
      setError("Errore durante la creazione della terapia.");
    }
  };

  if (loading) return <p className="loading-message">Caricamento dati...</p>;

  return (
    <div className="layout-medico">
      <SidebarMedico />

      <div className="lista-visite-container">
        <TopbarMedico />

        <div className="crea-terapia-container">
          <h2 className="crea-terapia-title">Crea Terapia</h2>

          {success && <p className="success-message">{success}</p>}
          {error && <p className="error-message">{error}</p>}

          <form className="crea-terapia-form" onSubmit={handleSubmit}>
            
            <label>Paziente</label>
            <select
              value={pazienteId}
              onChange={(e) => setPazienteId(e.target.value)}
              required
            >
              <option value="">Seleziona paziente</option>
              {pazienti.map((p) => (
                <option key={p.id} value={p.id}>
                  {p.nomeCompleto}
                </option>
              ))}
            </select>

            <label>Farmaco</label>
            <select
              value={farmacoId}
              onChange={(e) => setFarmacoId(e.target.value)}
              required
            >
              <option value="">Seleziona farmaco</option>
              {farmaci.map((f) => (
                <option key={f.id} value={f.id}>
                  {f.nome}
                </option>
              ))}
            </select>

            <label>Appuntamento</label>
            <select
              value={appuntamentoId}
              onChange={(e) => setAppuntamentoId(e.target.value)}
              required
            >
              <option value="">Seleziona appuntamento</option>
              {appuntamenti.map((a) => (
                <option key={a.id} value={a.id}>
                  {a.dataAppuntamento} — {a.tipoVisita}
                </option>
              ))}
            </select>

            <label>Dosaggio</label>
            <input
              type="text"
              value={dosaggio}
              onChange={(e) => setDosaggio(e.target.value)}
              required
            />

            <label>Note</label>
            <textarea
              rows="4"
              value={note}
              onChange={(e) => setNote(e.target.value)}
            ></textarea>

            <label>Data Inizio</label>
            <input
              type="date"
              value={dataInizio}
              onChange={(e) => setDataInizio(e.target.value)}
              required
            />

            <label>Data Fine</label>
            <input
              type="date"
              value={dataFine}
              onChange={(e) => setDataFine(e.target.value)}
            />

            <button type="submit" className="btn-crea-terapia">
              Crea Terapia
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}
