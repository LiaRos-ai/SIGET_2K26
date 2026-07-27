package com.tallerpw.gestiontareas.exception;

import com.tallerpw.gestiontareas.controller.TareaController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Manejo centralizado de excepciones para TareaController (vistas
 * Thymeleaf). assignableTypes limita este Advice a esa clase puntual:
 * TareaRestController (la API) tiene su propio manejador —
 * ApiExceptionHandler — porque necesita responder JSON, no HTML.
 */
@ControllerAdvice(assignableTypes = TareaController.class)
public class TareaWebExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(TareaWebExceptionHandler.class);

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public String manejarNoEncontrado(RecursoNoEncontradoException ex) {
        // warn, no error: no es una falla del sistema, es un id que no existe
        // (por ejemplo, alguien escribió una URL a mano con un id inventado).
        log.warn("Recurso no encontrado (vista): {}", ex.getMessage());
        return "tarea-no-encontrada";
    }

}
