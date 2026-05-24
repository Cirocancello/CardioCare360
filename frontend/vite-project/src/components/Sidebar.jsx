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
  FaStethoscope 
} from "react-icons/fa";
import "../styles/components/sidebar.css";

export default function Sidebar() {
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

        {/* 🔥 Prenotazione visite */}
        <NavLink to="/paziente/prenota/visita" className="sidebar-link">
          <FaStethoscope className="sidebar-icon" />
          <span>Visite</span>
        </NavLink>

        {/* 🔥 Prenotazione esami */}
        <NavLink to="/paziente/prenota/esame" className="sidebar-link">
          <FaFileMedical className="sidebar-icon" />
          <span>Prenota Esame</span>
        </NavLink>

        {/* 🔥 Referti = EsamiList (unificati) */}
        <NavLink to="/paziente/esami" className="sidebar-link">
          <FaFileMedical className="sidebar-icon" />
          <span>Referti</span>
        </NavLink>

        <NavLink to="/paziente/terapie" className="sidebar-link">
          <FaPills className="sidebar-icon" />
          <span>Terapie</span>
        </NavLink>

        <NavLink to="/paziente/notifiche" className="sidebar-link">
          <FaBell className="sidebar-icon" />
          <span>Notifiche</span>
        </NavLink>

        <NavLink to="/paziente/messaggi" className="sidebar-link">
          <FaComments className="sidebar-icon" />
          <span>Messaggi</span>
        </NavLink>

        <NavLink to="/paziente/profilo" className="sidebar-link">
          <FaUser className="sidebar-icon" />
          <span>Profilo</span>
        </NavLink>

      </nav>
    </div>
  );
}
