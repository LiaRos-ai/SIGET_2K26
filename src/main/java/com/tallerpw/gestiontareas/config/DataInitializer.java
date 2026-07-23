package com.tallerpw.gestiontareas.config;

import com.tallerpw.gestiontareas.model.Categoria;
import com.tallerpw.gestiontareas.repository.CategoriaRepository;
import com.tallerpw.gestiontareas.service.TareaService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

/**
 * Carga datos de ejemplo al arrancar la aplicación.
 *
 * Día 7: ahora también crea un par de Categorias y se las asigna a las
 * tareas de ejemplo, para tener datos reales de la relación
 * @ManyToOne/@OneToMany desde el primer arranque contra la base de datos.
 */
@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner cargarDatosDePrueba(TareaService tareaService,
                                                  CategoriaRepository categoriaRepository) {
        return args -> {
            Categoria curso = categoriaRepository.save(new Categoria(null, "Curso", new ArrayList<>()));
            Categoria personal = categoriaRepository.save(new Categoria(null, "Personal", new ArrayList<>()));

            var t1 = tareaService.crear("Configurar el repositorio Git del proyecto guía", curso);
            tareaService.crear("Explicar @SpringBootApplication, @Controller, @Service y @Repository", curso);
            tareaService.crear("Actualizar el tablero Kanban con el avance del Sprint 0", personal);

            tareaService.alternarCompletada(t1.getId());
        };
    }

}
