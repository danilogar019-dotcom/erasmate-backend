package com.erasmate.erasmate_backend.repositories;

import com.erasmate.erasmate_backend.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Usuarios que tienen un lugar concreto en favoritos (consulta a la tabla "favorito")
    List<Usuario> findByFavoritos_IdLugar(Long idLugar);

    // SELECT COUNT(*) > 0 FROM usuario WHERE email = ?
    boolean existsByEmail(String email);

    // SELECT * FROM usuario WHERE email = ?
    Optional<Usuario> findByEmail(String email);
}