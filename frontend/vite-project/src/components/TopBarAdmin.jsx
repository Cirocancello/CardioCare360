import React from "react";
import { FaBell, FaUserCircle } from "react-icons/fa";
import "../styles/components/topbaradmin.css";

export default function TopbarAdmin() {
  return (
    <div className="topbar-admin">
      <h2 className="topbar-title">CardioCare360 — Admin</h2>

      <div className="topbar-right">
        <div className="topbar-icon">
          <FaBell />
        </div>

        <div className="topbar-user">
          <FaUserCircle className="topbar-avatar" />
          <span className="topbar-username">Admin</span>
        </div>
      </div>
    </div>
  );
}
