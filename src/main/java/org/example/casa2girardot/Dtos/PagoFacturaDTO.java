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
public class PagoFacturaDTO {
    private Integer idMedioPago;
    private LocalDate fechaPago;
    private String comprobante;
    private String observaciones;
}