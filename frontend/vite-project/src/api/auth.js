// =========================
// LOGIN
// =========================
export async function login(email, password) {
  try {
    const response = await fetch("http://localhost:8080/auth/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, password })
    });

    if (!response.ok) {
      throw new Error("Credenziali errate");
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



// =========================
// REGISTER
// =========================
export async function register(form) {
  try {
    const response = await fetch("http://localhost:8080/auth/register", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(form)
    });

    if (!response.ok) {
      const errorMessage = await response.text();
      throw new Error(errorMessage);
    }

    return await response.json();

  } catch (error) {
    console.error("Errore nella funzione register:", error);
    throw error;
  }
}



// =========================
// FORGOT PASSWORD
// =========================
export async function forgotPassword(email) {
  try {
    const response = await fetch("http://localhost:8080/auth/forgot-password", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email })
    });

    if (!response.ok) {
      const errorMessage = await response.text();
      throw new Error(errorMessage);
    }

    const message = await response.text();
    return message; // ✅ Restituisce testo semplice
  } catch (error) {
    console.error("Errore nella funzione forgotPassword:", error);
    throw error;
  }
}

