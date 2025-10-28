package org.example.casa2girardot.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "registro_tenencias")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistroTenencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_arrendatario", nullable = false)
    private Arrendatario idArrendatario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_inmueble", nullable = false)
    private Inmueble idInmueble;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_final")
    private LocalDate fechaFinal;
}