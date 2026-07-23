package com.tallerpw.gestiontareas.service;

import com.tallerpw.gestiontareas.model.Categoria;
import com.tallerpw.gestiontareas.model.Tarea;
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
 * La inyección de dependencias se hace por constructor (buena práctica
 * recomendada por Spring). @Autowired en el constructor es opcional desde
 * Spring 4.3 si la clase tiene un único constructor, pero se deja explícito
 * en el Día 2 para que quede claro cómo funciona la inyección.
 *
 * Día 8: se agrega CategoriaService como segunda dependencia, para
 * resolver el id de categoría que llega desde el formulario (el
 * Controller nunca habla directamente con CategoriaRepository).
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
     * Día 6: usado por GET /tareas?completada=true|false.
     * Día 7: en vez de filtrar a mano con streams, se delega en el query
     * method findByCompletada, generado automáticamente por Spring Data
     * JPA a partir del nombre del método declarado en TareaRepository.
     */
    public List<Tarea> listarFiltradas(Boolean completada) {
        if (completada == null) {
            return tareaRepository.findAll();
        }
        return tareaRepository.findByCompletada(completada);
    }

    public Optional<Tarea> buscarPorId(Long id) {
        return tareaRepository.findById(id);
    }

    /**
     * Día 7: usado directamente por DataInitializer, que ya tiene el
     * objeto Categoria a mano (no necesita buscarlo por id).
     */
    public Tarea crear(String titulo, Categoria categoria) {
        Tarea nueva = new Tarea(null, titulo, false, categoria);
        return tareaRepository.save(nueva);
    }

    /**
     * Día 8: usado por el Controller cuando llega el formulario de
     * creación. A diferencia del método anterior, acá solo tenemos el ID
     * de la categoría elegida en el <select>, así que hay que resolverlo
     * a través de CategoriaService antes de armar la Tarea.
     */
    public Tarea crearDesdeFormulario(String titulo, Long categoriaId) {
        Categoria categoria = resolverCategoria(categoriaId);
        return crear(titulo, categoria);
    }

    /**
     * Día 8: CRUD completo — actualizar una tarea existente (título y
     * categoría) a partir de los datos del formulario de edición.
     */
    public Optional<Tarea> actualizarDesdeFormulario(Long id, String titulo, Long categoriaId) {
        return tareaRepository.findById(id).map(tarea -> {
            tarea.setTitulo(titulo);
            tarea.setCategoria(resolverCategoria(categoriaId));
            return tareaRepository.save(tarea);
        });
    }

    /**
     * Día 8: CRUD completo — eliminar una tarea. deleteById no lanza
     * error si el id no existe (a diferencia de delete(entidad), que sí
     * lo haría), lo cual es cómodo para este caso de uso simple.
     */
    public void eliminar(Long id) {
        tareaRepository.deleteById(id);
    }

    /**
     * Día 6: alterna el estado completada/pendiente de una tarea existente.
     */
    public Optional<Tarea> alternarCompletada(Long id) {
        return tareaRepository.findById(id).map(tarea -> {
            tarea.setCompletada(!tarea.isCompletada());
            return tareaRepository.save(tarea);
        });
    }

    private Categoria resolverCategoria(Long categoriaId) {
        if (categoriaId == null) {
            return null;
        }
        return categoriaService.buscarPorId(categoriaId).orElse(null);
    }

}
