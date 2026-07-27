package com.tallerpw.gestiontareas.service;

import com.tallerpw.gestiontareas.model.Categoria;
import com.tallerpw.gestiontareas.model.Tarea;
import com.tallerpw.gestiontareas.repository.CategoriaRepository;
import com.tallerpw.gestiontareas.repository.TareaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Día 8: se agrega esta capa Service para Categoria, en vez de que
 * TareaService inyecte CategoriaRepository directamente. Mantiene la
 * misma regla del Día 2: un Service puede depender de otro Service,
 * pero un Service nunca debería saltarse su propio Repository ni el de
 * otra entidad para ir directo a la base de datos.
 *
 * Día 12: EXCEPCIÓN DOCUMENTADA a esa regla. eliminarConTareas necesita
 * reasignar las tareas de la categoría antes de borrarla, así que en
 * principio debería pedírselo a TareaService. Pero TareaService YA
 * depende de CategoriaService (para resolver la categoría al crear o
 * editar una tarea) — si CategoriaService dependiera también de
 * TareaService, Spring no podría resolver la dependencia circular al
 * arrancar la aplicación ("requested bean is currently in creation").
 * Por eso, para esta única operación, se inyecta TareaRepository
 * directamente. Es un trade-off real y consciente, no un descuido.
 */
@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final TareaRepository tareaRepository;

    @Autowired
    public CategoriaService(CategoriaRepository categoriaRepository, TareaRepository tareaRepository) {
        this.categoriaRepository = categoriaRepository;
        this.tareaRepository = tareaRepository;
    }

    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    public Optional<Categoria> buscarPorId(Long id) {
        return categoriaRepository.findById(id);
    }

    /**
     * Día 12: ejemplo real de @Transactional. Esta operación tiene DOS
     * pasos que deben ocurrir juntos o no ocurrir ninguno:
     *   1. Desvincular todas las tareas de esta categoría (categoria = null).
     *   2. Recién entonces, borrar la categoría.
     *
     * Sin @Transactional, si el paso 2 fallara por cualquier motivo
     * (por ejemplo, un error de conexión momentáneo), las tareas ya
     * habrían quedado desvinculadas pero la categoría seguiría
     * existiendo — un estado a medio camino, inconsistente. Con
     * @Transactional, Spring envuelve ambos pasos en una única
     * transacción de base de datos: si algo falla en el medio, TODO se
     * revierte (rollback), como si nunca hubiera pasado nada.
     */
    @Transactional
    public void eliminarConTareas(Long categoriaId) {
        List<Tarea> tareasDeLaCategoria = tareaRepository.findByCategoriaId(categoriaId);
        for (Tarea tarea : tareasDeLaCategoria) {
            tarea.setCategoria(null);
        }
        tareaRepository.saveAll(tareasDeLaCategoria);

        categoriaRepository.deleteById(categoriaId);
    }

}
