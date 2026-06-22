import React from "react";
import { Link, useNavigate } from "react-router-dom";
import "../styles/components/SidebarAdmin.css";

export default function SidebarAdmin() {
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("ruolo");
    navigate("/login");
  };

  return (
    <div className="sidebar-admin">
      <h2 className="sidebar-title">Admin</h2>

      <nav className="sidebar-menu">
        <Link to="/admin" className="sidebar-item">
          Dashboard
        </Link>

        <Link to="/admin/medici" className="sidebar-item">
          Gestione Medici
        </Link>

        <Link to="/admin/pazienti" className="sidebar-item">
          Gestione Pazienti
        </Link>
      </nav>

      <button className="sidebar-logout" onClick={handleLogout}>
        Logout
      </button>
    </div>
  );
}
