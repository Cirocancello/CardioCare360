import React, { useEffect, useState } from "react";
import { FaBell, FaUserMd } from "react-icons/fa";
import "../styles/components/topbar.css";
import logo from "../assets/logo-CardioCare360.png";
import { Link } from "react-router-dom";

export default function TopbarMedico() {
  const [nomeMedico, setNomeMedico] = useState("");

  useEffect(() => {
    const nome = localStorage.getItem("nomeCompleto");
    if (nome) setNomeMedico(nome);
  }, []);

  return (
    <div className="topbar">
      {/* Logo a sinistra */}
      <div className="topbar-brand">
        <Link to="/" className="topbar-logo">
          <img src={logo} alt="CardioCare360" className="topbar-logo" />
        </Link>
      </div>

      {/* Sezione destra */}
      <div className="topbar-right">
        <div className="topbar-icon">
          <FaBell />
        </div>

        <div className="topbar-user">
          <FaUserMd className="topbar-avatar" />
          <span className="topbar-username">
            {nomeMedico ? `Dott. ${nomeMedico}` : "Medico"}
          </span>
        </div>
      </div>
    </div>
  );
}
