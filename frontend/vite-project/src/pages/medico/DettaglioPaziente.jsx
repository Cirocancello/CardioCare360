import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import SidebarMedico from "../../components/SidebarMedico.jsx";
import TopbarMedico from "../../components/TopbarMedico.jsx";
import "../../styles/medico/DettaglioPaziente.css";

export default function DettaglioPaziente() {
  const { id } = useParams();
  const [paziente, setPaziente] = useState(null);
  const [visite, setVisite] = useState([]);
  const [esami, setEsami] = useState([]);
  const [terapie, setTerapie] = useState([]);
  const [parametri, setParametri] = useState([]);

  useEffect(() => {
    const token = localStorage.getItem("token");

    // 🔹 Dati anagrafici
    fetch(`http://localhost:8080/paziente/${id}`, {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then((res) => res.json())
      .then((data) => setPaziente(data))
      .catch((err) => console.error("Errore caricamento paziente:", err));

    // 🔹 Storico visite
    fetch(`http://localhost:8080/paziente/${id}/visite`, {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then((res) => res.json())
      .then((data) => setVisite(data))
      .catch((err) => console.error("Errore caricamento visite:", err));

    // 🔹 Storico esami
    fetch(`http://localhost:8080/paziente/${id}/esami`, {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then((res) => res.json())
      .then((data) => setEsami(data))
      .catch((err) => console.error("Errore caricamento esami:", err));

    // 🔹 Terapie attive
    fetch(`http://localhost:8080/paziente/${id}/terapie`, {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then((res) => res.json())
      .then((data) => setTerapie(data))
      .catch((err) => console.error("Errore caricamento terapie:", err));

    // 🔹 Parametri vitali
    fetch(`http://localhost:8080/paziente/${id}/parametri`, {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then((res) => res.json())
      .then((data) => setParametri(data))
      .catch((err) => console.error("Errore caricamento parametri:", err));
  }, [id]);

  if (!paziente) return <div>Caricamento dati paziente...</div>;

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
          <h2>Parametri Vitali Recenti</h2>
          <ul>
            {parametri.map((p) => (
              <li key={p.id}>
                {p.tipoParametro}: {p.valore} {p.unitaMisura}
              </li>
            ))}
          </ul>
        </section>

        </div>
    </div>
  );
}
