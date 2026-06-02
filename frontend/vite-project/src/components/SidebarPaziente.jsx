import React from "react";
import { NavLink } from "react-router-dom";
import { 
  FaHome, 
  FaCalendarAlt, 
  FaFileMedical, 
  FaPills, 
  FaBell, 
  FaComments, 
  FaUser, 
  FaStethoscope,
  FaHeartbeat,
  FaSignOutAlt
} from "react-icons/fa";
import "../styles/components/sidebarmedico.css";

export default function Sidebar() {

  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("ruolo");
    localStorage.removeItem("idUtente");
    localStorage.removeItem("idPaziente");
    window.location.href = "/login";
  };

  return (
    <div className="sidebar">
      <h2 className="sidebar-title">Paziente</h2>

      <nav className="sidebar-menu">

        <NavLink to="/dashboard-paziente" className="sidebar-link">
          <FaHome className="sidebar-icon" />
          <span>Dashboard</span>
        </NavLink>

        <NavLink to="/paziente/appuntamenti" className="sidebar-link">
          <FaCalendarAlt className="sidebar-icon" />
          <span>Appuntamenti</span>
        </NavLink>

        <NavLink to="/paziente/prenota/visita" className="sidebar-link">
          <FaStethoscope className="sidebar-icon" />
          <span>Visite</span>
        </NavLink>

        <NavLink to="/paziente/prenota/esame" className="sidebar-link">
          <FaFileMedical className="sidebar-icon" />
          <span>Prenota Esame</span>
        </NavLink>

        <NavLink to="/paziente/esami" className="sidebar-link">
          <FaFileMedical className="sidebar-icon" />
          <span>Referti</span>
        </NavLink>

        <NavLink to="/paziente/parametri/inserisci" className="sidebar-link">
          <FaHeartbeat className="sidebar-icon" />
          <span>Parametri Vitali</span>
        </NavLink>

        <NavLink to="/paziente/storico-parametri" className="sidebar-link">
          <FaHeartbeat className="sidebar-icon" />
          <span>Storico Parametri</span>
        </NavLink>

        <NavLink to="/paziente/terapie" className="sidebar-link">
          <FaPills className="sidebar-icon" />
          <span>Terapie</span>
        </NavLink>

        <NavLink to="/paziente/notifiche" className="sidebar-link">
          <FaBell className="sidebar-icon" />
          <span>Notifiche</span>
        </NavLink>

        <NavLink to="/paziente/conversazioni" className="sidebar-link">
          <FaComments className="sidebar-icon" />
          <span>Messaggi</span>
        </NavLink>

        <NavLink to="/paziente/profilo" className="sidebar-link">
          <FaUser className="sidebar-icon" />
          <span>Profilo</span>
        </NavLink>

        {/* 🔥 LOGOUT */}
        <button className="sidebar-link logout-btn" onClick={handleLogout}>
          <FaSignOutAlt className="sidebar-icon" />
          <span>Logout</span>
        </button>

      </nav>
    </div>
  );
}
