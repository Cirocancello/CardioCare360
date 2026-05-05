package com.cardiocare360.service;

import com.cardiocare360.model.entity.Referto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RefertoService {

    Referto creaReferto(Long pazienteId,
                        Long medicoId,
                        String titolo,
                        String descrizione,
                        String diagnosi,
                        MultipartFile file);

    Referto getRefertoById(Long id);

    List<Referto> getRefertiPaziente(Long pazienteId);

    List<Referto> getRefertiMedico(Long medicoId);

    byte[] downloadFile(Long refertoId);
}
