package com.cardiocare360.service;

import com.cardiocare360.model.response.RefertoDTO;
import org.springframework.web.multipart.MultipartFile;

public interface RefertoService {

    RefertoDTO uploadReferto(Long esameId,
                             Long medicoId,
                             String noteMedico,
                             MultipartFile file);

    RefertoDTO getRefertoByEsame(Long esameId);

    byte[] downloadFile(Long refertoId);
}
