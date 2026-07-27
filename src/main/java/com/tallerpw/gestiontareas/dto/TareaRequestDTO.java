package com.tallerpw.gestiontareas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO de entrada para la API REST (Día 11): la forma que debe tener el
 * JSON del body en POST /api/tareas y PUT /api/tareas/{id}.
 *
 * Las mismas anotaciones de Bean Validation del Día 5 (@NotBlank, @Size)
 * funcionan igual acá: @Valid + @RequestBody las aplica automáticamente
 * sobre el JSON deserializado, exactamente como @Valid + @ModelAttribute
 * las aplicaba sobre un formulario HTML.
 */
@Data
public class TareaRequestDTO {

    @NotBlank(message = "El título es obligatorio")
    @Size(min = 3, max = 100, message = "El título debe tener entre 3 y 100 caracteres")
    private String titulo;

    private Long categoriaId;

}
