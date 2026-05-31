package com.electrodostore.auth_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@Entity
public class UserSec {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Nombre de usuario único para la autenticación del usuario.
    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    // Identidad del cliente asociada a usuarios con rol CLIENT.
    private Long clienteId;

    //Roles asignados al usuario
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    Set<Role> listRoles = new LinkedHashSet<>();

    private boolean enabled=true;
    private boolean accountNotExpired=true;
    private boolean accountNotLocked=true;
    private boolean credentialNotExpired=true;

}