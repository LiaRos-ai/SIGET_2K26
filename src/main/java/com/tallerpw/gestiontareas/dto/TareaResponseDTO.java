package com.tallerpw.gestiontareas.dto;

import com.tallerpw.gestiontareas.model.Tarea;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de salida para la API REST (Día 11).
 *
 * ¿Por qué no devolver la entidad Tarea directamente desde el
 * @RestController? Varias razones, todas ya conocidas de otros
 * contextos del curso:
 *
 *   1. Jackson (el serializador JSON que usa Spring Boot) intentaría
 *      convertir también "categoria" y "propietario", arrastrando toda
 *      la entidad Categoria/Usuario — incluida la lista tareas de
 *      Categoria (LAZY) y, peor, el hash de la contraseña de Usuario.
 *   2. El mismo problema de recursión que vimos con Lombok @Data en
 *      relaciones bidireccionales (Día 7) puede reaparecer en la
 *      serialización JSON.
 *   3. El "contrato" de la API (qué campos expone) queda desacoplado de
 *      cómo está modelada la base de datos por dentro — se puede
 *      cambiar la entidad sin romper a los consumidores de la API.
 *
 * Por eso: un DTO plano y explícito, igual que TareaFormDTO para los
 * formularios HTML.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TareaResponseDTO {

    private Long id;
    private String titulo;
    private boolean completada;
    private String categoria;
    private String propietario;

    /**
     * Método de mapeo Tarea -> TareaResponseDTO. Se podría usar una
     * librería como MapStruct para esto en un proyecto más grande, pero
     * a esta escala un método estático simple es perfectamente razonable.
     */
    public static TareaResponseDTO desde(Tarea tarea) {
        String nombreCategoria = tarea.getCategoria() != null ? tarea.getCategoria().getNombre() : null;
        String nombrePropietario = tarea.getPropietario() != null ? tarea.getPropietario().getNombre() : null;

        return new TareaResponseDTO(
                tarea.getId(),
                tarea.getTitulo(),
                tarea.isCompletada(),
                nombreCategoria,
                nombrePropietario
        );
    }

}
