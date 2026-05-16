import { Routes, Route } from "react-router-dom";

import HomePage from "./pages/HomePage";
import Login from "./pages/Login";
import ForgotPassword from "./pages/ForgotPassword";

import AboutPage from "./pages/AboutPage";
import ServicesPage from "./pages/ServicesPage";
import ContactPage from "./pages/ContactPage";
import DashboardPaziente from "./pages/DashboardPaziente";
import DashboardMedico from "./pages/DashboardMedico";
import AdminDashboard from "./pages/AdminDashboard";

import ProtectedRoute from "./components/ProtectedRoute";

function App() {
  return (
    <Routes>

      {/* 🌐 Rotta pubblica: Home Page */}
      <Route path="/" element={<HomePage />} />

      {/* 🌐 Rotte pubbliche aggiunte */}
      <Route path="/about" element={<AboutPage />} />
      <Route path="/services" element={<ServicesPage />} />

      {/* 🔓 Rotte pubbliche */}
      <Route path="/login" element={<Login />} />
      <Route path="/forgot-password" element={<ForgotPassword />} />
      <Route path="/contatti" element={<ContactPage />} />


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

      <Route
        path="/admin"
        element={
          <ProtectedRoute>
            <AdminDashboard />
          </ProtectedRoute>
        }
      />

    </Routes>
  );
}

export default App;
