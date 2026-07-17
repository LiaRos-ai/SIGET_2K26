package com.tallerpw.gestiontareas.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Modelo de dominio "Tarea".
 *
 * Día 2: es un POJO simple (sin anotaciones JPA todavía).
 * Las anotaciones @Entity, @Id, @GeneratedValue se agregan recién el
 * Día 7 ("Persistencia con Spring Data JPA"), cuando esta clase pase a
 * mapearse contra una tabla real en MySQL/PostgreSQL.
 *
 * Día 5: se agregan anotaciones de Bean Validation (@NotBlank, @Size).
 * Estas reglas se validan automáticamente cuando el Controller recibe
 * un objeto Tarea con @Valid (ver TareaController.crear).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tarea {

    private Long id;

    @NotBlank(message = "El título es obligatorio")
    @Size(min = 3, max = 100, message = "El título debe tener entre 3 y 100 caracteres")
    private String titulo;

    private boolean completada;

}