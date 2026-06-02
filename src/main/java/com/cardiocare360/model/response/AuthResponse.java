package com.cardiocare360.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    private String token;
    private String ruolo;
    private Long idUtente;
    private Long idPaziente; 
    private Long idMedico;   
}
