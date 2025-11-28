package org.example.casa2girardot.Dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagoFacturaDTO {

    @NotNull(message = "Debe seleccionar un medio de pago")
    private Integer idMedioPago;

    private LocalDate fechaPago;
    private String comprobante;
    private String observaciones;
}