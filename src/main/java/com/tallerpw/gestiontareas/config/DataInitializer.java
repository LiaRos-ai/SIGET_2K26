package com.tallerpw.gestiontareas.config;

import com.tallerpw.gestiontareas.model.Categoria;
import com.tallerpw.gestiontareas.model.Usuario;
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
 * Día 9: crea dos cuentas de ejemplo (ADMIN y USER).
 *
 * Día 10: el orden importa. Antes se creaban las tareas y recién
 * después (en otro bloque) los usuarios; ahora los usuarios se crean
 * PRIMERO, porque las tareas de ejemplo necesitan un propietario desde
 * el momento en que se guardan.
 */
@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner cargarDatosDePrueba(TareaService tareaService,
                                                  CategoriaRepository categoriaRepository,
                                                  UsuarioService usuarioService) {
        return args -> {
            Usuario admin = obtenerOCrear(usuarioService, "Administradora del curso",
                    "admin@tallerpw.com", "admin1234", "ADMIN");
            Usuario estudiante = obtenerOCrear(usuarioService, "Estudiante Demo",
                    "estudiante@tallerpw.com", "estudiante1234", "USER");

            Categoria curso = categoriaRepository.save(new Categoria(null, "Curso", new ArrayList<>()));
            Categoria personal = categoriaRepository.save(new Categoria(null, "Personal", new ArrayList<>()));

            // Las tareas de ejemplo quedan como propiedad del usuario "estudiante",
            // para poder probar en vivo que un USER solo ve/gestiona las suyas.
            var t1 = tareaService.crear("Configurar el repositorio Git del proyecto guía", curso, estudiante);
            tareaService.crear("Explicar @SpringBootApplication, @Controller, @Service y @Repository", curso, estudiante);
            tareaService.crear("Actualizar el tablero Kanban con el avance del Sprint 0", personal, estudiante);

            tareaService.alternarCompletada(t1.getId(), estudiante);
        };
    }

    /**
     * Evita el error de restricción UNIQUE en "email" si la aplicación se
     * reinicia contra la misma base de datos.
     */
    private Usuario obtenerOCrear(UsuarioService usuarioService, String nombre, String email,
                                   String password, String rol) {
        return usuarioService.buscarPorEmail(email)
                .orElseGet(() -> usuarioService.registrar(nombre, email, password, rol));
    }

}
