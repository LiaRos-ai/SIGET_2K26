package com.tallerpw.gestiontareas.model;

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
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tarea {

    private Long id;
    private String titulo;
    private boolean completada;

}
