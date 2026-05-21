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

// 📌 Altre pagine paziente
import Appuntamenti from "./pages/paziente/Appuntamenti";
import PrenotazioneVisite from "./pages/paziente/PrenotazioneVisite";

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

        {/* 📌 Altre pagine paziente */}
        <Route path="/paziente/appuntamenti" element={<Appuntamenti />} />
       
      </Route>
    </Routes>
  );
}

export default App;
