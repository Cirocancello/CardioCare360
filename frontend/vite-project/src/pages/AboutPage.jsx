import React from "react";
import Navbar from "../components/Navbar";
import Footer from "../components/Footer";
import "../styles/AboutPage.css";
import teamImg from "../assets/team.jpg";

function AboutPage() {
  return (
    <div className="about">
      <Navbar />

      <section className="about-hero">
        <h1>Chi Siamo</h1>
        <p>
          CardioCare360 nasce con una missione chiara: rendere la gestione della salute
          semplice, accessibile e sempre a portata di mano.
        </p>
      </section>

      <section className="about-content">
        <div className="about-text">
          <h2>La nostra storia</h2>
          <p>
            Siamo un team di sviluppatori, designer e professionisti sanitari che
            credono nella tecnologia come strumento per migliorare la qualità della vita.
          </p>

          <h2>I nostri valori</h2>
          <ul>
            <li>✔ Innovazione continua</li>
            <li>✔ Sicurezza dei dati</li>
            <li>✔ Accessibilità per tutti</li>
            <li>✔ Supporto umano e digitale</li>
          </ul>
        </div>

        <div className="about-image">
          <img src={teamImg} alt="Team CardioCare360" />
        </div>
      </section>

      <Footer />
    </div>
  );
}

export default AboutPage;
