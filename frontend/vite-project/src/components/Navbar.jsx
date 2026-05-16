import React from "react";
import { Link } from "react-router-dom";
import logo from "../assets/logo-CardioCare360.png";
import "../styles/HomePage.css";

function Navbar() {
  return (
    <header className="home-navbar">
      <div className="navbar-content">

        {/* LOGO → torna alla Home */}
        <Link to="/">
          <img src={logo} alt="CardioCare360 Logo" className="home-logo" />
        </Link>

        {/* LINK DI NAVIGAZIONE */}
       <nav className="navbar-links">
          <Link to="/about">Chi Siamo</Link>
          <Link to="/services">Servizi</Link>
          <Link to="/contatti">Contatti</Link>
          <Link to="/login" className="navbar-login">Accedi</Link>
       </nav>


      </div>
    </header>
  );
}

export default Navbar;
