package com.cardiocare360.model.response;

import java.time.LocalDate;
import java.time.LocalTime;
import com.fasterxml.jackson.annotation.JsonFormat;

public class AppuntamentoDTO {

    private Long id;
    private Long idPaziente;
    private Long idMedico;

    // 🔹 Dati Medico
    private String nomeMedico;
    private String cognomeMedico;
    private String specializzazioneMedico;

    // 🔹 Dati Paziente (aggiunti)
    private String nomePaziente;
    private String cognomePaziente;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataAppuntamento;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime oraAppuntamento;

    private String stato;
    private String note;
    private String tipoVisita;

    public AppuntamentoDTO() {}

    // -------------------------
    // Getter e Setter
    // -------------------------

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

    public String getNomePaziente() { return nomePaziente; }
    public void setNomePaziente(String nomePaziente) { this.nomePaziente = nomePaziente; }

    public String getCognomePaziente() { return cognomePaziente; }
    public void setCognomePaziente(String cognomePaziente) { this.cognomePaziente = cognomePaziente; }

    public LocalDate getDataAppuntamento() { return dataAppuntamento; }
    public void setDataAppuntamento(LocalDate dataAppuntamento) { this.dataAppuntamento = dataAppuntamento; }

    public LocalTime getOraAppuntamento() { return oraAppuntamento; }
    public void setOraAppuntamento(LocalTime oraAppuntamento) { this.oraAppuntamento = oraAppuntamento; }

    public String getStato() { return stato; }
    public void setStato(String stato) { this.stato = stato; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getTipoVisita() { return tipoVisita; }
    public void setTipoVisita(String tipoVisita) { this.tipoVisita = tipoVisita; }
}
