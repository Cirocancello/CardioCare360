import React from "react";
import Navbar from "../../components/Navbar";
import Footer from "../../components/Footer";
import "../../styles/public/ServicesPage.css";

function ServicesPage() {
  return (
    <div className="services">
      <Navbar />

      <section className="services-hero">
        <h1>I nostri servizi</h1>
        <p>
          Tutti gli strumenti di cui hai bisogno per gestire la tua salute in modo semplice e sicuro.
        </p>
      </section>

      <section className="services-grid">
        <div className="service-card">
          <h3>Gestione Appuntamenti</h3>
          <p>Prenota, modifica e gestisci le visite con un’interfaccia intuitiva.</p>
        </div>

        <div className="service-card">
          <h3>Referti Digitali</h3>
          <p>Accedi ai tuoi referti in qualsiasi momento, in totale sicurezza.</p>
        </div>

        <div className="service-card">
          <h3>Terapie e Promemoria</h3>
          <p>Ricevi notifiche e monitora le tue terapie quotidiane.</p>
        </div>

        <div className="service-card">
          <h3>Chat Medico‑Paziente</h3>
          <p>Comunica rapidamente con il tuo medico in modo protetto.</p>
        </div>
      </section>

      <Footer />
    </div>
  );
}

export default ServicesPage;
