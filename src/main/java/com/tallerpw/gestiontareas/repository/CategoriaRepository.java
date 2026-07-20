package com.tallerpw.gestiontareas.repository;

import com.tallerpw.gestiontareas.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio de Categoria. Por ahora no necesita métodos propios: los
 * heredados de JpaRepository (findAll, findById, save, deleteById...)
 * alcanzan para lo que se usa en el proyecto guía hasta el Día 8.
 */
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
