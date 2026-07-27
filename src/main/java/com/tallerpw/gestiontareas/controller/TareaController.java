package com.tallerpw.gestiontareas.controller;

import com.tallerpw.gestiontareas.dto.TareaFormDTO;
import com.tallerpw.gestiontareas.exception.RecursoNoEncontradoException;
import com.tallerpw.gestiontareas.model.Tarea;
import com.tallerpw.gestiontareas.model.Usuario;
import com.tallerpw.gestiontareas.service.CategoriaService;
import com.tallerpw.gestiontareas.service.TareaService;
import com.tallerpw.gestiontareas.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
 * Día 8: CRUD completo (editar/eliminar).
 * Día 9: /tareas/** requiere estar autenticado (ver SecurityConfig).
 * Día 10: autorización a nivel de datos (HU-07).
 *
 * Día 13: los métodos que antes hacían
 *     .map(...).orElse("tarea-no-encontrada")
 * ahora hacen
 *     .orElseThrow(() -> new RecursoNoEncontradoException(...))
 * y TareaWebExceptionHandler decide, en un solo lugar, qué vista
 * mostrar. El Controller queda más corto y la decisión de "qué pasa
 * cuando no se encuentra algo" vive en un solo lugar, no repetida en
 * cada método.
 */
@Controller
public class TareaController {

    private final TareaService tareaService;
    private final CategoriaService categoriaService;
    private final UsuarioService usuarioService;

    @Autowired
    public TareaController(TareaService tareaService, CategoriaService categoriaService,
                            UsuarioService usuarioService) {
        this.tareaService = tareaService;
        this.categoriaService = categoriaService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/tareas")
    public String listar(@RequestParam(required = false) Boolean completada,
                          Authentication authentication, Model model) {
        Usuario usuarioActual = usuarioActual(authentication);
        boolean esAdmin = "ADMIN".equals(usuarioActual.getRol());

        List<Tarea> tareas = tareaService.listarFiltradas(completada, esAdmin ? null : usuarioActual);

        model.addAttribute("tareas", tareas);
        model.addAttribute("totalTareas", tareas.size());
        model.addAttribute("filtroActual", completada);
        model.addAttribute("viendoTodas", esAdmin);
        return "tareas";
    }

    @GetMapping("/tareas/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        Tarea tarea = tareaService.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró la tarea con id " + id));
        model.addAttribute("tarea", tarea);
        return "tarea-detalle";
    }

    @GetMapping("/tareas/nueva")
    public String formularioNuevaTarea(Model model) {
        model.addAttribute("tarea", new TareaFormDTO());
        model.addAttribute("categorias", categoriaService.listarTodas());
        model.addAttribute("editando", false);
        return "tarea-form";
    }

    @PostMapping("/tareas")
    public String crear(@Valid @ModelAttribute("tarea") TareaFormDTO formulario, BindingResult resultado,
                         Authentication authentication, Model model) {
        if (resultado.hasErrors()) {
            model.addAttribute("categorias", categoriaService.listarTodas());
            model.addAttribute("editando", false);
            return "tarea-form";
        }
        Usuario usuarioActual = usuarioActual(authentication);
        tareaService.crearDesdeFormulario(formulario.getTitulo(), formulario.getCategoriaId(), usuarioActual);
        return "redirect:/tareas";
    }

    @GetMapping("/tareas/{id}/editar")
    public String formularioEditar(@PathVariable Long id, Model model) {
        Tarea tarea = tareaService.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró la tarea con id " + id));

        TareaFormDTO formulario = new TareaFormDTO();
        formulario.setTitulo(tarea.getTitulo());
        formulario.setCategoriaId(tarea.getCategoria() != null ? tarea.getCategoria().getId() : null);

        model.addAttribute("tarea", formulario);
        model.addAttribute("categorias", categoriaService.listarTodas());
        model.addAttribute("editando", true);
        model.addAttribute("tareaId", id);
        return "tarea-form";
    }

    @PostMapping("/tareas/{id}/editar")
    public String actualizar(@PathVariable Long id,
                              @Valid @ModelAttribute("tarea") TareaFormDTO formulario,
                              BindingResult resultado, Authentication authentication, Model model) {
        if (resultado.hasErrors()) {
            model.addAttribute("categorias", categoriaService.listarTodas());
            model.addAttribute("editando", true);
            model.addAttribute("tareaId", id);
            return "tarea-form";
        }
        Usuario usuarioActual = usuarioActual(authentication);
        tareaService.actualizarDesdeFormulario(id, formulario.getTitulo(), formulario.getCategoriaId(), usuarioActual)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontró la tarea con id " + id));
        return "redirect:/tareas";
    }

    @PostMapping("/tareas/{id}/eliminar")
    public String eliminar(@PathVariable Long id, Authentication authentication) {
        tareaService.eliminar(id, usuarioActual(authentication));
        return "redirect:/tareas";
    }

    @PostMapping("/tareas/{id}/completar")
    public String alternarCompletada(@PathVariable Long id, Authentication authentication) {
        tareaService.alternarCompletada(id, usuarioActual(authentication));
        return "redirect:/tareas";
    }

    /**
     * Authentication.getName() devuelve el "username" con el que la
     * persona inició sesión — en este proyecto, el email (ver
     * CustomUserDetailsService). A partir de ahí resolvemos el Usuario
     * real de nuestra base de datos.
     */
    private Usuario usuarioActual(Authentication authentication) {
        String email = authentication.getName();
        return usuarioService.buscarPorEmail(email)
                .orElseThrow(() -> new IllegalStateException(
                        "Usuario autenticado pero no encontrado en la base de datos: " + email));
    }

}
