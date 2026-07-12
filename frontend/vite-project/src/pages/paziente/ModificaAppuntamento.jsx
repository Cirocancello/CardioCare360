import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import "../../styles/paziente/appuntamenti.css";

export default function ModificaAppuntamento() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [form, setForm] = useState({
    dataAppuntamento: "",
    oraAppuntamento: "",
    note: ""
  });

  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem("token");

    async function fetchData() {
      const res = await fetch(`http://localhost:8080/appuntamenti/${id}`, {
        headers: { Authorization: `Bearer ${token}` }
      });

      if (!res.ok) return;

      const data = await res.json();

      setForm({
        dataAppuntamento: data.dataAppuntamento,
        oraAppuntamento: data.oraAppuntamento,
        note: data.note || ""
      });

      setLoading(false);
    }

    fetchData();
  }, [id]);

  function handleChange(e) {
    setForm({ ...form, [e.target.name]: e.target.value });
  }

  async function handleSubmit(e) {
    e.preventDefault();

    const token = localStorage.getItem("token");

    const res = await fetch(`http://localhost:8080/appuntamenti/${id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`
      },
      body: JSON.stringify(form)
    });

    if (res.ok) {
      navigate(`/paziente/appuntamenti/${id}`);
    }
  }

  if (loading) return <p>Caricamento...</p>;

  return (
    <div className="page-container">
      
      <h1 className="page-title">Modifica Appuntamento</h1>

      <form className="appointment-form" onSubmit={handleSubmit}>
        <label>Data</label>
        <input
          type="date"
          name="dataAppuntamento"
          value={form.dataAppuntamento}
          onChange={handleChange}
          required
        />

        <label>Ora</label>
        <input
          type="time"
          name="oraAppuntamento"
          value={form.oraAppuntamento}
          onChange={handleChange}
          required
        />

        <label>Note</label>
        <textarea
          name="note"
          value={form.note}
          onChange={handleChange}
        />

        <button className="btn-primary" type="submit">
          Salva modifiche
        </button>

        {/* 🔙 Pulsante torna indietro */}
      <button
        className="btn-secondary"
        onClick={() => navigate(-1)}
        style={{ marginBottom: "15px" }}
      >
        ← Torna indietro
      </button>
      
      </form>
    </div>
  );
}
