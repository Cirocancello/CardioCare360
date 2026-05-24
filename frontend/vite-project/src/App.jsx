import { Routes, Route } from "react-router-dom";

// 🌐 Pagine pubbliche
import HomePage from "./pages/public/HomePage";
import AboutPage from "./pages/public/AboutPage";
import ServicesPage from "./pages/public/ServicesPage";
import ContactPage from "./pages/public/ContactPage";
import Login from "./pages/public/Login";
import ForgotPassword from "./pages/public/ForgotPassword";

// 🔒 Pagine protette
import DashboardPaziente from "./pages/paziente/DashboardPaziente";
import DashboardMedico from "./pages/medico/DashboardMedico";
import AdminDashboard from "./pages/admin/AdminDashboard";

// 📌 Prenotazione visite
import SelezionaVisita from "./pages/paziente/SelezionaVisita";
import SelezionaMedico from "./pages/paziente/SelezionaMedico";
import SelezionaData from "./pages/paziente/SelezionaData";
import SelezionaOrario from "./pages/paziente/SelezionaOrario";
import RiepilogoPrenotazione from "./pages/paziente/RiepilogoPrenotazione";
import PrenotazioneConfermata from "./pages/paziente/PrenotazioneConfermata";

// 📌 Flusso appuntamenti paziente
import Appuntamenti from "./pages/paziente/Appuntamenti";
import DettaglioAppuntamento from "./pages/paziente/DettaglioAppuntamento";
import ModificaAppuntamento from "./pages/paziente/ModificaAppuntamento";
import ConfermaAnnullamento from "./pages/paziente/ConfermaAnnullamento";

// 📌 Flusso esami/referti paziente
import EsamiList from "./pages/paziente/EsamiList";
import EsameDettaglio from "./pages/paziente/EsameDettaglio";
import PrenotaEsame from "./pages/paziente/PrenotaEsame";
import PrenotazioneEsameConfermata from "./pages/paziente/PrenotazioneEsameConfermata";   // ✅ IMPORT MANCANTE AGGIUNTO

// 🧩 Layout e protezione
import DashboardLayout from "./Layouts/DashboardLayout";
import ProtectedRoute from "./components/ProtectedRoute";

function App() {
  return (
    <Routes>

      {/* 🌐 Rotte pubbliche */}
      <Route path="/" element={<HomePage />} />
      <Route path="/about" element={<AboutPage />} />
      <Route path="/services" element={<ServicesPage />} />
      <Route path="/contatti" element={<ContactPage />} />
      <Route path="/login" element={<Login />} />
      <Route path="/forgot-password" element={<ForgotPassword />} />

      {/* 🔒 Rotte protette */}
      <Route
        element={
          <ProtectedRoute>
            <DashboardLayout />
          </ProtectedRoute>
        }
      >

        {/* 📌 Dashboard */}
        <Route path="/dashboard-paziente" element={<DashboardPaziente />} />
        <Route path="/dashboard-medico" element={<DashboardMedico />} />
        <Route path="/admin" element={<AdminDashboard />} />

        {/* 📌 Prenotazione visite */}
        <Route path="/paziente/prenota/visita" element={<SelezionaVisita />} />
        <Route path="/paziente/prenota/medico" element={<SelezionaMedico />} />
        <Route path="/paziente/prenota/data" element={<SelezionaData />} />
        <Route path="/paziente/prenota/orario" element={<SelezionaOrario />} />
        <Route path="/paziente/prenota/riepilogo" element={<RiepilogoPrenotazione />} />
        <Route path="/paziente/prenota/confermata" element={<PrenotazioneConfermata />} />

        {/* 📌 Flusso appuntamenti paziente */}
        <Route path="/paziente/appuntamenti" element={<Appuntamenti />} />
        <Route path="/paziente/appuntamenti/:id" element={<DettaglioAppuntamento />} />
        <Route path="/paziente/appuntamenti/:id/modifica" element={<ModificaAppuntamento />} />
        <Route path="/paziente/appuntamenti/:id/annulla" element={<ConfermaAnnullamento />} />

        {/* 📌 Flusso esami/referti paziente */}
        <Route path="/paziente/esami" element={<EsamiList />} />
        <Route path="/paziente/esami/:id" element={<EsameDettaglio />} />

        {/* 🔥 Prenotazione esami */}
        <Route path="/paziente/prenota/esame" element={<PrenotaEsame />} />

        {/* 🔥 Conferma prenotazione esame */}
        <Route path="/paziente/prenota-esame/confermata" element={<PrenotazioneEsameConfermata />} />

      </Route>
    </Routes>
  );
}

export default App;
