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
public class RegistroTenenciaDTO {
    private Integer id;
    private Integer idArrendatario;
    private String nombreArrendatario;
    private String cedulaArrendatario;
    private Integer idInmueble;
    private String numeroInmueble;
    private LocalDate fechaInicio;
    private LocalDate fechaFinal;
    private Boolean activo; // true si fechaFinal es null
    private Long diasTenencia; // Duración en días
}