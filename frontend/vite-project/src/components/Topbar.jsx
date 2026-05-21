import React from "react";
import { FaBell, FaUserCircle } from "react-icons/fa";
import "../styles/components/topbar.css";
import logo from "../assets/logo-CardioCare360.png";
import { Link } from "react-router-dom";


export default function Topbar() {
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
          <FaUserCircle className="topbar-avatar" />
          <span className="topbar-username">Ciro</span>
        </div>
      </div>

    </div>
  );
}
