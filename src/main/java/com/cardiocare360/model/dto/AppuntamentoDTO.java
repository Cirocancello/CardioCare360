package com.cardiocare360.model.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class AppuntamentoDTO {

    private Long id;
    private Long idPaziente;
    private Long idMedico;
    private LocalDate dataAppuntamento;
    private LocalTime oraAppuntamento;
    private String stato;
    private String note;

    public AppuntamentoDTO() {}

    // Getter e Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getIdPaziente() { return idPaziente; }
    public void setIdPaziente(Long idPaziente) { this.idPaziente = idPaziente; }

    public Long getIdMedico() { return idMedico; }
    public void setIdMedico(Long idMedico) { this.idMedico = idMedico; }

    public LocalDate getDataAppuntamento() { return dataAppuntamento; }
    public void setDataAppuntamento(LocalDate dataAppuntamento) { this.dataAppuntamento = dataAppuntamento; }

    public LocalTime getOraAppuntamento() { return oraAppuntamento; }
    public void setOraAppuntamento(LocalTime oraAppuntamento) { this.oraAppuntamento = oraAppuntamento; }

    public String getStato() { return stato; }
    public void setStato(String stato) { this.stato = stato; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
