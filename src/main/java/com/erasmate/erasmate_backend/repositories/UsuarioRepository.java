package com.erasmate.erasmate_backend.repositories;

import com.erasmate.erasmate_backend.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Usuarios que tienen un lugar concreto en favoritos (consulta a la tabla "favorito")
    List<Usuario> findByFavoritos_IdLugar(Long idLugar);
}
