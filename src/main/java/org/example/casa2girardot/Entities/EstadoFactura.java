package org.example.casa2girardot.Entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "estados_factura")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadoFactura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nombre", nullable = false, unique = true, length = 50)
    private String nombre;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @Column(name = "codigo", unique = true, length = 20)
    private String codigo;
}