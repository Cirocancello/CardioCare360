import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import SidebarMedico from "../../components/SidebarMedico.jsx";
import TopbarMedico from "../../components/TopbarMedico.jsx";
import "../../styles/medico/ListaPazienti.css";

export default function ListaPazienti() {
  const [pazienti, setPazienti] = useState([]);
  const [search, setSearch] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem("token");

    fetch("http://localhost:8080/paziente/all", {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then((res) => res.json())
      .then((data) => setPazienti(data))
      .catch((err) => console.error("Errore caricamento pazienti:", err));
  }, []);

  const pazientiFiltrati = pazienti.filter((p) =>
    p.nomeCompleto.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <div className="layout-medico">
      <SidebarMedico />

      <div className="lista-visite-container">
        <TopbarMedico />

        <h1 className="title">Lista Pazienti</h1>

        <input
          type="text"
          placeholder="Cerca paziente..."
          className="search-input"
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />

        <table className="tabella-visite">
          <thead>
            <tr>
              <th>Nome</th>
              <th>Codice Fiscale</th>
              <th>Email</th>
              <th>Telefono</th>
              <th>Azioni</th>
            </tr>
          </thead>

          <tbody>
            {pazientiFiltrati.map((p) => (
              <tr key={p.id}>
                <td>{p.nomeCompleto}</td>
                <td>{p.codiceFiscale}</td>
                <td>{p.email}</td>
                <td>{p.telefono}</td>
                <td>
                  <button
                    className="btn-dettagli"
                    onClick={() => navigate(`/medico/pazienti/${p.id}`)}
                  >
                    Dettaglio
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>

      </div>
    </div>
  );
}
