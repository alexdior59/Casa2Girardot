package org.example.casa2girardot.Dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NovedadDTO {
    private Integer id;
    private String tipoMovimiento;
    private BigDecimal monto;
    private String medioPagoNombre;
    private String numeroInmueble;
    private String nombreAdministrador;
    private String concepto;
    private String descripcion;
    private LocalDateTime fecha;
    private String numeroReferencia;
}