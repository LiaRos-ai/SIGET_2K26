package com.tallerpw.gestiontareas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Modelo de dominio "Tarea".
 *
 * Día 2: nace como un POJO simple (sin anotaciones JPA).
 * Día 5: se agregan anotaciones de Bean Validation (@NotBlank, @Size).
 *
 * Día 7: se convierte en una entidad JPA real:
 *   - @Entity + @Table: la marca como mapeada contra la tabla "tareas".
 *   - @Id + @GeneratedValue: clave primaria autogenerada por la base de datos.
 *   - @ManyToOne: cada tarea pertenece (opcionalmente) a una Categoria.
 *
 * TareaRepositoryEnMemoria (Días 2-6) queda retirada: a partir de hoy,
 * TareaRepository extiende JpaRepository y Spring Data JPA genera la
 * implementación real contra la base de datos.
 */
@Entity
@Table(name = "tareas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El título es obligatorio")
    @Size(min = 3, max = 100, message = "El título debe tener entre 3 y 100 caracteres")
    private String titulo;

    private boolean completada;

    /**
     * @ManyToOne es EAGER por defecto (a diferencia de @OneToMany, que es
     * LAZY por defecto) — por eso no hace falta preocuparse acá por
     * LazyInitializationException al acceder a tarea.getCategoria().
     *
     * @ToString.Exclude y @EqualsAndHashCode.Exclude evitan que el
     * toString/equals de Tarea entre en un ciclo con el de Categoria
     * (ver el comentario en esa clase).
     */
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Categoria categoria;

}

