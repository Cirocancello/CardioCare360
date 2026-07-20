import React from "react";
import { useNavigate } from "react-router-dom";
import SidebarMedico from "../../components/SidebarMedico";
import TopbarMedico from "../../components/TopbarMedico";
import "../../styles/medico/parametriMedico.css";

const ParametriVitaliMedico = () => {
  const navigate = useNavigate();

  return (
    <div className="layout-medico">
      <SidebarMedico />
      <div className="lista-visite-container">
        <TopbarMedico />

        <div className="lista-parametri-header">
          <h2 className="lista-parametri-title">Monitoraggio Parametri Vitali</h2>
        </div>

        <div className="no-parametri">
          Questa sezione mostra lo storico dei parametri dei pazienti.
          <br />
          Premi il pulsante qui sotto per aprire la lista dei pazienti.
        </div>

        <button
          className="btn-storico"
          onClick={() => navigate("/medico/pazienti")}
        >
          Vai ai Pazienti
        </button>
      </div>
    </div>
  );
};

export default ParametriVitaliMedico;
