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
public class InmuebleDTO {
    private Integer id;
    private String numero;
    private BigDecimal valorArriendo;
    private Integer idArrendatario;
    private String nombreArrendatario;
    private Boolean estado;
    private Boolean ocupado; // true si tiene arrendatario
}