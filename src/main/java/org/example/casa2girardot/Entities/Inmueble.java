package org.example.casa2girardot.Entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inmuebles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inmueble {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "numero", nullable = false, length = 50)
    private String numero;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_arrendatario")
    private Arrendatario idArrendatario;

    @Column(name = "estado", nullable = false)
    private Boolean estado;
}