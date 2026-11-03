package com.erasmate.erasmate_backend.repositories;

import com.erasmate.erasmate_backend.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
