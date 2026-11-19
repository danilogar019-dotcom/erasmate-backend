package com.erasmate.erasmate_backend.repositories;

import com.erasmate.erasmate_backend.models.Lugar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
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

    // Búsqueda con todos los filtros opcionales: si un parámetro llega null, ese filtro no se aplica.
    // El texto se busca en el nombre o en la descripción, sin distinguir mayúsculas y minúsculas.
    @Query("""
        SELECT l FROM Lugar l
        WHERE (:ciudad IS NULL OR l.ciudad.idCiudad = :ciudad)
          AND (:categoria IS NULL OR l.categoria.idCategoria = :categoria)
          AND (:texto IS NULL
               OR LOWER(l.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
               OR LOWER(l.descripcion) LIKE LOWER(CONCAT('%', :texto, '%')))
          AND (:precioMax IS NULL OR l.precioAprox <= :precioMax)
        """)
    List<Lugar> buscar(@Param("ciudad") Long ciudad,
                       @Param("categoria") Long categoria,
                       @Param("texto") String texto,
                       @Param("precioMax") BigDecimal precioMax);
}
