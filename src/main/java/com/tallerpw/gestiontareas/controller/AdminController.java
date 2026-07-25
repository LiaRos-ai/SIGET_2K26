package com.tallerpw.gestiontareas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador mínimo para demostrar la autorización por rol configurada
 * en SecurityConfig (.requestMatchers("/admin/**").hasRole("ADMIN")).
 *
 * No tiene funcionalidad real todavía: es solo un ejemplo didáctico de
 * que una ruta puede requerir un rol específico, distinto de "estar
 * simplemente autenticado". El panel de administración real (por
 * ejemplo, ver las tareas de todos los usuarios — HU-07) se profundiza
 * más adelante en el curso.
 */
@Controller
public class AdminController {

    @GetMapping("/admin")
    public String panel() {
        return "admin";
    }

}
