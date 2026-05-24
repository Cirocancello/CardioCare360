package com.cardiocare360.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DisponibilitaEsameResponse {
    private String data; // formato YYYY-MM-DD
    private String ora;  // formato HH:MM
}
