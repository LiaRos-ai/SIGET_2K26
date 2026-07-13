package com.tallerpw.gestiontareas.repository;

import com.tallerpw.gestiontareas.model.Tarea;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Implementación EN MEMORIA de TareaRepository.
 *
 * La anotación @Repository le indica a Spring que esta clase pertenece a
 * la capa de acceso a datos, la registra como Bean, y habilita la
 * traducción automática de excepciones de persistencia (útil ya desde
 * ahora, aunque su verdadero beneficio se nota a partir del Día 7 con JPA).
 *
 * IMPORTANTE: los datos se pierden al reiniciar la aplicación. Esto es
 * intencional para el Día 2: el objetivo es entender las capas y las
 * anotaciones, no la persistencia real todavía.
 */
@Repository
public class TareaRepositoryEnMemoria implements TareaRepository {

    private final ConcurrentHashMap<Long, Tarea> datos = new ConcurrentHashMap<>();
    private final AtomicLong contadorId = new AtomicLong(0);

    @Override
    public List<Tarea> findAll() {
        return List.copyOf(datos.values());
    }

    @Override
    public Optional<Tarea> findById(Long id) {
        return Optional.ofNullable(datos.get(id));
    }

    @Override
    public Tarea save(Tarea tarea) {
        if (tarea.getId() == null) {
            tarea.setId(contadorId.incrementAndGet());
        }
        datos.put(tarea.getId(), tarea);
        return tarea;
    }

}
