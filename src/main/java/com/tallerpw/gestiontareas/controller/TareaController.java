package com.tallerpw.gestiontareas.controller;

import com.tallerpw.gestiontareas.dto.TareaFormDTO;
import com.tallerpw.gestiontareas.model.Tarea;
import com.tallerpw.gestiontareas.service.TareaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Controlador de la capa web para "Tarea".
 *
 * Día 3: se reemplaza el @ResponseBody de texto plano del Día 2 por una
 * vista Thymeleaf real.
 *
 * Día 5: se agrega el formulario de creación con th:object/th:field y
 * validación con @Valid + BindingResult.
 *
 * Día 6: se repasa el ciclo petición-respuesta de Spring MVC completo:
 *   - @RequestParam: filtro opcional en el listado (?completada=true|false).
 *   - @PathVariable: ya usado desde el Día 3 en /tareas/{id}; ahora también
 *     en el toggle de estado (POST /tareas/{id}/completar).
 *   - DTO: el formulario ya no bindea contra la entidad Tarea, sino contra
 *     TareaFormDTO (ver esa clase para la razón: evitar over-posting).
 *   - @PutMapping/@DeleteMapping: los formularios HTML solo soportan GET y
 *     POST de forma nativa, así que en esta capa web (Thymeleaf) seguimos
 *     usando POST para "actualizar" y "eliminar". Los verbos PUT y DELETE
 *     reales se usan a partir del Día 11 con @RestController, donde el
 *     cliente (Postman, JavaScript) sí puede enviarlos directamente.
 */
@Controller
public class TareaController {

    private final TareaService tareaService;

    @Autowired
    public TareaController(TareaService tareaService) {
        this.tareaService = tareaService;
    }

    /**
     * Listado de tareas, con filtro opcional por estado.
     * @RequestParam(required = false) hace que el parámetro sea opcional:
     * /tareas               -> completada == null -> muestra todas
     * /tareas?completada=true  -> solo completadas
     * /tareas?completada=false -> solo pendientes
     */
    @GetMapping("/tareas")
    public String listar(@RequestParam(required = false) Boolean completada, Model model) {
        List<Tarea> tareas = tareaService.listarFiltradas(completada);
        model.addAttribute("tareas", tareas);
        model.addAttribute("totalTareas", tareas.size());
        model.addAttribute("filtroActual", completada);
        return "tareas";
    }

    /**
     * Detalle de una tarea puntual. @PathVariable extrae el {id} de la URL.
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

    /**
     * Muestra el formulario vacío. Ahora el Model expone un TareaFormDTO
     * en vez de una Tarea completa (Día 6: separación DTO / entidad).
     */
    @GetMapping("/tareas/nueva")
    public String formularioNuevaTarea(Model model) {
        model.addAttribute("tarea", new TareaFormDTO());
        return "tarea-form";
    }

    /**
     * Recibe el formulario. El DTO solo expone "titulo": ni "id" ni
     * "completada" pueden llegar manipulados desde la petición HTTP,
     * porque esos campos ni siquiera existen en TareaFormDTO.
     */
    @PostMapping("/tareas")
    public String crear(@Valid @ModelAttribute("tarea") TareaFormDTO formulario, BindingResult resultado) {
        if (resultado.hasErrors()) {
            return "tarea-form";
        }
        tareaService.crear(formulario.getTitulo());
        return "redirect:/tareas";
    }

    /**
     * Alterna el estado completada/pendiente. Se modela como POST (no
     * PUT) porque lo dispara un <form> HTML normal desde la vista; ver el
     * comentario de la clase sobre @PutMapping/@DeleteMapping reales.
     */
    @PostMapping("/tareas/{id}/completar")
    public String alternarCompletada(@PathVariable Long id) {
        tareaService.alternarCompletada(id);
        return "redirect:/tareas";
    }

}
