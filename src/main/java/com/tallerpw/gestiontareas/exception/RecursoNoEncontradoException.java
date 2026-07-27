package com.tallerpw.gestiontareas.exception;

/**
 * Excepción de dominio para "no se encontró el recurso pedido".
 *
 * Antes del Día 13, cada método del Controller manejaba esto a mano con
 * Optional.map(...).orElse("tarea-no-encontrada") (vistas) o
 * ResponseEntity.notFound().build() (API) — funcionaba, pero repetía la
 * misma lógica una y otra vez.
 *
 * Desde hoy: los métodos hacen
 *     .orElseThrow(() -> new RecursoNoEncontradoException("..."))
 * y un @ControllerAdvice centralizado decide, en un solo lugar, cómo se
 * ve un "no encontrado" para las vistas (una página HTML) y cómo se ve
 * para la API (un JSON con código 404) — ver TareaWebExceptionHandler y
 * ApiExceptionHandler.
 */
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }

}
