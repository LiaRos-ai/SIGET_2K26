package com.tallerpw.gestiontareas.config;

import com.tallerpw.gestiontareas.model.Categoria;
import com.tallerpw.gestiontareas.repository.CategoriaRepository;
import com.tallerpw.gestiontareas.service.TareaService;
import com.tallerpw.gestiontareas.service.UsuarioService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

/**
 * Carga datos de ejemplo al arrancar la aplicación.
 *
 * Día 7: crea un par de Categorias y las asigna a las tareas de ejemplo.
 *
 * Día 9: también crea dos cuentas de ejemplo (un ADMIN y un USER), para
 * poder probar el login sin tener que registrarse a mano la primera vez.
 * A diferencia de las tareas/categorías de ejemplo, acá SÍ hace falta
 * verificar que el email no exista todavía antes de crear: la columna
 * email tiene una restricción UNIQUE, así que insertar el mismo usuario
 * dos veces (por ejemplo, al reiniciar la aplicación) rompería el
 * arranque con un error de base de datos.
 */
@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner cargarDatosDePrueba(TareaService tareaService,
                                                  CategoriaRepository categoriaRepository,
                                                  UsuarioService usuarioService) {
        return args -> {
            Categoria curso = categoriaRepository.save(new Categoria(null, "Curso", new ArrayList<>()));
            Categoria personal = categoriaRepository.save(new Categoria(null, "Personal", new ArrayList<>()));

            var t1 = tareaService.crear("Configurar el repositorio Git del proyecto guía", curso);
            tareaService.crear("Explicar @SpringBootApplication, @Controller, @Service y @Repository", curso);
            tareaService.crear("Actualizar el tablero Kanban con el avance del Sprint 0", personal);

            tareaService.alternarCompletada(t1.getId());

            if (!usuarioService.existeEmail("admin@tallerpw.com")) {
                usuarioService.registrar("Administradora del curso", "admin@tallerpw.com", "admin1234", "ADMIN");
            }
            if (!usuarioService.existeEmail("estudiante@tallerpw.com")) {
                usuarioService.registrar("Estudiante Demo", "estudiante@tallerpw.com", "estudiante1234");
            }
        };
    }

}
