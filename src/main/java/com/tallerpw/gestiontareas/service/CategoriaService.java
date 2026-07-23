package com.tallerpw.gestiontareas.service;

import com.tallerpw.gestiontareas.model.Categoria;
import com.tallerpw.gestiontareas.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Día 8: se agrega esta capa Service para Categoria, en vez de que
 * TareaService inyecte CategoriaRepository directamente. Mantiene la
 * misma regla del Día 2: un Service puede depender de otro Service,
 * pero un Service nunca debería saltarse su propio Repository ni el de
 * otra entidad para ir directo a la base de datos.
 */
@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Autowired
    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    public Optional<Categoria> buscarPorId(Long id) {
        return categoriaRepository.findById(id);
    }

}
