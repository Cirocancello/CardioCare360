import { useEffect, useState } from "react";
import "../../styles/paziente/profilo.css";

export default function ProfiloPaziente() {
  const [paziente, setPaziente] = useState(null);

  useEffect(() => {
    const idPaziente = localStorage.getItem("idPaziente");
    const token = localStorage.getItem("token");

    fetch(`http://localhost:8080/paziente/${idPaziente}`, {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then(res => res.json())
      .then(data => setPaziente(data))
      .catch(err => console.error("Errore caricamento profilo:", err));
  }, []);

  if (!paziente) return <p>Caricamento profilo...</p>;

  return (
    <div className="profilo-container">
      <h1 className="profilo-title">Profilo Paziente</h1>

      <div className="profilo-card">
        <p><strong>Nome:</strong> {paziente.nomeCompleto}</p>
        <p><strong>Email:</strong> {paziente.email}</p>
        <p><strong>Codice Fiscale:</strong> {paziente.codiceFiscale}</p>
        <p><strong>Telefono:</strong> {paziente.telefono}</p>
      </div>
    </div>
  );
}
