package com.tallerpw.gestiontareas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controlador de verificación para el Día 1.
 * Sirve únicamente para comprobar que el entorno (JDK, Maven, IDE,
 * Spring Boot, Thymeleaf) quedó correctamente configurado.
 *
 * A partir del Día 3 este controlador se reemplaza/expande con las
 * vistas reales del "Sistema de Gestión de Tareas".
 */
@Controller
public class HomeController {

    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("mensaje", "¡Spring Boot está corriendo correctamente!");
        model.addAttribute("proyecto", "Sistema de Gestión de Tareas");
        model.addAttribute("sprint", "Sprint 0 - Día 1");
        return "inicio";
    }

}
