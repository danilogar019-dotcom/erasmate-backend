package com.erasmate.erasmate_backend.repositories;

import com.erasmate.erasmate_backend.models.Lugar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LugarRepository extends JpaRepository<Lugar, Long> {

    // Spring genera la consulta a partir del nombre del método:
    // SELECT COUNT(*) > 0 FROM lugar WHERE id_ciudad = ?
    boolean existsByCiudad_IdCiudad(Long idCiudad);

    // SELECT COUNT(*) > 0 FROM lugar WHERE id_categoria = ?
    boolean existsByCategoria_IdCategoria(Long idCategoria);
}