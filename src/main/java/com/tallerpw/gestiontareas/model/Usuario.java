package com.tallerpw.gestiontareas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad JPA "Usuario". Se agrega el Día 9 junto con Spring Security.
 *
 * IMPORTANTE: "password" guarda el HASH de la contraseña (generado con
 * BCryptPasswordEncoder en UsuarioService), nunca la contraseña en texto
 * plano. Ni el Controller ni la vista deberían mostrar este campo jamás.
 *
 * "rol" es un String simple ("USER" o "ADMIN"). CustomUserDetailsService
 * se encarga de traducirlo al formato que espera Spring Security
 * (con el prefijo "ROLE_").
 *
 * Se usa @Getter/@Setter (no @Data) por costumbre del proyecto guía desde
 * el Día 7: mantiene la puerta abierta a agregar relaciones más adelante
 * sin arrastrar el riesgo de un toString/equals que las recorra.
 */
@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(unique = true)
    private String email;

    private String password;

    private String rol;

}
