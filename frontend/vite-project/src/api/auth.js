export async function login(email, password) {
  try {
    const response = await fetch("http://localhost:8080/auth/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, password })
    });

    if (!response.ok) {
      throw new Error("Errore durante il login: " + response.status);
    }

    const data = await response.json();

    // 🔥 Salvataggio dati nel localStorage
    localStorage.setItem("token", data.token);
    localStorage.setItem("ruolo", data.ruolo);
    localStorage.setItem("idUtente", data.idUtente);
    localStorage.setItem("idPaziente", data.idPaziente);

    return data;

  } catch (error) {
    console.error("Errore nella funzione login:", error);
    throw error;
  }
}
