import { useEffect, useState } from "react";
import "../../styles/paziente/profilo.css";

export default function ProfiloPaziente() {
  const [paziente, setPaziente] = useState(null);
  const [loading, setLoading] = useState(true);
  const [errore, setErrore] = useState(null);

  useEffect(() => {
    const idPaziente = localStorage.getItem("idPaziente");
    const token = localStorage.getItem("token");

    // 🔒 Blindatura: token o id mancanti
    if (!idPaziente || !token) {
      setErrore("Sessione scaduta. Effettua nuovamente il login.");
      setLoading(false);
      return;
    }

    const fetchProfilo = async () => {
      try {
        const res = await fetch(`http://localhost:8080/paziente/${idPaziente}`, {
          headers: { Authorization: `Bearer ${token}` },
        });

        if (!res.ok) {
          throw new Error("Errore nel caricamento del profilo");
        }

        const data = await res.json();
        setPaziente(data);
      } catch (err) {
        console.error("Errore caricamento profilo:", err);
        setErrore("Errore nel caricamento del profilo.");
      } finally {
        setLoading(false);
      }
    };

    fetchProfilo();
  }, []);

  // ---------------------------------------------------------
  // RENDER
  // ---------------------------------------------------------

  if (loading) return <p className="loading-message">Caricamento profilo...</p>;

  if (errore) return <p className="error-message">{errore}</p>;

  if (!paziente) return <p className="error-message">Profilo non trovato.</p>;

  return (
    <div className="profilo-container">
      <h1 className="profilo-title">Profilo Paziente</h1>

      <div className="profilo-card">
        <p><strong>Nome:</strong> {paziente.nomeCompleto || "—"}</p>
        <p><strong>Email:</strong> {paziente.email || "—"}</p>
        <p><strong>Codice Fiscale:</strong> {paziente.codiceFiscale || "—"}</p>
        <p><strong>Telefono:</strong> {paziente.telefono || "—"}</p>
      </div>
    </div>
  );
}
