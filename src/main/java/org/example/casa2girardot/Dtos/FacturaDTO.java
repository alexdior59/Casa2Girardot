package org.example.casa2girardot.Dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FacturaDTO {
    private Integer id;
    private String numeroFactura;
    private BigDecimal monto;
    private BigDecimal mora;
    private BigDecimal total;
    private LocalDate fechaGeneracion;
    private LocalDate fechaVencimiento;
    private LocalDate fechaPago;
    private String nombreArrendatario;
    private String numeroInmueble;
    private String estado;
    private String medioPagoNombre;
    private String comprobantePago;
    private String observaciones;
}