package com.tallerpw.gestiontareas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad JPA "Categoria". Se agrega el Día 7 junto con Tarea, para
 * poder mostrar una relación real @OneToMany / @ManyToOne (una
 * categoría tiene muchas tareas; una tarea pertenece a una categoría).
 *
 * OJO con Lombok y relaciones bidireccionales: a propósito NO se usa
 * @Data acá (que generaría toString/equals/hashCode incluyendo la lista
 * "tareas"). Si Tarea también incluyera "categoria" en su toString, y
 * Categoria incluyera "tareas" en el suyo, se formaría una recursión
 * infinita (StackOverflowError) apenas alguien llame a toString() o
 * intente loguear un objeto. Por eso acá se usa @Getter/@Setter en vez
 * de @Data, y en Tarea se excluye "categoria" del toString/equals.
 */
@Entity
@Table(name = "categorias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la categoría es obligatorio")
    private String nombre;

    /**
     * mappedBy = "categoria" indica que la relación ya está mapeada del
     * otro lado (en Tarea.categoria) — esta es la parte "inversa" de la
     * relación, no genera una columna nueva en la tabla "categorias".
     */
    @OneToMany(mappedBy = "categoria")
    private List<Tarea> tareas = new ArrayList<>();

}
