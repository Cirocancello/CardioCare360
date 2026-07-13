import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import "../../styles/paziente/selezionaVisita.css";

export default function SelezionaOrario() {
  const navigate = useNavigate();

  const [slotDisponibili, setSlotDisponibili] = useState([]);
  const [orariOccupati, setOrariOccupati] = useState([]);
  const [orarioSelezionato, setOrarioSelezionato] = useState("");

  useEffect(() => {
    const data = localStorage.getItem("dataPrenotazione");
    const idMedico = localStorage.getItem("idMedicoSelezionato");

    if (!data || !idMedico) {
      navigate("/paziente/prenota/data");
      return;
    }

    axios
      .get(`http://localhost:8080/disponibilita/slot/${idMedico}`, {
        params: { data },
      })
      .then((res) => {
        const unici = res.data.filter(
          (slot, index, self) =>
            index === self.findIndex((s) => s.inizio === slot.inizio)
        );
        setSlotDisponibili(unici);
      })
      .catch((err) => console.error("Errore nel caricamento slot:", err));

    axios
      .get("http://localhost:8080/appuntamenti/occupati", {
        params: { idMedico, data },
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      })
      .then((res) => setOrariOccupati(res.data))
      .catch((err) => console.error("Errore orari occupati:", err));
  }, [navigate]);

  const handleNext = () => {
    if (!orarioSelezionato) return;

    const oraFormattata = orarioSelezionato.substring(11, 16);
    localStorage.setItem("oraPrenotazione", oraFormattata);

    navigate("/paziente/prenota/riepilogo");
  };

  return (
    <div className="visita-container">
      <h2>Seleziona l'orario</h2>

      <p className="visita-descrizione">
        Data scelta: <strong>{localStorage.getItem("dataPrenotazione")}</strong>
      </p>

      <div className="visita-grid">
        {slotDisponibili?.length > 0 ? (
          slotDisponibili.map((slot) => {
            const oraSlot = slot.inizio.substring(11, 16);
            const oraFine = slot.fine.substring(11, 16);
            const occupato = orariOccupati.some((ora) =>
              ora.startsWith(oraSlot)
            );

            return (
              <div
                key={slot.id}
                className={`visita-card 
                  ${orarioSelezionato === slot.inizio ? "selected" : ""} 
                  ${occupato ? "disabled-slot" : ""}`}
                onClick={() => {
                  if (!occupato) {
                    setOrarioSelezionato(slot.inizio);
                  }
                }}
              >
                <span>{oraSlot} - {oraFine}</span>
              </div>
            );
          })
        ) : (
          <p>Nessuno slot disponibile per questa data.</p>
        )}
      </div>

      <div className="bottoni-navigazione">
        <button className="btn-indietro" onClick={() => navigate(-1)}>
          Indietro
        </button>

        <button
          className="btn-continua"
          disabled={!orarioSelezionato}
          onClick={handleNext}
        >
          Continua
        </button>
      </div>
    </div>
  );
}
