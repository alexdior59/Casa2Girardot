package org.example.casa2girardot.Dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NovedadCreateDTO {
    private String tipoMovimiento;
    private BigDecimal monto;
    private Integer idMedioPago;
    private Integer idInmueble;
    private Integer idAdministrador;
    private String concepto;
    private String descripcion;
    private String numeroReferencia;
}