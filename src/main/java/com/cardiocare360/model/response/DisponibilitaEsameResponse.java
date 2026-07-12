package com.cardiocare360.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DisponibilitaEsameResponse {

    private String data; // formato YYYY-MM-DD
    private String ora;  // formato HH:MM

    // ---------------------------------------------------------
    // VALIDAZIONI DI SICUREZZA
    // ---------------------------------------------------------

    public boolean isValid() {
        return isDataValida() && isOraValida();
    }

    public boolean isDataValida() {
        return data != null && !data.isBlank();
    }

    public boolean isOraValida() {
        return ora != null && !ora.isBlank();
    }
}
