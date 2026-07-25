package com.tallerpw.gestiontareas.service;

import com.tallerpw.gestiontareas.model.Usuario;
import com.tallerpw.gestiontareas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Capa Service para Usuario. La regla de negocio más importante que vive
 * acá: la contraseña SIEMPRE se guarda encriptada (hash con
 * BCryptPasswordEncoder), nunca en texto plano.
 */
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean existeEmail(String email) {
        return usuarioRepository.findByEmail(email).isPresent();
    }

    /**
     * Registro público (usado por el formulario /registro): siempre crea
     * el usuario con rol "USER". Nadie puede autoasignarse "ADMIN" desde
     * este formulario, porque RegistroFormDTO ni siquiera tiene ese campo.
     */
    public Usuario registrar(String nombre, String email, String password) {
        return registrar(nombre, email, password, "USER");
    }

    /**
     * Overload interno, usado solo por DataInitializer para crear el
     * usuario administrador de ejemplo. No está expuesto a través de
     * ningún formulario público.
     */
    public Usuario registrar(String nombre, String email, String password, String rol) {
        String hash = passwordEncoder.encode(password);
        Usuario nuevo = new Usuario(null, nombre, email, hash, rol);
        return usuarioRepository.save(nuevo);
    }

}
