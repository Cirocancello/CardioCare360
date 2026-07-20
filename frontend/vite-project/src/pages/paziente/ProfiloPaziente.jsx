import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom"; // ✅ Import necessario
import "../../styles/paziente/profilo.css";

export default function ProfiloPaziente() {
  const [paziente, setPaziente] = useState(null);
  const [form, setForm] = useState({});
  const [editing, setEditing] = useState(false);
  const navigate = useNavigate(); // ✅ Inizializzazione

  useEffect(() => {
    const idPaziente = localStorage.getItem("idPaziente");
    const token = localStorage.getItem("token");

    fetch(`http://localhost:8080/paziente/${idPaziente}`, {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then(res => res.json())
      .then(data => {
        const nomeCompleto = `${data.nome} ${data.cognome}`;
        const profiloCompleto = { ...data, nomeCompleto };
        setPaziente(profiloCompleto);
        setForm(profiloCompleto);
      })
      .catch(err => console.error("Errore caricamento profilo:", err));
  }, []);

  const handleChange = e => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const salvaModifiche = async () => {
    const idPaziente = localStorage.getItem("idPaziente");
    const token = localStorage.getItem("token");

    try {
      const res = await fetch(`http://localhost:8080/paziente/${idPaziente}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(form),
      });

      const data = await res.json();

      if (res.ok) {
        const nomeCompleto = `${data.nome} ${data.cognome}`;
        const profiloCompleto = { ...data, nomeCompleto };
        setPaziente(profiloCompleto);
        setForm(profiloCompleto);
        setEditing(false);
        alert("Profilo aggiornato con successo!");
      } else {
        alert(`Errore: ${data.message || "Impossibile salvare le modifiche"}`);
      }
    } catch (err) {
      console.error("Errore nel salvataggio:", err);
      alert("Errore di connessione al server");
    }
  };

  if (!paziente) return <p>Caricamento profilo...</p>;

  return (
    <div className="profilo-page">
      <div className="profilo-container">
        <h1 className="profilo-title">Profilo Paziente</h1>

        <div className="profilo-card">
          <label>Nome</label>
            <input
              name="nome"
              value={form.nome || ""}
              onChange={handleChange}
              disabled={!editing}
            />

            <label>Cognome</label>
            <input
              name="cognome"
              value={form.cognome || ""}
              onChange={handleChange}
              disabled={!editing}
            />


          <label>Email</label>
          <input
            name="email"
            value={form.email || ""}
            onChange={handleChange}
            disabled={!editing}
          />

          <label>Codice Fiscale</label>
          <input
            name="codiceFiscale"
            value={form.codiceFiscale || ""}
            onChange={handleChange}
            disabled={!editing}
          />

          <label>Telefono</label>
          <input
            name="telefono"
            value={form.telefono || ""}
            onChange={handleChange}
            disabled={!editing}
          />
        </div>

        {!editing ? (
          <div className="profilo-actions">
            <button className="btn-primary" onClick={() => setEditing(true)}>
              Modifica Profilo
            </button>
            <button
              className="btn-primary"
              onClick={() => navigate("/paziente/cambia-password")}
            >
              Cambia Password
            </button>
          </div>
        ) : (
          <div className="profilo-actions">
            <button className="btn-secondary" onClick={() => setEditing(false)}>
              Annulla
            </button>
            <button className="btn-primary" onClick={salvaModifiche}>
              Salva Modifiche
            </button>
          </div>
        )}
      </div>

      <div className="back-button-container">
        <button onClick={() => window.history.back()} className="back-button">
          Torna indietro
        </button>
      </div>
    </div>
  );
}
