package com.tallerpw.gestiontareas.controller;

import com.tallerpw.gestiontareas.dto.TareaRequestDTO;
import com.tallerpw.gestiontareas.dto.TareaResponseDTO;
import com.tallerpw.gestiontareas.model.Tarea;
import com.tallerpw.gestiontareas.service.TareaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API REST de tareas (Día 11).
 *
 * Diferencias clave con TareaController (la capa web con vistas Thymeleaf):
 *
 *   - @RestController = @Controller + @ResponseBody en TODOS los métodos:
 *     cada valor que retornan los métodos se serializa directo a JSON
 *     (vía Jackson, ya incluido en spring-boot-starter-web), en vez de
 *     buscar una vista .html con ese nombre.
 *   - @RequestBody en vez de @ModelAttribute: el body de la petición
 *     (JSON) se deserializa directo a un objeto Java, en vez de leer
 *     campos de un formulario HTML.
 *   - Devuelve códigos de estado HTTP explícitos (200, 201, 204, 404)
 *     en vez de nombres de vista o redirects.
 *
 * Base path: /api/tareas (convención común para distinguir la API de
 * las rutas "de página" como /tareas).
 */
@RestController
@RequestMapping("/api/tareas")
public class TareaRestController {

    private final TareaService tareaService;

    @Autowired
    public TareaRestController(TareaService tareaService) {
        this.tareaService = tareaService;
    }

    /**
     * GET /api/tareas
     * GET /api/tareas?completada=true
     *
     * Devuelve 200 OK con la lista en JSON. Reutiliza el mismo
     * TareaService.listarFiltradas del Día 6, pasando propietario=null
     * (por ahora la API muestra las tareas de todos — ver el comentario
     * de la clase sobre la seguridad pendiente del Día 12).
     */
    @GetMapping
    public List<TareaResponseDTO> listar(@RequestParam(required = false) Boolean completada) {
        return tareaService.listarFiltradas(completada, null).stream()
                .map(TareaResponseDTO::desde)
                .toList();
    }

    /**
     * GET /api/tareas/{id}
     *
     * 200 OK con la tarea si existe; 404 Not Found si no.
     * ResponseEntity permite controlar explícitamente el código de
     * estado HTTP de la respuesta, algo que un método @GetMapping que
     * solo retorna un DTO no puede hacer por sí mismo.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TareaResponseDTO> detalle(@PathVariable Long id) {
        return tareaService.buscarPorId(id)
                .map(tarea -> ResponseEntity.ok(TareaResponseDTO.desde(tarea)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/tareas
     * Body (JSON): {"titulo": "Comprar pan", "categoriaId": 1}
     *
     * 201 Created con la tarea recién creada. @Valid aplica las mismas
     * anotaciones de Bean Validation que ya conocemos (Día 5) sobre el
     * JSON deserializado.
     */
    @PostMapping
    public ResponseEntity<TareaResponseDTO> crear(@Valid @RequestBody TareaRequestDTO body) {
        Tarea creada = tareaService.crearDesdeApi(body.getTitulo(), body.getCategoriaId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(TareaResponseDTO.desde(creada));
    }

    /**
     * PUT /api/tareas/{id}
     * Body (JSON): {"titulo": "Comprar pan integral", "categoriaId": 1}
     *
     * 200 OK con la tarea actualizada; 404 Not Found si el id no existe.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TareaResponseDTO> actualizar(@PathVariable Long id,
                                                        @Valid @RequestBody TareaRequestDTO body) {
        return tareaService.actualizarDesdeApi(id, body.getTitulo(), body.getCategoriaId())
                .map(tarea -> ResponseEntity.ok(TareaResponseDTO.desde(tarea)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE /api/tareas/{id}
     *
     * 204 No Content si se eliminó; 404 Not Found si no existía.
     * 204 no lleva body — por eso ResponseEntity<Void>.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        boolean eliminada = tareaService.eliminarDesdeApi(id);
        return eliminada ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

}
