package com.tallerpw.gestiontareas.controller;

import com.tallerpw.gestiontareas.dto.RegistroFormDTO;
import com.tallerpw.gestiontareas.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Controlador de autenticación: login y registro de cuentas.
 *
 * El POST de /login NO se maneja acá: Spring Security lo intercepta
 * automáticamente gracias a formLogin().loginPage("/login") configurado
 * en SecurityConfig. Este Controller solo muestra la vista del login
 * (GET) y maneja el registro completo (GET + POST).
 */
@Controller
public class AuthController {

    private final UsuarioService usuarioService;

    @Autowired
    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registro")
    public String formularioRegistro(Model model) {
        model.addAttribute("registro", new RegistroFormDTO());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrar(@Valid @ModelAttribute("registro") RegistroFormDTO formulario,
                             BindingResult resultado) {
        if (usuarioService.existeEmail(formulario.getEmail())) {
            // rejectValue asocia el error puntualmente al campo "email",
            // para que th:errors="*{email}" lo muestre en el lugar correcto.
            resultado.rejectValue("email", "email.duplicado", "Ya existe una cuenta con ese email");
        }

        if (resultado.hasErrors()) {
            return "registro";
        }

        usuarioService.registrar(formulario.getNombre(), formulario.getEmail(), formulario.getPassword());
        return "redirect:/login?registrado";
    }

}
