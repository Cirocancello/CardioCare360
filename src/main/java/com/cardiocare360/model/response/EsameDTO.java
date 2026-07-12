package com.cardiocare360.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalTime;

public class EsameDTO {

    private Long id;
    private Long idPaziente;
    private Long idMedico;

    private String nomeMedico;
    private String cognomeMedico;
    private String specializzazioneMedico;

    private String tipoEsame;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataEsame;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime oraEsame;

    private String stato;
    private String note;

    private boolean refertoPresente;

    public EsameDTO() {}

    // Getter e Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getIdPaziente() { return idPaziente; }
    public void setIdPaziente(Long idPaziente) { this.idPaziente = idPaziente; }

    public Long getIdMedico() { return idMedico; }
    public void setIdMedico(Long idMedico) { this.idMedico = idMedico; }

    public String getNomeMedico() { return nomeMedico; }
    public void setNomeMedico(String nomeMedico) { this.nomeMedico = nomeMedico; }

    public String getCognomeMedico() { return cognomeMedico; }
    public void setCognomeMedico(String cognomeMedico) { this.cognomeMedico = cognomeMedico; }

    public String getSpecializzazioneMedico() { return specializzazioneMedico; }
    public void setSpecializzazioneMedico(String specializzazioneMedico) { this.specializzazioneMedico = specializzazioneMedico; }

    public String getTipoEsame() { return tipoEsame; }
    public void setTipoEsame(String tipoEsame) { this.tipoEsame = tipoEsame; }

    public LocalDate getDataEsame() { return dataEsame; }
    public void setDataEsame(LocalDate dataEsame) { this.dataEsame = dataEsame; }

    public LocalTime getOraEsame() { return oraEsame; }
    public void setOraEsame(LocalTime oraEsame) { this.oraEsame = oraEsame; }

    public String getStato() { return stato; }
    public void setStato(String stato) { this.stato = stato; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public boolean isRefertoPresente() { return refertoPresente; }
    public void setRefertoPresente(boolean refertoPresente) { this.refertoPresente = refertoPresente; }

    // ---------------------------------------------------------
    // VALIDAZIONI DI SICUREZZA
    // ---------------------------------------------------------

    public boolean isValid() {
        return id != null && id > 0 &&
               idPaziente != null && idPaziente > 0 &&
               idMedico != null && idMedico > 0 &&
               tipoEsame != null && !tipoEsame.isBlank() &&
               dataEsame != null &&
               oraEsame != null &&
               stato != null && !stato.isBlank();
    }

    public boolean hasMedicoData() {
        return nomeMedico != null && !nomeMedico.isBlank()
                && cognomeMedico != null && !cognomeMedico.isBlank();
    }
}
