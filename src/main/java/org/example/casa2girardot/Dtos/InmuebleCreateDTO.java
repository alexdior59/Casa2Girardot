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
public class InmuebleCreateDTO {

    @NotBlank(message = "El número del inmueble es obligatorio")
    private String numero;

    @NotNull(message = "El valor del arriendo es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El valor debe ser mayor a 0")
    private BigDecimal valorArriendo;

    private Boolean estado;
}