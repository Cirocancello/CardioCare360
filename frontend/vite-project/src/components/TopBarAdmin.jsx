import React, { useEffect, useState } from "react";
import { FaBell, FaUserShield } from "react-icons/fa";
import "../styles/components/topbar.css";
import logo from "../assets/logo-CardioCare360.png";
import { Link } from "react-router-dom";

export default function TopbarAdmin() {
  const [nomeAdmin, setNomeAdmin] = useState("");

  useEffect(() => {
    const nome = localStorage.getItem("nomeCompleto"); // 🔥 chiave unificata
    if (nome) setNomeAdmin(nome);
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

        <Link to="/admin/profilo" className="topbar-user">
          <FaUserShield className="topbar-avatar" />
          <span className="topbar-username">
            {nomeAdmin ? `Admin ${nomeAdmin}` : "Admin"}
          </span>
        </Link>
      </div>
    </div>
  );
}
