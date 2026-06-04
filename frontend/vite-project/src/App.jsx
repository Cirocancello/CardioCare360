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
import PrenotazioneEsameConfermata from "./pages/paziente/PrenotazioneEsameConfermata";

// 📌 Parametri vitali
import InserisciParametri from "./pages/paziente/InserisciParametri";
import StoricoParametri from "./pages/paziente/StoricoParametri";

// 📌 Terapie
import ListaTerapie from "./pages/paziente/ListaTerapie";

// 📌 Conversazioni
import ListaConversazioni from "./pages/paziente/ListaConversazioni";
import DettaglioConversazione from "./pages/paziente/DettaglioConversazione";

// 🧩 Protezione
import ProtectedRoute from "./components/ProtectedRoute";

// Profilo Paziente
import ProfiloPaziente from "./pages/paziente/ProfiloPaziente";

// 📌 Gestione Pazienti (Medico)
import ListaPazienti from "./pages/medico/ListaPazienti";
import DettaglioPaziente from "./pages/medico/DettaglioPaziente";
import ListaVisiteMedico from "./pages/medico/ListaVisiteMedico";
import DettaglioVisitaMedico from "./pages/medico/DettaglioVisitaMedico";

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
        path="/dashboard-paziente"
        element={
          <ProtectedRoute>
            <DashboardPaziente />
          </ProtectedRoute>
        }
      />

      <Route
        path="/dashboard-medico"
        element={
          <ProtectedRoute>
            <DashboardMedico />
          </ProtectedRoute>
        }
      />

      {/* 📌 Lista Pazienti */}
      <Route
        path="/medico/pazienti"
        element={
          <ProtectedRoute>
            <ListaPazienti />
          </ProtectedRoute>
        }
      />

      { /* Dettaglio Paziente */}  
      <Route
        path="/medico/pazienti/:id"
        element={
          <ProtectedRoute>
            <DettaglioPaziente />
          </ProtectedRoute>
        }
/>
  
      <Route
        path="/admin"
        element={
          <ProtectedRoute>
            <AdminDashboard />
          </ProtectedRoute>
        }
      />

      {/* 📌 Prenotazione visite */}
      <Route
        path="/paziente/prenota/visita"
        element={
          <ProtectedRoute>
            <SelezionaVisita />
          </ProtectedRoute>
        }
      />

      <Route
        path="/paziente/prenota/medico"
        element={
          <ProtectedRoute>
            <SelezionaMedico />
          </ProtectedRoute>
        }
      />

      <Route
        path="/paziente/prenota/data"
        element={
          <ProtectedRoute>
            <SelezionaData />
          </ProtectedRoute>
        }
      />

      <Route
        path="/paziente/prenota/orario"
        element={
          <ProtectedRoute>
            <SelezionaOrario />
          </ProtectedRoute>
        }
      />

      <Route
        path="/paziente/prenota/riepilogo"
        element={
          <ProtectedRoute>
            <RiepilogoPrenotazione />
          </ProtectedRoute>
        }
      />

      <Route
        path="/paziente/prenota/confermata"
        element={
          <ProtectedRoute>
            <PrenotazioneConfermata />
          </ProtectedRoute>
        }
      />

      {/* 📌 Appuntamenti */}
      <Route
        path="/paziente/appuntamenti"
        element={
          <ProtectedRoute>
            <Appuntamenti />
          </ProtectedRoute>
        }
      />

      <Route
        path="/paziente/appuntamenti/:id"
        element={
          <ProtectedRoute>
            <DettaglioAppuntamento />
          </ProtectedRoute>
        }
      />

      <Route
        path="/paziente/appuntamenti/:id/modifica"
        element={
          <ProtectedRoute>
            <ModificaAppuntamento />
          </ProtectedRoute>
        }
      />

      <Route
        path="/paziente/appuntamenti/:id/annulla"
        element={
          <ProtectedRoute>
            <ConfermaAnnullamento />
          </ProtectedRoute>
        }
      />

      {/* 📌 Esami */}
      <Route
        path="/paziente/esami"
        element={
          <ProtectedRoute>
            <EsamiList />
          </ProtectedRoute>
        }
      />

      <Route
        path="/paziente/esami/:id"
        element={
          <ProtectedRoute>
            <EsameDettaglio />
          </ProtectedRoute>
        }
      />

      <Route
        path="/paziente/prenota/esame"
        element={
          <ProtectedRoute>
            <PrenotaEsame />
          </ProtectedRoute>
        }
      />

      <Route
        path="/paziente/prenota-esame/confermata"
        element={
          <ProtectedRoute>
            <PrenotazioneEsameConfermata />
          </ProtectedRoute>
        }
      />

      {/* 🩺 Parametri vitali */}
      <Route
        path="/paziente/parametri/inserisci"
        element={
          <ProtectedRoute>
            <InserisciParametri />
          </ProtectedRoute>
        }
      />

      <Route
        path="/paziente/storico-parametri"
        element={
          <ProtectedRoute>
            <StoricoParametri />
          </ProtectedRoute>
        }
      />

      {/* 💊 Terapie */}
      <Route
        path="/paziente/terapie"
        element={
          <ProtectedRoute>
            <ListaTerapie />
          </ProtectedRoute>
        }
      />

      {/* 💬 Conversazioni */}
      <Route
        path="/paziente/conversazioni"
        element={
          <ProtectedRoute>
            <ListaConversazioni />
          </ProtectedRoute>
        }
      />

      <Route
        path="/paziente/conversazioni/:id"
        element={
          <ProtectedRoute>
            <DettaglioConversazione />
          </ProtectedRoute>
        }
      />

      {/* 👤 Profilo */}
      <Route
        path="/paziente/profilo"
        element={
          <ProtectedRoute>
            <ProfiloPaziente />
          </ProtectedRoute>
        }
      />

      {/* 📌 Lista Visite Medico */}
      <Route
        path="/medico/visite"
        element={
          <ProtectedRoute>
            <ListaVisiteMedico />
          </ProtectedRoute>
        }
      />

      {/* 📌 Dettaglio Visita Medico */}
      <Route
        path="/medico/visite/:id"
        element={
          <ProtectedRoute>
            <DettaglioVisitaMedico />
          </ProtectedRoute>
        }
      />


    </Routes>
  );
}

export default App;
