package com.tallerpw.gestiontareas.repository;

import com.tallerpw.gestiontareas.model.Tarea;

import java.util.List;
import java.util.Optional;

/**
 * Contrato de la capa Repository.
 *
 * Día 2: se define la interfaz para que el resto de las capas (Service,
 * Controller) ya puedan programar "contra la interfaz" y no contra una
 * implementación concreta. La implementación de hoy es en memoria
 * (ver TareaRepositoryEnMemoria).
 *
 * Día 7: esta interfaz se reemplaza por una que extiende
 * JpaRepository<Tarea, Long>, y Spring Data JPA genera la implementación
 * automáticamente contra la base de datos real. El resto del código
 * (Service y Controller) NO debería tener que cambiar gracias a esta
 * separación por capas.
 */
public interface TareaRepository {

    List<Tarea> findAll();

    Optional<Tarea> findById(Long id);

    Tarea save(Tarea tarea);

}
