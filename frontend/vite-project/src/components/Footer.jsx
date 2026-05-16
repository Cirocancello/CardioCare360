import React from "react";
import { Link } from "react-router-dom";
import "../styles/HomePage.css";
import { FaFacebookF, FaInstagram, FaLinkedinIn } from "react-icons/fa";

function Footer() {
  return (
    <footer className="home-footer">
      <div className="footer-content">
        <div className="footer-left">
          <h3>CardioCare360</h3>
          <p>Piattaforma digitale per la gestione della salute.</p>
        </div>

        <div className="footer-links">
          <Link to="/services">Servizi</Link>
          <Link to="/contatti">Contatti</Link>
          <Link to="/login">Accedi</Link>
        </div>
      </div>

      {/* 🔗 Sezione Social */}
      <div className="footer-social">
        <a href="https://facebook.com" target="_blank" rel="noopener noreferrer">
          <FaFacebookF />
        </a>
        <a href="https://instagram.com" target="_blank" rel="noopener noreferrer">
          <FaInstagram />
        </a>
        <a href="https://linkedin.com" target="_blank" rel="noopener noreferrer">
          <FaLinkedinIn />
        </a>
      </div>

      <div className="footer-bottom">
        <p>© {new Date().getFullYear()} CardioCare360 — Tutti i diritti riservati.</p>
      </div>
    </footer>
  );
}

export default Footer;
