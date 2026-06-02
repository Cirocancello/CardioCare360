import React from "react";
import { NavLink } from "react-router-dom";
import { 
  FaHome,
  FaUsers,
  FaCalendarCheck,
  FaFileMedical,
  FaPrescriptionBottle,
  FaHeartbeat,
  FaComments,
  FaClock,
  FaUserCog,
  FaSignOutAlt
} from "react-icons/fa";
import "../styles/components/sidebarmedico.css";

export default function SidebarMedico() {

  const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("ruolo");
    localStorage.removeItem("idUtente");
    localStorage.removeItem("idMedico");
    window.location.href = "/login";
  };

  return (
    <div className="sidebar">
      <h2 className="sidebar-title">Medico</h2>

      <nav className="sidebar-menu">

        <NavLink to="/dashboard-medico" className="sidebar-link">
          <FaHome className="sidebar-icon" />
          <span>Dashboard</span>
        </NavLink>

        <NavLink to="/medico/pazienti" className="sidebar-link">
          <FaUsers className="sidebar-icon" />
          <span>Gestione Pazienti</span>
        </NavLink>

        <NavLink to="/medico/visite" className="sidebar-link">
          <FaCalendarCheck className="sidebar-icon" />
          <span>Gestione Visite</span>
        </NavLink>

        <NavLink to="/medico/esami" className="sidebar-link">
          <FaFileMedical className="sidebar-icon" />
          <span>Esami e Referti</span>
        </NavLink>

        <NavLink to="/medico/terapie" className="sidebar-link">
          <FaPrescriptionBottle className="sidebar-icon" />
          <span>Gestione Terapie</span>
        </NavLink>

        <NavLink to="/medico/parametri" className="sidebar-link">
          <FaHeartbeat className="sidebar-icon" />
          <span>Parametri Vitali</span>
        </NavLink>

        <NavLink to="/medico/conversazioni" className="sidebar-link">
          <FaComments className="sidebar-icon" />
          <span>Conversazioni</span>
        </NavLink>

        <NavLink to="/medico/disponibilita" className="sidebar-link">
          <FaClock className="sidebar-icon" />
          <span>Disponibilità</span>
        </NavLink>

        <NavLink to="/medico/profilo" className="sidebar-link">
          <FaUserCog className="sidebar-icon" />
          <span>Profilo</span>
        </NavLink>

        <button className="sidebar-link logout-btn" onClick={handleLogout}>
          <FaSignOutAlt className="sidebar-icon" />
          <span>Logout</span>
        </button>

      </nav>
    </div>
  );
}
