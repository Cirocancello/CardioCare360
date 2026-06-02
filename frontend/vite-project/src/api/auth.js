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

    // Se il backend risponde con errore, NON lanciare eccezione
    if (!response.ok) {
      const errorText = await response.text();
      console.error("Errore login backend:", errorText);
      return null;
    }

    // 🔥 Restituisce SOLO i dati, senza salvare nulla
    return await response.json();

  } catch (error) {
    console.error("Errore nella funzione login:", error);
    return null;
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

    // Il backend restituisce testo semplice
    return await response.text();

  } catch (error) {
    console.error("Errore nella funzione forgotPassword:", error);
    throw error;
  }
}
