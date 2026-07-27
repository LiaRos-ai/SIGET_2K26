package com.tallerpw.gestiontareas.controller;

import com.tallerpw.gestiontareas.dto.TareaRequestDTO;
import com.tallerpw.gestiontareas.dto.TareaResponseDTO;
import com.tallerpw.gestiontareas.exception.RecursoNoEncontradoException;
import com.tallerpw.gestiontareas.model.Tarea;
import com.tallerpw.gestiontareas.service.TareaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API REST de tareas (Día 11).
 *
 * Día 12: protegida con HTTP Basic (ver SecurityConfig).
 *
 * Día 13:
 *   - Los "no encontrado" ahora lanzan RecursoNoEncontradoException, que
 *     ApiExceptionHandler traduce a un 404 con un ErrorResponseDTO
 *     consistente (antes: ResponseEntity.notFound().build() a mano, sin
 *     ningún detalle en el body).
 *   - Se agrega logging con SLF4J en las operaciones de escritura.
 *   - Se agregan anotaciones de Swagger/OpenAPI (@Tag, @Operation) para
 *     que springdoc genere documentación legible en /swagger-ui/index.html
 *     (dependencia incluida desde el Día 1, recién ahora la usamos).
 */
@RestController
@RequestMapping("/api/tareas")
@Tag(name = "Tareas", description = "CRUD de tareas vía JSON")
public class TareaRestController {

    private static final Logger log = LoggerFactory.getLogger(TareaRestController.class);

    private final TareaService tareaService;

    @Autowired
    public TareaRestController(TareaService tareaService) {
        this.tareaService = tareaService;
    }

    @Operation(summary = "Listar tareas", description = "Devuelve todas las tareas, opcionalmente filtradas por estado")
    @GetMapping
    public List<TareaResponseDTO> listar(@RequestParam(required = false) Boolean completada) {
        return tareaService.listarFiltradas(completada, null).stream()
                .map(TareaResponseDTO::desde)
                .toList();
    }

    @Operation(summary = "Detalle de una tarea", description = "Devuelve 404 si el id no existe")
    @GetMapping("/{id}")
    public TareaResponseDTO detalle(@PathVariable Long id) {
        Tarea tarea = tareaService.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró la tarea con id " + id));
        return TareaResponseDTO.desde(tarea);
    }

    @Operation(summary = "Crear una tarea", description = "Devuelve 201 con la tarea recién creada")
    @PostMapping
    public ResponseEntity<TareaResponseDTO> crear(@Valid @RequestBody TareaRequestDTO body) {
        Tarea creada = tareaService.crearDesdeApi(body.getTitulo(), body.getCategoriaId());
        log.info("Tarea creada vía API: id={}, titulo=\"{}\"", creada.getId(), creada.getTitulo());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(TareaResponseDTO.desde(creada));
    }

    @Operation(summary = "Actualizar una tarea", description = "Devuelve 404 si el id no existe")
    @PutMapping("/{id}")
    public TareaResponseDTO actualizar(@PathVariable Long id, @Valid @RequestBody TareaRequestDTO body) {
        Tarea actualizada = tareaService.actualizarDesdeApi(id, body.getTitulo(), body.getCategoriaId())
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró la tarea con id " + id));
        log.info("Tarea actualizada vía API: id={}", id);
        return TareaResponseDTO.desde(actualizada);
    }

    @Operation(summary = "Eliminar una tarea", description = "Devuelve 204 si se eliminó, 404 si el id no existía")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        boolean eliminada = tareaService.eliminarDesdeApi(id);
        if (!eliminada) {
            throw new RecursoNoEncontradoException("No se encontró la tarea con id " + id);
        }
        log.info("Tarea eliminada vía API: id={}", id);
        return ResponseEntity.noContent().build();
    }

}
