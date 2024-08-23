package com.ESFE.Asistencias.Entidades;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "el nombre del usuario es requerido")
    private String login;

    @NotBlank(message = "la contrasena es requerida")
    private String clave;

    private int status;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usuario_rol",
    joinColumns = @JoinColumn(name = "usuario_id"),
    inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private List<Rol> roles;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public @NotBlank(message = "el nombre del usuario es requerido") String getLogin() {
        return login;
    }

    public void setLogin(@NotBlank(message = "el nombre del usuario es requerido") String login) {
        this.login = login;
    }

    public @NotBlank(message = "la contrasena es requerida") String getClave() {
        return clave;
    }

    public void setClave(@NotBlank(message = "la contrasena es requerida") String clave) {
        this.clave = clave;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Rol> getRoles() {
        return roles;
    }

    public void setRoles(List<Rol> roles) {
        this.roles = roles;
    }
}
