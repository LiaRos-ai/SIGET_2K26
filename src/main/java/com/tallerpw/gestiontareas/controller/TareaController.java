package com.tallerpw.gestiontareas.controller;

import com.tallerpw.gestiontareas.dto.TareaFormDTO;
import com.tallerpw.gestiontareas.model.Tarea;
import com.tallerpw.gestiontareas.service.CategoriaService;
import com.tallerpw.gestiontareas.service.TareaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador de la capa web para "Tarea".
 *
 * Día 3: reemplaza el @ResponseBody de texto plano por vistas Thymeleaf.
 * Día 5: formulario de creación con th:object/th:field y @Valid.
 * Día 6: @RequestParam (filtro), @PathVariable (toggle), DTO.
 * Día 7: persistencia real con Spring Data JPA.
 *
 * Día 8: CRUD completo. Se agregan:
 *   - GET  /tareas/{id}/editar  -> formulario de edición (precargado)
 *   - POST /tareas/{id}/editar  -> guarda los cambios
 *   - POST /tareas/{id}/eliminar -> elimina la tarea
 * El mismo formulario (tarea-form.html) se reutiliza para crear y
 * editar, distinguiendo el caso con el atributo "editando" del Model.
 */
@Controller
public class TareaController {

    private final TareaService tareaService;
    private final CategoriaService categoriaService;

    @Autowired
    public TareaController(TareaService tareaService, CategoriaService categoriaService) {
        this.tareaService = tareaService;
        this.categoriaService = categoriaService;
    }

    @GetMapping("/tareas")
    public String listar(@RequestParam(required = false) Boolean completada, Model model) {
        List<Tarea> tareas = tareaService.listarFiltradas(completada);
        model.addAttribute("tareas", tareas);
        model.addAttribute("totalTareas", tareas.size());
        model.addAttribute("filtroActual", completada);
        return "tareas";
    }

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
     * Formulario vacío para CREAR. "editando=false" le indica a la vista
     * que debe enviar el POST a /tareas (no a /tareas/{id}/editar).
     */
    @GetMapping("/tareas/nueva")
    public String formularioNuevaTarea(Model model) {
        model.addAttribute("tarea", new TareaFormDTO());
        model.addAttribute("categorias", categoriaService.listarTodas());
        model.addAttribute("editando", false);
        return "tarea-form";
    }

    @PostMapping("/tareas")
    public String crear(@Valid @ModelAttribute("tarea") TareaFormDTO formulario, BindingResult resultado, Model model) {
        if (resultado.hasErrors()) {
            model.addAttribute("categorias", categoriaService.listarTodas());
            model.addAttribute("editando", false);
            return "tarea-form";
        }
        tareaService.crearDesdeFormulario(formulario.getTitulo(), formulario.getCategoriaId());
        return "redirect:/tareas";
    }

    /**
     * Formulario precargado para EDITAR. "editando=true" + "tareaId" le
     * indican a la vista que debe enviar el POST a /tareas/{id}/editar.
     */
    @GetMapping("/tareas/{id}/editar")
    public String formularioEditar(@PathVariable Long id, Model model) {
        return tareaService.buscarPorId(id)
                .map(tarea -> {
                    TareaFormDTO formulario = new TareaFormDTO();
                    formulario.setTitulo(tarea.getTitulo());
                    formulario.setCategoriaId(tarea.getCategoria() != null ? tarea.getCategoria().getId() : null);

                    model.addAttribute("tarea", formulario);
                    model.addAttribute("categorias", categoriaService.listarTodas());
                    model.addAttribute("editando", true);
                    model.addAttribute("tareaId", id);
                    return "tarea-form";
                })
                .orElse("tarea-no-encontrada");
    }

    @PostMapping("/tareas/{id}/editar")
    public String actualizar(@PathVariable Long id,
                              @Valid @ModelAttribute("tarea") TareaFormDTO formulario,
                              BindingResult resultado, Model model) {
        if (resultado.hasErrors()) {
            model.addAttribute("categorias", categoriaService.listarTodas());
            model.addAttribute("editando", true);
            model.addAttribute("tareaId", id);
            return "tarea-form";
        }
        return tareaService.actualizarDesdeFormulario(id, formulario.getTitulo(), formulario.getCategoriaId())
                .map(t -> "redirect:/tareas")
                .orElse("tarea-no-encontrada");
    }

    @PostMapping("/tareas/{id}/eliminar")
    public String eliminar(@PathVariable Long id) {
        tareaService.eliminar(id);
        return "redirect:/tareas";
    }

    @PostMapping("/tareas/{id}/completar")
    public String alternarCompletada(@PathVariable Long id) {
        tareaService.alternarCompletada(id);
        return "redirect:/tareas";
    }

}
