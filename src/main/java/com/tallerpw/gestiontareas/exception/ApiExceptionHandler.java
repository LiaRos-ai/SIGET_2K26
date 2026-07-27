package com.tallerpw.gestiontareas.exception;

import com.tallerpw.gestiontareas.controller.TareaRestController;
import com.tallerpw.gestiontareas.dto.ErrorResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * Manejo centralizado de excepciones para TareaRestController (API
 * JSON). @RestControllerAdvice = @ControllerAdvice + @ResponseBody en
 * todos los métodos (el mismo combo que ya conocemos de
 * @RestController desde el Día 11).
 *
 * assignableTypes limita este Advice a la API: TareaController (vistas)
 * usa TareaWebExceptionHandler en su lugar.
 */
@RestControllerAdvice(assignableTypes = TareaRestController.class)
public class ApiExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorResponseDTO> manejarNoEncontrado(RecursoNoEncontradoException ex) {
        log.warn("Recurso no encontrado (API): {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDTO(404, ex.getMessage()));
    }

    /**
     * Spring lanza esta excepción automáticamente cuando @Valid rechaza
     * un @RequestBody (Día 11). Antes de hoy, esto devolvía el JSON
     * genérico por defecto de Spring; ahora devuelve la misma forma
     * ErrorResponseDTO que cualquier otro error de la API, con el detalle
     * de qué campo falló y por qué.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> manejarValidacion(MethodArgumentNotValidException ex) {
        String detalle = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        log.warn("Error de validación en la API: {}", detalle);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDTO(400, detalle));
    }

    /**
     * Red de seguridad: cualquier otra excepción no prevista se loguea
     * con el detalle completo (para el equipo) y se responde con un
     * mensaje genérico (para quien consuma la API) — nunca un stack
     * trace ni detalles internos.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> manejarErrorInesperado(Exception ex) {
        log.error("Error inesperado no controlado en la API", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponseDTO(500, "Ocurrió un error inesperado."));
    }

}
