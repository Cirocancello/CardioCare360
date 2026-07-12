import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080",
});

// 🔐 Interceptor: aggiunge automaticamente il token JWT
api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");

  if (token && token !== "null" && token !== "undefined") {
    config.headers.Authorization = `Bearer ${token}`;
  }

  return config;
});

// ⚠️ Interceptor: gestione errori SENZA logout automatico
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && error.response.status === 401) {
      console.warn("⚠️ 401 ricevuto: token mancante, non valido o endpoint non autorizzato.");
      // ❌ NON cancelliamo il token
      // ❌ NON reindirizziamo al login
      // Lasciamo che la pagina gestisca l’errore
    }

    return Promise.reject(error);
  }
);

export default api;
