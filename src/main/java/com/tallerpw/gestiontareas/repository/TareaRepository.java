package com.tallerpw.gestiontareas.repository;

import com.tallerpw.gestiontareas.model.Tarea;
import com.tallerpw.gestiontareas.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Día 2-6: esta interfaz declaraba findAll/findById/save a mano, e
 * implementábamos TareaRepositoryEnMemoria nosotros mismos.
 *
 * Día 7: al extender JpaRepository<Tarea, Long>, Spring Data JPA genera
 * automáticamente la implementación real (findAll, findById, save,
 * deleteById, etc.) contra la base de datos configurada en
 * application.properties.
 *
 * findByCompletada, findByPropietario y findByPropietarioAndCompletada
 * son "query methods": Spring Data JPA interpreta el nombre del método y
 * genera automáticamente la consulta SQL/JPQL equivalente.
 *
 * Día 10: se agregan los query methods con "Propietario" para poder
 * filtrar el listado por dueño (HU-07: cada usuario ve solo sus tareas,
 * salvo que sea ADMIN).
 *
 * Día 12: findByCategoriaId navega la relación anidada
 * Tarea.categoria.id — Spring Data JPA entiende "CategoriaId" como
 * "la propiedad id del objeto categoria", sin necesidad de escribir
 * ninguna consulta JPQL a mano.
 */
 /**Día 12: nota de seguridad — ninguno de estos query methods (ni
 * JpaRepository en general) es vulnerable a inyección SQL. Spring Data
 * JPA SIEMPRE genera consultas parametrizadas por debajo (con
 * placeholders "?", nunca concatenando el valor recibido directo en el
 * texto de la consulta), sin importar qué le pasemos como argumento.
 * Esto es válido tanto para estos query methods como para JPQL/HQL
 * escrito a mano con @Query, siempre que se usen parámetros
 * (:parametro o ?1) en vez de concatenar Strings.
 */
public interface TareaRepository extends JpaRepository<Tarea, Long> {

    List<Tarea> findByCompletada(boolean completada);

    List<Tarea> findByPropietario(Usuario propietario);

    List<Tarea> findByPropietarioAndCompletada(Usuario propietario, boolean completada);

    List<Tarea> findByCategoriaId(Long categoriaId);

}
