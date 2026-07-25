package com.tallerpw.gestiontareas.controller;

import com.tallerpw.gestiontareas.dto.TareaFormDTO;
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
 *
 * Día 10: cada método resuelve primero al Usuario autenticado (a partir
 * del email que devuelve Authentication.getName()) y se lo pasa al
 * Service, que decide qué puede ver o hacer según sea ADMIN o dueño de
 * la tarea (HU-07). Esto es "autorización a nivel de datos", un paso
 * más allá de la autorización a nivel de ruta que ya hace SecurityConfig.
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

        // ADMIN ve las tareas de todos (propietario = null -> sin filtrar por dueño).
        // Un usuario normal solo ve las suyas.
        List<Tarea> tareas = tareaService.listarFiltradas(completada, esAdmin ? null : usuarioActual);

        model.addAttribute("tareas", tareas);
        model.addAttribute("totalTareas", tareas.size());
        model.addAttribute("filtroActual", completada);
        model.addAttribute("viendoTodas", esAdmin);
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
                              BindingResult resultado, Authentication authentication, Model model) {
        if (resultado.hasErrors()) {
            model.addAttribute("categorias", categoriaService.listarTodas());
            model.addAttribute("editando", true);
            model.addAttribute("tareaId", id);
            return "tarea-form";
        }
        Usuario usuarioActual = usuarioActual(authentication);
        return tareaService.actualizarDesdeFormulario(id, formulario.getTitulo(), formulario.getCategoriaId(), usuarioActual)
                .map(t -> "redirect:/tareas")
                .orElse("tarea-no-encontrada");
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
