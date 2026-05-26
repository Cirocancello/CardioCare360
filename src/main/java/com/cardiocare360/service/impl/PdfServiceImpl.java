package com.cardiocare360.service.impl;

import com.cardiocare360.service.PdfService;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.StringReader;

@Service
public class PdfServiceImpl implements PdfService {

    private final String BASE_PATH = "C:/cardiocare360/uploads/referti/";

    @Override
    public String generaPdfReferto(String htmlContent, Long esameId) {
        try {
            // Percorso finale del PDF
            String filePath = BASE_PATH + "referto_" + esameId + ".pdf";

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));

            document.open();

            HTMLWorker htmlWorker = new HTMLWorker(document);
            htmlWorker.parse(new StringReader(htmlContent));

            document.close();

            return filePath;

        } catch (Exception e) {
            throw new RuntimeException("Errore durante la generazione del PDF: " + e.getMessage());
        }
    }
}
