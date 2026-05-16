import React from "react";
import Navbar from "../components/Navbar";
import Footer from "../components/Footer";
import "../styles/ContactPage.css";

function ContactPage() {
  return (
    <div className="contact-page">
      <Navbar />

      <section className="contact-hero">
        <h1>Contattaci</h1>
        <p>Siamo qui per aiutarti. Compila il form o usa i nostri recapiti.</p>
      </section>

      <section className="contact-container">

        {/* FORM */}
        <div className="contact-form">
          <h2>Scrivici un messaggio</h2>

          <form>
            <label>Nome</label>
            <input type="text" placeholder="Il tuo nome" required />

            <label>Email</label>
            <input type="email" placeholder="La tua email" required />

            <label>Messaggio</label>
            <textarea placeholder="Scrivi qui il tuo messaggio..." required />

            <button type="submit">Invia</button>
          </form>
        </div>

        {/* INFO */}
        <div className="contact-info">
          <h2>Informazioni</h2>

          <p><strong>Email:</strong> support@cardiocare360.com</p>
          <p><strong>Telefono:</strong> +39 333 123 4567</p>
          <p><strong>Indirizzo:</strong> Napoli, Italia</p>

          <div className="map-container">
            <iframe
              title="Mappa Napoli"
              src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d2995.046!2d14.2681!3d40.8518!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x133b086e8b1c3b3b%3A0x4b8b8b8b8b8b8b8b!2sNapoli%2C%20Italia!5e0!3m2!1sit!2sit!4v1681234567890!5m2!1sit!2sit"
              width="100%"
              height="250"
              style={{ border: 0, borderRadius: "8px" }}
              allowFullScreen=""
              loading="lazy"
              referrerPolicy="no-referrer-when-downgrade"
            ></iframe>
          </div>


        </div>

      </section>

      <Footer />
    </div>
  );
}

export default ContactPage;
