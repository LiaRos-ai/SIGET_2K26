package com.tallerpw.gestiontareas.security;

import com.tallerpw.gestiontareas.model.Usuario;
import com.tallerpw.gestiontareas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Puente entre nuestra entidad Usuario y Spring Security.
 *
 * Cuando alguien intenta iniciar sesión, Spring Security llama a
 * loadUserByUsername(...) con lo que la persona escribió en el campo
 * "username" del formulario de login (en nuestro caso, el email).
 * Nosotros buscamos ese Usuario en la base de datos y lo traducimos a un
 * UserDetails, el objeto que Spring Security entiende internamente.
 *
 * User.builder().roles("USER") agrega automáticamente el prefijo
 * "ROLE_" (queda como "ROLE_USER"), que es el formato que después se
 * compara contra hasRole("USER") en SecurityConfig.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No existe una cuenta con ese email: " + email));

        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getPassword()) // ya viene hasheada desde UsuarioService
                .roles(usuario.getRol())
                .build();
    }

}
