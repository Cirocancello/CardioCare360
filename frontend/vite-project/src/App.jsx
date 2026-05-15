import { Routes, Route } from "react-router-dom";
import Login from "./pages/Login";
import Register from "./pages/Register";
import ForgotPassword from "./pages/ForgotPassword";
import DashboardPaziente from "./pages/DashboardPaziente";
import DashboardMedico from "./pages/DashboardMedico";
import AdminDashboard from "./pages/AdminDashboard";
import ProtectedRoute from "./components/ProtectedRoute";

function App() {
  return (
    <Routes>
      {/* Rotte pubbliche */}
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />
      <Route path="/forgot-password" element={<ForgotPassword />} />

      {/* Rotte protette */}
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
