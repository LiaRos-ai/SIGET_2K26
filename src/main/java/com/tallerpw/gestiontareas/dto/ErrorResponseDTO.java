package com.tallerpw.gestiontareas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Forma consistente para CUALQUIER error que devuelva la API
 * (Día 13). Antes, un 404 devolvía un body vacío y un error de
 * validación devolvía el JSON genérico por defecto de Spring — dos
 * formas distintas de representar "esto salió mal". Ahora todo error de
 * la API tiene la misma forma, sin importar la causa.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDTO {

    private int status;
    private String mensaje;

}
