package com.cardiocare360.service;

public interface PdfService {

    /**
     * Genera un PDF a partire da contenuto HTML e lo salva nella cartella referti.
     *
     * @param htmlContent contenuto HTML del referto
     * @param esameId id dell'esame per nominare il file
     * @return percorso completo del file PDF generato
     */
    String generaPdfReferto(String htmlContent, Long esameId);
}
