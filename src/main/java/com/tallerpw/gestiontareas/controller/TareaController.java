package com.tallerpw.gestiontareas.controller;

import com.tallerpw.gestiontareas.model.Tarea;
import com.tallerpw.gestiontareas.service.TareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador de la capa web para "Tarea".
 *
 * Día 2: el objetivo es SOLO demostrar el recorrido completo
 * Controller -> Service -> Repository usando @Autowired. Por eso la
 * respuesta se devuelve con @ResponseBody como texto plano, en vez de
 * una vista Thymeleaf real.
 *
 * Día 3: este método se reemplaza por uno que retorna un nombre de vista
 * (String) y usa Model para pasarle la lista de tareas a un archivo
 * tareas.html con th:each, th:text, etc.
 */
@Controller
public class TareaController {

    private final TareaService tareaService;

    @Autowired
    public TareaController(TareaService tareaService) {
        this.tareaService = tareaService;
    }

    @GetMapping("/tareas")
    @ResponseBody
    public String listar() {
        List<Tarea> tareas = tareaService.listarTodas();

        String filas = tareas.stream()
                .map(t -> "- [" + t.getId() + "] " + t.getTitulo() + (t.isCompletada() ? " (completada)" : ""))
                .collect(Collectors.joining("\n"));

        return "Tareas registradas (demo Día 2, en memoria):\n\n" + filas
                + "\n\nEsta respuesta es temporal: a partir del Día 3 se mostrará "
                + "con una vista Thymeleaf real (th:each) en lugar de texto plano.";
    }

}
