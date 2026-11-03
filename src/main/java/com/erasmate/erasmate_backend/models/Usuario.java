package com.erasmate.erasmate_backend.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(nullable = false, length = 100)
    private String nombre;

    // unique = true: no puede haber dos usuarios con el mismo email
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    // WRITE_ONLY: se puede enviar al crear el usuario, pero nunca se devuelve en el JSON
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false, length = 255)
    private String password;

    // Relación N:M: un usuario guarda muchos lugares favoritos
    // y un lugar puede ser favorito de muchos usuarios.
    // @JoinTable crea la tabla intermedia "favorito".
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "favorito",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_lugar")
    )
    private Set<Lugar> favoritos = new HashSet<>();

    // Constructor vacío: obligatorio para JPA
    public Usuario() {
    }

    public Usuario(String nombre, String email, String password) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
    }

    // Métodos del diagrama UML para gestionar favoritos
    public void addFavorito(Lugar lugar) {
        favoritos.add(lugar);
    }

    public void removeFavorito(Lugar lugar) {
        favoritos.remove(lugar);
    }

    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Set<Lugar> getFavoritos() { return favoritos; }
    public void setFavoritos(Set<Lugar> favoritos) { this.favoritos = favoritos; }
}
