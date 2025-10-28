package org.example.casa2girardot.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "facturas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "numero_factura", unique = true, nullable = false, length = 50)
    private String numeroFactura;

    @Column(name = "monto", nullable = false, precision = 12, scale = 2)
    private BigDecimal monto;

    @Column(name = "mora", precision = 12, scale = 2)
    private BigDecimal mora;

    @Column(name = "total", precision = 12, scale = 2)
    private BigDecimal total;

    @Column(name = "fecha_generacion", nullable = false)
    private LocalDateTime fechaGeneracion;

    @Column(name = "fecha_cobro", nullable = false)
    private LocalDate fechaCobro;

    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;

    @Column(name = "fecha_pago")
    private LocalDate fechaPago;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_arrendatario", nullable = false)
    private Arrendatario idArrendatario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_inmueble", nullable = false)
    private Inmueble idInmueble;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estado_factura", nullable = false)
    private EstadoFactura estadoFactura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_medio_pago")
    private MedioPago medioPago;

    @Column(name = "observaciones", length = 500)
    private String observaciones;

    @Column(name = "mes", nullable = false)
    private Integer mes;

    @Column(name = "anio", nullable = false)
    private Integer anio;

    @Column(name = "comprobante_pago", length = 255)
    private String comprobantePago;
}