package com.tallerpw.gestiontareas.controller;

import com.tallerpw.gestiontareas.model.Tarea;
import com.tallerpw.gestiontareas.service.TareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Controlador de la capa web para "Tarea".
 *
 * Día 3: se reemplaza el @ResponseBody de texto plano del Día 2 por una
 * vista Thymeleaf real. El Controller ya no arma el texto de la
 * respuesta; solo llena el Model con los datos y delega el renderizado
 * a la plantilla HTML (separación de responsabilidades: el Controller no
 * debería tener HTML mezclado en el código Java).
 */
@Controller
public class TareaController {

    private final TareaService tareaService;

    @Autowired
    public TareaController(TareaService tareaService) {
        this.tareaService = tareaService;
    }

    /**
     * Listado de tareas. La vista usa th:each para recorrer la lista y
     * th:if/th:unless para mostrar un mensaje distinto según si hay o no
     * datos cargados.
     */
    @GetMapping("/tareas")
    public String listar(Model model) {
        List<Tarea> tareas = tareaService.listarTodas();
        model.addAttribute("tareas", tareas);
        model.addAttribute("totalTareas", tareas.size());
        return "tareas"; // busca src/main/resources/templates/tareas.html
    }

    /**
     * Detalle de una tarea puntual. Demuestra @PathVariable + th:if para
     * mostrar un estado distinto (completada / pendiente) y qué pasa
     * cuando el id no existe (Optional vacío -> vista de "no encontrada").
     */
    @GetMapping("/tareas/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        return tareaService.buscarPorId(id)
                .map(tarea -> {
                    model.addAttribute("tarea", tarea);
                    return "tarea-detalle";
                })
                .orElse("tarea-no-encontrada");
    }

}
