package org.example.casa2girardot.Dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AuthResponseDTO {
    private String token;
    private String tipoUsuario;
    private String email;
}