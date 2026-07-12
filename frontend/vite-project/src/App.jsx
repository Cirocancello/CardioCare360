import { Routes, Route } from "react-router-dom";

// 🌐 Pagine pubbliche
import HomePage from "./pages/public/HomePage";
import AboutPage from "./pages/public/AboutPage";
import ServicesPage from "./pages/public/ServicesPage";
import ContactPage from "./pages/public/ContactPage";
import Login from "./pages/public/Login";
import ForgotPassword from "./pages/public/ForgotPassword";

// 🔒 Pagine protette
import ProtectedRoute from "./components/ProtectedRoute";

// Dashboard
import DashboardPaziente from "./pages/paziente/DashboardPaziente";
import DashboardMedico from "./pages/medico/DashboardMedico";
import DashboardAdmin from "./pages/admin/DashboardAdmin";

// 📌 Prenotazione visite (pagina iniziale)
import PrenotazioneVisite from "./pages/paziente/PrenotazioneVisite";

// 📌 Prenotazione visite (step)
import SelezionaVisita from "./pages/paziente/SelezionaVisita";
import SelezionaMedico from "./pages/paziente/SelezionaMedico";
import SelezionaData from "./pages/paziente/SelezionaData";
import SelezionaOrario from "./pages/paziente/SelezionaOrario";
import RiepilogoPrenotazione from "./pages/paziente/RiepilogoPrenotazione";
import PrenotazioneConfermata from "./pages/paziente/PrenotazioneConfermata";

// 📌 Appuntamenti paziente
import Appuntamenti from "./pages/paziente/Appuntamenti";
import DettaglioAppuntamento from "./pages/paziente/DettaglioAppuntamento";
import ModificaAppuntamento from "./pages/paziente/ModificaAppuntamento";
import ConfermaAnnullamento from "./pages/paziente/ConfermaAnnullamento";

// 📌 Esami paziente
import EsamiList from "./pages/paziente/EsamiList";
import EsameDettaglio from "./pages/paziente/EsameDettaglio";
import PrenotaEsame from "./pages/paziente/PrenotaEsame";
import PrenotazioneEsameConfermata from "./pages/paziente/PrenotazioneEsameConfermata";

// 📌 Parametri vitali paziente
import InserisciParametri from "./pages/paziente/InserisciParametri";
import StoricoParametri from "./pages/paziente/StoricoParametri";

// 📌 Terapie paziente
import ListaTerapie from "./pages/paziente/ListaTerapie";

// 📌 Conversazioni paziente
import ListaConversazioni from "./pages/paziente/ListaConversazioni";
import DettaglioConversazione from "./pages/paziente/DettaglioConversazione";

// Profilo paziente
import ProfiloPaziente from "./pages/paziente/ProfiloPaziente";

// 📌 Medico – gestione pazienti
import ListaPazienti from "./pages/medico/ListaPazienti";
import DettaglioPaziente from "./pages/medico/DettaglioPaziente";

// 📌 Medico – visite
import ListaVisiteMedico from "./pages/medico/ListaVisiteMedico";
import DettaglioVisitaMedico from "./pages/medico/DettaglioVisitaMedico";

// 📌 Medico – esami
import ListaEsamiDaRefertare from "./pages/medico/ListaEsamiDaRefertare";
import RefertaEsame from "./pages/medico/RefertaEsame";

// 📌 Medico – terapie
import ListaTerapieMedico from "./pages/medico/ListaTerapieMedico";
import CreaTerapia from "./pages/medico/CreaTerapia";

// 📌 Medico – parametri vitali
import ParametriVitaliMedico from "./pages/medico/ParametriVitaliMedico";
import StoricoParametriMedico from "./pages/medico/StoricoParametriMedico";

// 📌 Medico – conversazioni
import ListaConversazioniMedico from "./pages/medico/ListaConversazioniMedico";
import ChatMedico from "./pages/medico/ChatMedico";

// 📌 Medico – profilo
import ProfiloMedico from "./pages/medico/ProfiloMedico";
import CambiaPasswordMedico from "./pages/medico/CambiaPasswordMedico";
import DisponibilitaMedico from "./pages/medico/DisponibilitaMedico";
import AggiungiDisponibilita from "./pages/medico/AggiungiDisponibilita";

// 📌 Admin – gestione utenti
import GestioneMedici from "./pages/admin/GestioneMedici";
import GestionePazienti from "./pages/admin/GestionePazienti";
import CreaMedico from "./pages/admin/CreaMedico";
import ModificaMedico from "./pages/admin/ModificaMedico";
import ConfermaEliminazioneMedico from "./pages/admin/ConfermaEliminazioneMedico";
import DettaglioMedico from "./pages/admin/DettaglioMedico";
import DettaglioPazienteAdmin from "./pages/admin/DettaglioPazienteAdmin";
import ModificaPazienteAdmin from "./pages/admin/ModificaPazienteAdmin";
import CreaPaziente from "./pages/admin/CreaPaziente";
import ConfermaEliminazionePaziente from "./pages/admin/ConfermaEliminazionePaziente";

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

      {/* 🔒 Dashboard */}
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

      {/* 🛠 Admin */}
      <Route
        path="/admin/pazienti/:id/conferma-eliminazione"
        element={
          <ProtectedRoute>
            <ConfermaEliminazionePaziente />
          </ProtectedRoute>
        }
      />

      <Route
        path="/admin"
        element={
          <ProtectedRoute>
            <DashboardAdmin />
          </ProtectedRoute>
        }
      />

      <Route
        path="/admin/dashboard"
        element={
          <ProtectedRoute>
            <DashboardAdmin />
          </ProtectedRoute>
        }
      />

      <Route
        path="/admin/medici"
        element={
          <ProtectedRoute>
            <GestioneMedici />
          </ProtectedRoute>
        }
      />

      <Route
        path="/admin/pazienti"
        element={
          <ProtectedRoute>
            <GestionePazienti />
          </ProtectedRoute>
        }
      />

      <Route
        path="/admin/medici/crea"
        element={
          <ProtectedRoute>
            <CreaMedico />
          </ProtectedRoute>
        }
      />

      <Route
        path="/admin/paziente/crea"
        element={
          <ProtectedRoute>
            <CreaPaziente />
          </ProtectedRoute>
        }
      />

      <Route
        path="/admin/pazienti/:id"
        element={
          <ProtectedRoute>
            <DettaglioPazienteAdmin />
          </ProtectedRoute>
        }
      />

      <Route
        path="/admin/pazienti/:id/modifica"
        element={
          <ProtectedRoute>
            <ModificaPazienteAdmin />
          </ProtectedRoute>
        }
      />

      {/* 📌 Medico – gestione pazienti */}
      <Route
        path="/medico/pazienti"
        element={
          <ProtectedRoute>
            <ListaPazienti />
          </ProtectedRoute>
        }
      />

      <Route
        path="/medico/pazienti/:id"
        element={
          <ProtectedRoute>
            <DettaglioPaziente />
          </ProtectedRoute>
        }
      />

      {/* 📌 Medico – parametri */}
      <Route
        path="/medico/parametri"
        element={
          <ProtectedRoute>
            <ParametriVitaliMedico />
          </ProtectedRoute>
        }
      />

      <Route
        path="/medico/parametri/storico/:idPaziente"
        element={
          <ProtectedRoute>
            <StoricoParametriMedico />
          </ProtectedRoute>
        }
      />

      {/* 📌 Medico – visite */}
      <Route
        path="/medico/visite"
        element={
          <ProtectedRoute>
            <ListaVisiteMedico />
          </ProtectedRoute>
        }
      />

      <Route
        path="/medico/visite/:id"
        element={
          <ProtectedRoute>
            <DettaglioVisitaMedico />
          </ProtectedRoute>
        }
      />

      {/* 📌 Medico – esami */}
      <Route
        path="/medico/esami"
        element={
          <ProtectedRoute>
            <ListaEsamiDaRefertare />
          </ProtectedRoute>
        }
      />

      <Route
        path="/medico/esami/referta/:idEsame"
        element={
          <ProtectedRoute>
            <RefertaEsame />
          </ProtectedRoute>
        }
      />

      {/* 📌 Medico – terapie */}
      <Route
        path="/medico/terapie"
        element={
          <ProtectedRoute>
            <ListaTerapieMedico />
          </ProtectedRoute>
        }
      />

      <Route
        path="/medico/terapie/crea"
        element={
          <ProtectedRoute>
            <CreaTerapia />
          </ProtectedRoute>
        }
      />

      {/* 📌 Medico – conversazioni */}
      <Route
        path="/medico/conversazioni"
        element={
          <ProtectedRoute>
            <ListaConversazioniMedico />
          </ProtectedRoute>
        }
      />

      <Route
        path="/medico/conversazioni/:idConversazione"
        element={
          <ProtectedRoute>
            <ChatMedico />
          </ProtectedRoute>
        }
      />

      {/* 📌 Medico – profilo */}
      <Route
        path="/medico/profilo"
        element={
          <ProtectedRoute>
            <ProfiloMedico />
          </ProtectedRoute>
        }
      />

      <Route
        path="/medico/cambia-password"
        element={
          <ProtectedRoute>
            <CambiaPasswordMedico />
          </ProtectedRoute>
        }
      />

      <Route
        path="/medico/disponibilita"
        element={
          <ProtectedRoute>
            <DisponibilitaMedico />
          </ProtectedRoute>
        }
      />

      <Route
        path="/medico/disponibilita/aggiungi"
        element={
          <ProtectedRoute>
            <AggiungiDisponibilita />
          </ProtectedRoute>
        }
      />

      <Route
        path="/paziente/prenota"
        element={
          <ProtectedRoute>
            <PrenotazioneVisite />
          </ProtectedRoute>
        }
      />


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

      {/* 📌 Paziente – appuntamenti */}
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

      {/* 📌 Paziente – esami */}
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

      {/* 📌 Paziente – parametri */}
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

      {/* 📌 Paziente – terapie */}
      <Route
        path="/paziente/terapie"
        element={
          <ProtectedRoute>
            <ListaTerapie />
          </ProtectedRoute>
        }
      />

      {/* 📌 Paziente – conversazioni */}
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

      {/* 📌 Paziente – profilo */}
      <Route
        path="/paziente/profilo"
        element={
          <ProtectedRoute>
            <ProfiloPaziente />
          </ProtectedRoute>
        }
      />

      {/* 📌 Admin – medico */}
      <Route
        path="/admin/medici/:id/modifica"
        element={
          <ProtectedRoute>
            <ModificaMedico />
          </ProtectedRoute>
        }
      />

      <Route
        path="/admin/medici/:id/elimina"
        element={
          <ProtectedRoute>
            <ConfermaEliminazioneMedico />
          </ProtectedRoute>
        }
      />

      <Route
        path="/admin/medici/:id"
        element={
          <ProtectedRoute>
            <DettaglioMedico />
          </ProtectedRoute>
        }
      />

    </Routes>
  );
}

export default App;
