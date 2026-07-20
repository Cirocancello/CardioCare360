import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import SidebarMedico from "../../components/SidebarMedico.jsx";
import TopbarMedico from "../../components/TopbarMedico.jsx";
import "../../styles/medico/DettaglioPaziente.css";

export default function DettaglioPaziente() {
  const { id } = useParams(); // ID del paziente
  const [paziente, setPaziente] = useState(null);
  const [visite, setVisite] = useState([]);
  const [esami, setEsami] = useState([]);
  const [terapie, setTerapie] = useState([]);
  const [parametri, setParametri] = useState({}); // 🔥 Raggruppati per data

  useEffect(() => {
    const token = localStorage.getItem("token");

    // 🔹 Dati anagrafici
    fetch(`http://localhost:8080/paziente/${id}`, {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then((res) => res.json())
      .then(setPaziente);

    // 🔹 Storico visite
    fetch(`http://localhost:8080/paziente/${id}/visite`, {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then((res) => res.json())
      .then(setVisite);

    // 🔹 Storico esami
    fetch(`http://localhost:8080/paziente/${id}/esami`, {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then((res) => res.json())
      .then(setEsami);

    // 🔹 Terapie attive
    fetch(`http://localhost:8080/paziente/${id}/terapie`, {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then((res) => res.json())
      .then(setTerapie);

    // 🔥 PARAMETRI VITALI — STESSO ENDPOINT DEL PAZIENTE
    fetch(`http://localhost:8080/api/pazienti/${id}/parametri`, {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then((res) => res.json())
      .then((data) => {
        // Raggruppa per data come nello storico paziente
        const grouped = data.reduce((acc, p) => {
          const data = new Date(p.dataRilevazione).toLocaleString();
          if (!acc[data]) acc[data] = [];
          acc[data].push(p);
          return acc;
        }, {});
        setParametri(grouped);
      });

  }, [id]);

  if (!paziente) return <div>Caricamento dati paziente...</div>;

  // Funzione per ottenere il nome del parametro
  const getNomeParametro = (tipo) => {
    switch (tipo) {
      case "PRESSIONE_SIS": return "Pressione Sistolica";
      case "PRESSIONE_DIA": return "Pressione Diastolica";
      case "BATTITI": return "Battiti";
      case "GLICEMIA": return "Glicemia";
      case "SATURAZIONE": return "Saturazione";
      case "PESO": return "Peso";
      case "TEMPERATURA": return "Temperatura";
      default: return tipo;
    }
  };

  // Funzione per ottenere il valore corretto
  const getValoreParametro = (p) => {
    switch (p.tipo) {
      case "PRESSIONE_SIS": return p.pressioneSistolica;
      case "PRESSIONE_DIA": return p.pressioneDiastolica;
      case "BATTITI": return p.battiti;
      case "GLICEMIA": return p.glicemia;
      case "SATURAZIONE": return p.saturazione;
      case "PESO": return p.peso;
      case "TEMPERATURA": return p.temperatura;
      default: return "";
    }
  };

  return (
    <div className="layout-medico">
      <SidebarMedico />
      <div className="dettaglio-paziente-container">
        <TopbarMedico />
        <h1 className="title">Dettaglio Paziente</h1>

        {/* 🔹 Dati anagrafici */}
        <section className="section">
          <h2>Dati Anagrafici</h2>
          <p><strong>Nome:</strong> {paziente.nomeCompleto}</p>
          <p><strong>Email:</strong> {paziente.email}</p>
          <p><strong>Codice Fiscale:</strong> {paziente.codiceFiscale}</p>
          <p><strong>Telefono:</strong> {paziente.telefono}</p>
          <p><strong>Luogo di nascita:</strong> {paziente.luogoNascita}</p>
          <p><strong>Data di nascita:</strong> {paziente.dataNascita}</p>
          <p><strong>Indirizzo:</strong> {paziente.indirizzo}</p>
        </section>

        {/* 🔹 Storico visite */}
        <section className="section">
          <h2>Storico Visite</h2>
          <table>
            <thead>
              <tr>
                <th>Tipo</th>
                <th>Data</th>
                <th>Ora</th>
                <th>Stato</th>
              </tr>
            </thead>
            <tbody>
              {visite.map((v) => (
                <tr key={v.id}>
                  <td>{v.tipoVisita}</td>
                  <td>{v.dataAppuntamento}</td>
                  <td>{v.oraAppuntamento}</td>
                  <td>{v.stato}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </section>

        {/* 🔹 Storico esami */}
        <section className="section">
          <h2>Storico Esami</h2>
          <table>
            <thead>
              <tr>
                <th>Tipo</th>
                <th>Data</th>
                <th>Ora</th>
                <th>Stato</th>
              </tr>
            </thead>
            <tbody>
              {esami.map((e) => (
                <tr key={e.id}>
                  <td>{e.tipoEsame}</td>
                  <td>{e.dataEsame}</td>
                  <td>{e.oraEsame}</td>
                  <td>{e.stato}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </section>

        {/* 🔹 Terapie attive */}
        <section className="section">
          <h2>Terapie Attive</h2>
          <ul>
            {terapie.map((t) => (
              <li key={t.id}>
                {t.nomeTerapia} — {t.dosaggio}
              </li>
            ))}
          </ul>
        </section>

        {/* 🔹 Parametri vitali */}
        <section className="section">
          <h2>Parametri Vitali</h2>

          {Object.keys(parametri).length === 0 ? (
            <p>Nessun parametro registrato.</p>
          ) : (
            Object.entries(parametri).map(([data, lista]) => (
              <div key={data} className="parametro-card">
                <h4>{data}</h4>

                {lista.map((p) => (
                  <div key={p.id} className="parametro-info">
                    <p><strong>{getNomeParametro(p.tipo)}:</strong></p>
                    <p>{getValoreParametro(p)} {p.unitaMisura || ""}</p>

                    {p.alert && (
                      <p style={{ color: "red", fontWeight: "bold" }}>
                        ⚠️ {p.alert}
                      </p>
                    )}
                  </div>
                ))}
              </div>
            ))
          )}
        </section>

      </div>
    </div>
  );
}
