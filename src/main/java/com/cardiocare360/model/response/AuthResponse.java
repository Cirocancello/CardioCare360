//Cosa rappresenta: la risposta del backend dopo login o registrazione.

package com.cardiocare360.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {

    private String token;
    private String ruolo;
    private Long idUtente;
}
