package com.tallerpw.gestiontareas.service;

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
 */
@Service
public class TareaService {

    private final TareaRepository tareaRepository;

    @Autowired
    public TareaService(TareaRepository tareaRepository) {
        this.tareaRepository = tareaRepository;
    }

    public List<Tarea> listarTodas() {
        return tareaRepository.findAll();
    }

    public Optional<Tarea> buscarPorId(Long id) {
        return tareaRepository.findById(id);
    }

    public Tarea crear(String titulo) {
        Tarea nueva = new Tarea(null, titulo, false);
        return tareaRepository.save(nueva);
    }

}
