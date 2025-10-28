package org.example.casa2girardot.Dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistroTenenciaCreateDTO {
    private Integer idArrendatario;
    private Integer idInmueble;
    private LocalDate fechaInicio;
    private LocalDate fechaFinal;
}