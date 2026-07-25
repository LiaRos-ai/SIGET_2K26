package com.tallerpw.gestiontareas.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO del formulario de registro. Igual que TareaFormDTO (Día 6), evita
 * bindear directamente contra la entidad Usuario: acá es todavía más
 * importante, porque Usuario tiene un campo "rol" que un usuario nunca
 * debería poder autoasignarse (por ejemplo, registrarse como "ADMIN"
 * agregando ese campo a mano en el formulario).
 */
@Data
public class RegistroFormDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no tiene un formato válido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

}
