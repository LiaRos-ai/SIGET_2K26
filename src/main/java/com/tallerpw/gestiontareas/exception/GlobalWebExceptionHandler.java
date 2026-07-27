package com.tallerpw.gestiontareas.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * Red de seguridad para CUALQUIER excepción no controlada que se escape
 * de un @Controller (sin restricción de assignableTypes: aplica a todos:
 * TareaController, AdminController, AuthController, HomeController).
 *
 * Sin esto, un error inesperado mostraría la "Whitelabel Error Page" por
 * defecto de Spring Boot — genérica, sin estilo, y (peor) a veces con
 * detalles técnicos que no deberían llegar a los ojos de un usuario
 * final. Acá se loguea el detalle completo para el equipo de desarrollo
 * (log.error, con el stack trace) y se muestra al usuario un mensaje
 * amigable y sin información sensible.
 */
@ControllerAdvice
public class GlobalWebExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalWebExceptionHandler.class);

    /**
     * Ignora NoResourceFoundException (favicon, robots.txt, etc. que
     * el navegador solicita automáticamente pero no son errores reales).
     * Spring Boot ya retorna 404 correctamente, no es necesario loguear.
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public void manejarNoResourceFound(NoResourceFoundException ex) {
        // Silenciosamente ignorado — Spring Boot ya retorna 404
    }

    @ExceptionHandler(Exception.class)
    public String manejarErrorInesperado(Exception ex, Model model) {
        // error, no warn: esto SÍ es una falla real que el equipo debería revisar.
        log.error("Error inesperado no controlado", ex);
        model.addAttribute("mensaje", "Ocurrió un error inesperado. Por favor intentá de nuevo en unos minutos.");
        return "error";
    }

}
