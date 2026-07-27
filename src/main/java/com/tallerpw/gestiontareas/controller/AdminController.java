package com.tallerpw.gestiontareas.controller;

import com.tallerpw.gestiontareas.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Panel de administración, protegido por rol desde el Día 9
 * (.requestMatchers("/admin/**").hasRole("ADMIN") en SecurityConfig).
 *
 * Día 12: además de la demo original, ahora tiene una funcionalidad
 * real: listar y eliminar categorías, usando
 * CategoriaService.eliminarConTareas — el ejemplo de @Transactional
 * de hoy. Así, "protegido por rol" y "transaccional" quedan integrados
 * en una sola funcionalidad concreta, en vez de dos ejemplos sueltos.
 */
@Controller
public class AdminController {

    private final CategoriaService categoriaService;

    @Autowired
    public AdminController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping("/admin")
    public String panel(Model model) {
        model.addAttribute("categorias", categoriaService.listarTodas());
        return "admin";
    }

    /**
     * Al eliminar una categoría, las tareas que la tenían asignada NO
     * se borran: quedan sin categoría (categoria = null). Ver el
     * comentario de CategoriaService.eliminarConTareas sobre por qué
     * esto necesita ser una operación @Transactional.
     */
    @PostMapping("/admin/categorias/{id}/eliminar")
    public String eliminarCategoria(@PathVariable Long id) {
        categoriaService.eliminarConTareas(id);
        return "redirect:/admin";
    }

}
