package org.example.casa2girardot.Dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArrendatarioCreateDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Size(min = 10, message = "El celular debe tener al menos 10 dígitos")
    private String celular;

    @NotBlank(message = "La cédula es obligatoria")
    private String cedula;

    @Email(message = "Debe ser un email válido")
    @NotBlank(message = "El email es obligatorio")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener mínimo 6 caracteres")
    private String password;

    private Boolean estado;
}