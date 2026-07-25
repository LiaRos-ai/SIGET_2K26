package com.tallerpw.gestiontareas.service;

import com.tallerpw.gestiontareas.model.Categoria;
import com.tallerpw.gestiontareas.model.Tarea;
import com.tallerpw.gestiontareas.model.Usuario;
import com.tallerpw.gestiontareas.repository.TareaRepository;
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
 */
@Service
public class TareaService {

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
                .filter(tarea -> puedeGestionar(tarea, quienElimina))
                .map(tarea -> {
                    tareaRepository.deleteById(id);
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

    private Categoria resolverCategoria(Long categoriaId) {
        if (categoriaId == null) {
            return null;
        }
        return categoriaService.buscarPorId(categoriaId).orElse(null);
    }

}
