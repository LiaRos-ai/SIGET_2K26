package com.tallerpw.gestiontareas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador de verificación del entorno (Día 1).
 * A partir del Día 3 también sirve como puerta de entrada hacia el
 * listado de tareas real, construido con Thymeleaf.
 */
@Controller
public class HomeController {

    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("mensaje", "¡Spring Boot está corriendo correctamente!");
        model.addAttribute("proyecto", "Sistema de Gestión de Tareas");
        model.addAttribute("sprint", "Sprint 2 - Día 8");
        return "inicio";
    }

}
