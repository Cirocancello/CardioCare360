package com.cardiocare360.model.request;

public class RefertoRequest {

    private Long pazienteId;
    private Long medicoId;
    private String titolo;
    private String descrizione;
    private String diagnosi;

    // Getter e Setter
    public Long getPazienteId() { return pazienteId; }
    public void setPazienteId(Long pazienteId) { this.pazienteId = pazienteId; }

    public Long getMedicoId() { return medicoId; }
    public void setMedicoId(Long medicoId) { this.medicoId = medicoId; }

    public String getTitolo() { return titolo; }
    public void setTitolo(String titolo) { this.titolo = titolo; }

    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

    public String getDiagnosi() { return diagnosi; }
    public void setDiagnosi(String diagnosi) { this.diagnosi = diagnosi; }

    // ---------------------------------------------------------
    // VALIDAZIONI DI SICUREZZA
    // ---------------------------------------------------------

    public boolean isValid() {
        return isPazienteValido()
                && isMedicoValido()
                && isTitoloValido()
                && isDescrizioneValida()
                && isDiagnosiValida();
    }

    public boolean isPazienteValido() {
        return pazienteId != null && pazienteId > 0;
    }

    public boolean isMedicoValido() {
        return medicoId != null && medicoId > 0;
    }

    public boolean isTitoloValido() {
        return titolo != null && !titolo.isBlank();
    }

    public boolean isDescrizioneValida() {
        return descrizione != null && !descrizione.isBlank();
    }

    public boolean isDiagnosiValida() {
        return diagnosi != null && !diagnosi.isBlank();
    }
}
