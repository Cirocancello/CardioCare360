import React from "react";
import Sidebar from "../components/Sidebar";
import Topbar from "../components/Topbar";
import { Outlet, useLocation } from "react-router-dom";
import "../styles/paziente/dashboard.css";

export default function DashboardLayout() {
  const location = useLocation();

  return (
    <div className="dashboard-container">
      <Sidebar />

      <div className="dashboard-main">
        <Topbar />

        <div className="dashboard-content">
          {/* Rimonta il contenuto ogni volta che cambia la route */}
          <Outlet key={location.pathname} />
        </div>
      </div>
    </div>
  );
}
