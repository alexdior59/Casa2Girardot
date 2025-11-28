package org.example.casa2girardot.Dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank(message = "El tipo de movimiento (INGRESO/EGRESO) es obligatorio")
    private String tipoMovimiento;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El monto debe ser mayor a 0")
    private BigDecimal monto;

    @NotNull(message = "El medio de pago es obligatorio")
    private Integer idMedioPago;

    private Integer idInmueble;

    @NotBlank(message = "El concepto es obligatorio")
    private String concepto;

    private String descripcion;
    private String numeroReferencia;
}