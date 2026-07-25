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
 */
public interface TareaRepository extends JpaRepository<Tarea, Long> {

    List<Tarea> findByCompletada(boolean completada);

    List<Tarea> findByPropietario(Usuario propietario);

    List<Tarea> findByPropietarioAndCompletada(Usuario propietario, boolean completada);

}
