package com.tallerpw.gestiontareas.repository;

import com.tallerpw.gestiontareas.model.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Día 2-6: esta interfaz declaraba findAll/findById/save a mano, e
 * implementábamos TareaRepositoryEnMemoria nosotros mismos.
 *
 * Día 7: al extender JpaRepository<Tarea, Long>, Spring Data JPA genera
 * automáticamente la implementación real (findAll, findById, save,
 * deleteById, etc.) contra la base de datos configurada en
 * application.properties. Ya no hace falta escribir ninguna clase de
 * implementación: Spring crea un proxy en tiempo de ejecución.
 *
 * findByCompletada es un "query method": Spring Data JPA interpreta el
 * nombre del método y genera automáticamente la consulta SQL/JPQL
 * equivalente a "SELECT * FROM tareas WHERE completada = ?".
 */
public interface TareaRepository extends JpaRepository<Tarea, Long> {

    List<Tarea> findByCompletada(boolean completada);

}
