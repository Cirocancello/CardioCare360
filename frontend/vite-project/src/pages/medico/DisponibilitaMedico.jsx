import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import SidebarMedico from "../../components/SidebarMedico";
import TopbarMedico from "../../components/TopbarMedico";
import "../../styles/medico/DisponibilitaMedico.css";

export default function DisponibilitaMedico() {
  const [disponibilita, setDisponibilita] = useState([]);
  const navigate = useNavigate();

  const idMedico = localStorage.getItem("idMedico");
  const token = localStorage.getItem("token");

  useEffect(() => {
    fetchDisponibilita();
  }, []);

  const fetchDisponibilita = async () => {
    try {
      const res = await fetch(`http://localhost:8080/disponibilita/medico`, {
        headers: { Authorization: `Bearer ${token}` },
      });


      if (!res.ok) throw new Error("Errore nel caricamento delle disponibilità");

      const data = await res.json();
      setDisponibilita(data);
    } catch (err) {
      console.error(err);
      alert("Errore: " + err.message);
    }
  };

  const eliminaDisponibilita = async (id) => {
    if (!window.confirm("Sei sicuro di voler eliminare questa disponibilità?")) return;

    try {
      const res = await fetch(`http://localhost:8080/disponibilita/${id}`, {
        method: "DELETE",
        headers: { Authorization: `Bearer ${token}` },
      });

      if (!res.ok) throw new Error("Errore durante l'eliminazione");

      alert("Disponibilità eliminata");
      fetchDisponibilita();
    } catch (err) {
      console.error(err);
      alert("Errore: " + err.message);
    }
  };

  return (
    <div className="layout-medico">
      <SidebarMedico />

      <div className="disponibilita-container">
        <TopbarMedico />

        <h1 className="title">Le mie disponibilità</h1>

        <button
          className="btn-aggiungi"
          onClick={() => navigate("/medico/disponibilita/aggiungi")}
        >
          ➕ Aggiungi disponibilità
        </button>

        <table className="tabella-disponibilita">
          <thead>
            <tr>
              <th>Giorno</th>
              <th>Ora inizio</th>
              <th>Ora fine</th>
              <th>Azioni</th>
            </tr>
          </thead>

          <tbody>
            {disponibilita.length === 0 ? (
              <tr>
                <td colSpan="4" className="vuoto">
                  Nessuna disponibilità presente
                </td>
              </tr>
            ) : (
              disponibilita.map((d) => (
                <tr key={d.id}>
                  <td>{d.giorno}</td>
                  <td>{d.oraInizio}</td>
                  <td>{d.oraFine}</td>
                  <td>
                    <button
                      className="btn-elimina"
                      onClick={() => eliminaDisponibilita(d.id)}
                    >
                      🗑 Elimina
                    </button>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
