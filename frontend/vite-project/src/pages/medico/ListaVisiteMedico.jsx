import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import SidebarMedico from "../../components/SidebarMedico";
import TopbarMedico from "../../components/TopbarMedico";
import "../../styles/medico/ListaVisiteMedico.css";

export default function ListaVisiteMedico() {
  const [visite, setVisite] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem("token");

    fetch("http://localhost:8080/appuntamenti/medico", {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then((res) => res.json())
      .then((data) => setVisite(data))
      .catch((err) => console.error("Errore caricamento visite:", err));
  }, []);

  return (
    <div className="layout-medico">
      <SidebarMedico />
      <div className="lista-visite-container">
        <TopbarMedico />
        <h1 className="title">Gestione Visite</h1>

        <table className="tabella-visite">
          <thead>
            <tr>
              <th>Paziente</th>
              <th>Tipo Visita</th>
              <th>Data</th>
              <th>Ora</th>
              <th>Stato</th>
              <th>Azioni</th>
            </tr>
          </thead>
          <tbody>
            {visite.map((v) => (
                <tr key={v.id}>
                <td>{v.nomePaziente} {v.cognomePaziente}</td>
                <td>{v.tipoVisita}</td>
                <td>{v.dataAppuntamento}</td>
                <td>{v.oraAppuntamento}</td>
                <td>{v.stato}</td>
                <td>
                    <button
                    className="btn-dettagli"
                    onClick={() => navigate(`/medico/visite/${v.id}`)}
                    >
                    Dettagli
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
