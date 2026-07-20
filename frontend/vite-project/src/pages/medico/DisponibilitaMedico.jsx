import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import SidebarMedico from "../../components/SidebarMedico";
import TopbarMedico from "../../components/TopbarMedico";
import "../../styles/medico/DisponibilitaMedico.css";

export default function DisponibilitaMedico() {
  const [disponibilita, setDisponibilita] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  const token = localStorage.getItem("token");

  useEffect(() => {
    if (!token) {
      alert("Sessione scaduta. Effettua nuovamente il login.");
      navigate("/login-medico");
      return;
    }
    fetchDisponibilita();
  }, []);

  const fetchDisponibilita = async () => {
    try {
      setLoading(true);

      const res = await fetch(`http://localhost:8080/disponibilita/medico`, {
        headers: { Authorization: `Bearer ${token}` },
      });

      if (res.status === 401) {
        alert("Sessione non valida. Effettua di nuovo il login.");
        navigate("/login-medico");
        return;
      }

      if (!res.ok) throw new Error("Errore nel caricamento delle disponibilità");

      const data = await res.json();
      setDisponibilita(data);
    } catch (err) {
      console.error(err);
      alert("Errore: " + err.message);
    } finally {
      setLoading(false);
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

      alert("Disponibilità eliminata con successo.");
      fetchDisponibilita();
    } catch (err) {
      console.error(err);
      alert("Errore: " + err.message);
    }
  };

  // ⭐ Converte LUN → Lunedì
  const formatGiorno = (g) => {
    const map = {
      LUN: "Lunedì",
      MAR: "Martedì",
      MER: "Mercoledì",
      GIO: "Giovedì",
      VEN: "Venerdì",
      SAB: "Sabato",
      DOM: "Domenica",
    };
    return map[g] || g;
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

        {loading ? (
          <p className="loading">Caricamento disponibilità...</p>
        ) : (
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
                    <td>{formatGiorno(d.giornoSettimana)}</td>

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
        )}
      </div>
    </div>
  );
}
