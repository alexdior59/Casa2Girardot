package org.example.casa2girardot.Dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArrendatarioDTO {
    private Integer id;
    private String nombre;
    private String celular;
    private String cedula;
    private String email;
    private Boolean estado;
}