package com.erasmate.erasmate_backend.repositories;

import com.erasmate.erasmate_backend.models.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
