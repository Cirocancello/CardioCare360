import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

const PrenotaEsame = () => {

    const [tipoEsame, setTipoEsame] = useState("");
    const [disponibilita, setDisponibilita] = useState(null);
    const [loadingDisponibilita, setLoadingDisponibilita] = useState(false);

    const [note, setNote] = useState("");

    // 🔥 NUOVI STATI PER MEDICO DINAMICO
    const [medici, setMedici] = useState([]);
    const [medicoSelezionato, setMedicoSelezionato] = useState("");

    const user = JSON.parse(localStorage.getItem("user"));
    const navigate = useNavigate();

    // 🔹 Fetch disponibilità
    const fetchDisponibilita = async (tipo) => {
        try {
            setLoadingDisponibilita(true);

            const response = await fetch(
                `http://localhost:8080/esami/disponibilita/prossima?tipo=${tipo}`,
                {
                    headers: {
                        "Authorization": `Bearer ${localStorage.getItem("token")}`
                    }
                }
            );

            const data = await response.json();
            setDisponibilita(data);

        } catch (error) {
            console.error("Errore nel calcolo disponibilità", error);
        } finally {
            setLoadingDisponibilita(false);
        }
    };

    // 🔹 Fetch medici dinamici
    const fetchMedici = async (tipo) => {
        try {
            const response = await fetch(
                `http://localhost:8080/medici/esami?tipo=${tipo}`,
                {
                    headers: {
                        "Authorization": `Bearer ${localStorage.getItem("token")}`
                    }
                }
            );

            const data = await response.json();
            setMedici(data);

        } catch (error) {
            console.error("Errore nel caricamento medici", error);
        }
    };

    // 🔹 Quando cambia il tipo esame → aggiorna disponibilità + medici
    useEffect(() => {
        if (tipoEsame !== "") {
            fetchDisponibilita(tipoEsame);
            fetchMedici(tipoEsame);
        }
    }, [tipoEsame]);

    // 🔹 Prenotazione esame
    const prenotaEsame = async () => {

        if (!user || !user.idPaziente) {
            alert("Errore: utente non trovato. Effettua nuovamente il login.");
            return;
        }

        if (!medicoSelezionato) {
            alert("Seleziona un medico.");
            return;
        }

        const body = {
            idPaziente: user.idPaziente,
            idMedico: medicoSelezionato,
            tipoEsame: tipoEsame,
            dataEsame: disponibilita.data,
            oraEsame: disponibilita.ora,
            note: note
        };

        const response = await fetch("http://localhost:8080/esami/prenota", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${localStorage.getItem("token")}`
            },
            body: JSON.stringify(body)
        });

        const result = await response.json();
        console.log("Esame prenotato:", result);

        navigate("/paziente/prenota-esame/confermata", {
            state: { esame: result }
        });
    };

    return (
        <div className="prenotazione-container">

            <h2>Prenota Esame</h2>

            {/* Tipo esame */}
            <div className="form-group">
                <label>Tipo di esame</label>
                <select
                    value={tipoEsame}
                    onChange={(e) => setTipoEsame(e.target.value)}
                >
                    <option value="">Seleziona esame</option>
                    <option value="ECG">ECG</option>
                    <option value="HOLTER">Holter</option>
                    <option value="ECOCARDIOGRAMMA">Ecocardiogramma</option>
                </select>
            </div>

            {/* Medico dinamico */}
            {medici.length > 0 && (
                <div className="form-group">
                    <label>Medico</label>
                    <select
                        value={medicoSelezionato}
                        onChange={(e) => setMedicoSelezionato(e.target.value)}
                    >
                        <option value="">Seleziona medico</option>
                        {medici.map(m => (
                            <option key={m.id} value={m.id}>
                                {m.nomeCompleto}
                            </option>
                        ))}
                    </select>
                </div>
            )}

            {loadingDisponibilita && <p>Calcolo disponibilità...</p>}

            {disponibilita && (
                <div className="box-disponibilita">
                    <h4>Prossima disponibilità</h4>
                    <p><strong>Data:</strong> {disponibilita.data}</p>
                    <p><strong>Ora:</strong> {disponibilita.ora}</p>
                </div>
            )}

            <div className="form-group">
                <label>Note (opzionali)</label>
                <textarea
                    value={note}
                    onChange={(e) => setNote(e.target.value)}
                    placeholder="Inserisci eventuali note..."
                />
            </div>

            <button
                disabled={!disponibilita || !medicoSelezionato}
                onClick={prenotaEsame}
                className="btn-primary"
            >
                Conferma prenotazione
            </button>

        </div>
    );
};

export default PrenotaEsame;
