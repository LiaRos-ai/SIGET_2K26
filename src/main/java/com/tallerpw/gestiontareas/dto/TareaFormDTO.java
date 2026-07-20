package com.tallerpw.gestiontareas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO (Data Transfer Object) para el formulario de creación de tareas.
 *
 * Día 6: ¿por qué no seguir bindeando directamente contra Tarea, como
 * hacíamos desde el Día 5?
 *
 * PROBLEMA (over-posting / mass assignment): si el formulario bindea
 * directamente contra la entidad Tarea, un usuario malicioso podría
 * agregar a mano campos ocultos en la petición HTTP (por ejemplo
 * completada=true o id=999) que el formulario visible no expone, y
 * Spring los asignaría igual al objeto Tarea. Bindear contra un DTO que
 * SOLO tiene los campos que realmente queremos permitir desde el
 * formulario elimina esa posibilidad: "completada" e "id" ni siquiera
 * existen en esta clase.
 *
 * Este mismo patrón se vuelve todavía más importante cuando Tarea pase a
 * ser una entidad JPA (Día 7) y, más adelante, cuando se expongan
 * endpoints REST (Día 11).
 */
@Data
public class TareaFormDTO {

    @NotBlank(message = "El título es obligatorio")
    @Size(min = 3, max = 100, message = "El título debe tener entre 3 y 100 caracteres")
    private String titulo;

}
