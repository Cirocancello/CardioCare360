import { useEffect, useState } from "react";
import { getPazienteById } from "../api/paziente";

function Dashboard() {
  const [paziente, setPaziente] = useState(null);

  useEffect(() => {
    const id = localStorage.getItem("idPaziente"); // ID dinamico dal login

    const fetchData = async () => {
      try {
        const data = await getPazienteById(id);
        setPaziente(data);
      } catch (err) {
        console.error("Errore nel caricamento dati paziente:", err);
      }
    };

    fetchData();
  }, []);

  if (!paziente) return <p>Caricamento dati...</p>;

  return (
    <div style={{ maxWidth: "600px", margin: "50px auto" }}>
      <h2>Benvenuto, {paziente.nome} {paziente.cognome}</h2>

      <p>Email: {paziente.email}</p>
      <p>Codice Fiscale: {paziente.codiceFiscale}</p>
      <p>Telefono: {paziente.telefono}</p>

      {/* 🔥 Logout */}
      <button
        onClick={() => {
          localStorage.removeItem("token");
          localStorage.removeItem("ruolo");
          localStorage.removeItem("idUtente");
          localStorage.removeItem("idPaziente");
          window.location.href = "/login";
        }}
        style={{
          marginTop: "20px",
          padding: "10px 20px",
          backgroundColor: "#d9534f",
          color: "white",
          border: "none",
          borderRadius: "5px",
          cursor: "pointer"
        }}
      >
        Logout
      </button>
    </div>
  );
}

export default Dashboard;
