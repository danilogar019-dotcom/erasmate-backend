package com.erasmate.erasmate_backend.repositories;

import com.erasmate.erasmate_backend.models.Lugar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LugarRepository extends JpaRepository<Lugar, Long> {

    // Spring genera la consulta a partir del nombre del método:
    // SELECT COUNT(*) > 0 FROM lugar WHERE id_ciudad = ?
    boolean existsByCiudad_IdCiudad(Long idCiudad);

    // SELECT COUNT(*) > 0 FROM lugar WHERE id_categoria = ?
    boolean existsByCategoria_IdCategoria(Long idCategoria);

    // SELECT * FROM lugar WHERE id_ciudad = ?
    List<Lugar> findByCiudad_IdCiudad(Long idCiudad);

    // SELECT * FROM lugar WHERE id_categoria = ?
    List<Lugar> findByCategoria_IdCategoria(Long idCategoria);

    // SELECT * FROM lugar WHERE id_ciudad = ? AND id_categoria = ?
    List<Lugar> findByCiudad_IdCiudadAndCategoria_IdCategoria(Long idCiudad, Long idCategoria);
}
