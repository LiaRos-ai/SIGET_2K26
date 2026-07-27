package com.tallerpw.gestiontareas.service;

import com.tallerpw.gestiontareas.model.Categoria;
import com.tallerpw.gestiontareas.model.Tarea;
import com.tallerpw.gestiontareas.model.Usuario;
import com.tallerpw.gestiontareas.repository.TareaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Capa Service: contiene las reglas de negocio y coordina el acceso a
 * datos a través del repositorio. El Controller NUNCA debería hablar
 * directamente con el Repository; siempre pasa por aquí.
 *
 * Día 8: se agrega CategoriaService como segunda dependencia.
 * Día 10: el listado y las operaciones de escritura ahora tienen en
 * cuenta al Usuario dueño de cada tarea (HU-07).
 * Día 13: se agrega logging con SLF4J en las operaciones de escritura
 * y en los intentos de gestionar una tarea sin permiso (log.warn —
 * útil para detectar patrones sospechosos, sin ser necesariamente un
 * error del sistema).
 */
@Service
public class TareaService {

    private static final Logger log = LoggerFactory.getLogger(TareaService.class);

    private final TareaRepository tareaRepository;
    private final CategoriaService categoriaService;

    @Autowired
    public TareaService(TareaRepository tareaRepository, CategoriaService categoriaService) {
        this.tareaRepository = tareaRepository;
        this.categoriaService = categoriaService;
    }

    public List<Tarea> listarTodas() {
        return tareaRepository.findAll();
    }

    /**
     * Día 6: filtro opcional por estado.
     * Día 10: además, si "propietario" no es null, solo devuelve las
     * tareas de ESE usuario (caso normal); si es null, devuelve de
     * TODOS los usuarios (caso ADMIN — ver TareaController.listar).
     */
    public List<Tarea> listarFiltradas(Boolean completada, Usuario propietario) {
        if (propietario == null) {
            // Camino ADMIN: ve todas las tareas, de cualquier usuario.
            return completada == null ? tareaRepository.findAll() : tareaRepository.findByCompletada(completada);
        }
        // Camino usuario normal: solo sus propias tareas.
        return completada == null
                ? tareaRepository.findByPropietario(propietario)
                : tareaRepository.findByPropietarioAndCompletada(propietario, completada);
    }

    public Optional<Tarea> buscarPorId(Long id) {
        return tareaRepository.findById(id);
    }

    /**
     * Día 7: usado directamente por DataInitializer, que ya tiene los
     * objetos Categoria y Usuario a mano (no necesita buscarlos por id).
     */
    public Tarea crear(String titulo, Categoria categoria, Usuario propietario) {
        Tarea nueva = new Tarea(null, titulo, false, categoria, propietario);
        return tareaRepository.save(nueva);
    }

    /**
     * Día 8: usado por el Controller cuando llega el formulario de
     * creación. Día 10: además recibe el Usuario autenticado, para
     * asignarlo automáticamente como propietario — el usuario NUNCA
     * elige de quién es la tarea desde el formulario.
     */
    public Tarea crearDesdeFormulario(String titulo, Long categoriaId, Usuario propietario) {
        Categoria categoria = resolverCategoria(categoriaId);
        return crear(titulo, categoria, propietario);
    }

    /**
     * Día 8: actualizar una tarea existente.
     * Día 10: solo se aplica si quienActualiza puede gestionar la tarea
     * (ver puedeGestionar). Si no puede, devuelve Optional.empty() como
     * si la tarea no existiera — una forma simple de no revelar si la
     * tarea existe o no cuando no es su dueño (se profundiza el manejo
     * de errores más prolijo el Día 13).
     */
    public Optional<Tarea> actualizarDesdeFormulario(Long id, String titulo, Long categoriaId, Usuario quienActualiza) {
        return tareaRepository.findById(id)
                .filter(tarea -> puedeGestionar(tarea, quienActualiza))
                .map(tarea -> {
                    tarea.setTitulo(titulo);
                    tarea.setCategoria(resolverCategoria(categoriaId));
                    return tareaRepository.save(tarea);
                });
    }

    /**
     * Día 8: eliminar. Día 10: mismo criterio de permiso que actualizar.
     * Devuelve true si efectivamente se eliminó, false si no tenía permiso
     * o la tarea no existía.
     */
    public boolean eliminar(Long id, Usuario quienElimina) {
        return tareaRepository.findById(id)
                .map(tarea -> {
                    if (!puedeGestionar(tarea, quienElimina)) {
                        log.warn("Intento de eliminar la tarea {} sin permiso (usuario id={})",
                                id, quienElimina != null ? quienElimina.getId() : null);
                        return false;
                    }
                    tareaRepository.deleteById(id);
                    log.info("Tarea eliminada: id={}", id);
                    return true;
                })
                .orElse(false);
    }

    /**
     * Día 6: alterna el estado completada/pendiente.
     * Día 10: mismo criterio de permiso que actualizar/eliminar.
     */
    public Optional<Tarea> alternarCompletada(Long id, Usuario quienAlterna) {
        return tareaRepository.findById(id)
                .filter(tarea -> puedeGestionar(tarea, quienAlterna))
                .map(tarea -> {
                    tarea.setCompletada(!tarea.isCompletada());
                    return tareaRepository.save(tarea);
                });
    }

    /**
     * Día 10: regla de autorización a nivel de datos (más allá de la
     * autorización a nivel de ruta que ya hace SecurityConfig): un
     * usuario puede gestionar una tarea si es ADMIN, o si es el dueño.
     * Las tareas sembradas antes del Día 9 (sin propietario) solo las
     * puede gestionar un ADMIN.
     */
    private boolean puedeGestionar(Tarea tarea, Usuario usuario) {
        if (usuario == null) {
            return false;
        }
        if ("ADMIN".equals(usuario.getRol())) {
            return true;
        }
        return tarea.getPropietario() != null && tarea.getPropietario().getId().equals(usuario.getId());
    }

    // ---------------------------------------------------------------
    // Día 11: métodos para la API REST (TareaRestController).
    //
    // Día 12: /api/** ya requiere autenticación (HTTP Basic), pero estos
    // métodos TODAVÍA no aplican puedeGestionar: cualquier usuario
    // autenticado en la API puede crear/editar/eliminar CUALQUIER tarea,
    // no solo las propias. Es una limitación real y conocida, distinta
    // de la de HU-07 en las vistas web — queda como ejercicio para
    // quien quiera profundizar: pasar el Usuario autenticado también
    // acá (con @AuthenticationPrincipal) y aplicar el mismo
    // puedeGestionar que ya usan crearDesdeFormulario/eliminar/etc.
    // ---------------------------------------------------------------

    public Tarea crearDesdeApi(String titulo, Long categoriaId) {
        Categoria categoria = resolverCategoria(categoriaId);
        Tarea creada = crear(titulo, categoria, null);
        log.info("Tarea creada vía API: id={}", creada.getId());
        return creada;
    }

    public Optional<Tarea> actualizarDesdeApi(Long id, String titulo, Long categoriaId) {
        return tareaRepository.findById(id).map(tarea -> {
            tarea.setTitulo(titulo);
            tarea.setCategoria(resolverCategoria(categoriaId));
            return tareaRepository.save(tarea);
        });
    }

    public boolean eliminarDesdeApi(Long id) {
        if (!tareaRepository.existsById(id)) {
            return false;
        }
        tareaRepository.deleteById(id);
        return true;
    }

    private Categoria resolverCategoria(Long categoriaId) {
        if (categoriaId == null) {
            return null;
        }
        return categoriaService.buscarPorId(categoriaId).orElse(null);
    }

}
