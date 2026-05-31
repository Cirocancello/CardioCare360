import React from "react";
import { Link } from "react-router-dom";
import "../styles/components/footer.css"; 
import { FaFacebookF, FaInstagram, FaLinkedinIn } from "react-icons/fa";

// Import corretto del logo
import logo from "../assets/logo-CardioCare360.png";

function Footer() {
  return (
    <footer className="home-footer">
      <div className="footer-content">

        {/* 🔵 Logo + sottotitolo */}
        <div className="footer-left">
          <img 
            src={logo} 
            alt="CardioCare360" 
            className="footer-logo"
          />

          <p className="footer-subtitle">
            Piattaforma digitale per la gestione della salute.
          </p>
        </div>

        {/* 🔗 Link */}
        <div className="footer-links">
          <Link to="/services">Servizi</Link>
          <Link to="/contatti">Contatti</Link>
          <Link to="/login">Accedi</Link>
        </div>
      </div>

      {/* 🌐 Social */}
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

      {/* © Copyright */}
      <div className="footer-bottom">
        <p>
          © {new Date().getFullYear()} CardioCare360 — Tutti i diritti riservati.
        </p>
      </div>
    </footer>
  );
}

export default Footer;
