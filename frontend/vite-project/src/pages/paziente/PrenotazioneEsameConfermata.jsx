import React from "react";
import { useLocation, useNavigate } from "react-router-dom";
import "../../styles/paziente/esameConfermato.css";

export default function PrenotazioneEsameConfermata() {

    const location = useLocation();
    const navigate = useNavigate();

    // I dati dell’esame arrivano dalla pagina precedente tramite navigate()
    const esame = location.state?.esame;

    return (
        <div className="conferma-container">

            <div className="conferma-box">
                <h1 className="titolo-conferma">Esame prenotato con successo</h1>
                <p className="icona-conferma">✅</p>

                <p className="testo-conferma">
                    La tua prenotazione è stata registrata correttamente.
                </p>

                {esame && (
                    <div className="riepilogo-box">
                        <h3>Riepilogo esame</h3>

                        <p><strong>Tipo:</strong> {esame.tipoEsame}</p>
                        <p><strong>Data:</strong> {esame.dataEsame}</p>
                        <p><strong>Ora:</strong> {esame.oraEsame}</p>
                        <p><strong>Medico:</strong> {esame.nomeMedico} {esame.cognomeMedico}</p>

                        {esame.note && (
                            <p><strong>Note:</strong> {esame.note}</p>
                        )}
                    </div>
                )}

                <div className="azioni-conferma">
                    <button
                        className="btn-primary"
                        onClick={() => navigate("/paziente/esami")}
                    >
                        Vai ai miei esami
                    </button>

                    <button
                        className="btn-secondary"
                        onClick={() => navigate("/paziente/prenota/esame")}
                    >
                        Prenota un altro esame
                    </button>

                </div>
            </div>

        </div>
    );
}
