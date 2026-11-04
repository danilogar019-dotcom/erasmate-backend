package com.erasmate.erasmate_backend.controllers;

import com.erasmate.erasmate_backend.models.Categoria;
import com.erasmate.erasmate_backend.models.Ciudad;
import com.erasmate.erasmate_backend.models.Lugar;
import com.erasmate.erasmate_backend.models.Usuario;
import com.erasmate.erasmate_backend.repositories.CategoriaRepository;
import com.erasmate.erasmate_backend.repositories.CiudadRepository;
import com.erasmate.erasmate_backend.repositories.LugarRepository;
import com.erasmate.erasmate_backend.repositories.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/lugares")
@CrossOrigin(origins = "http://localhost:5173") // permite las peticiones desde tu React (Vite)
public class LugarController {

    private final LugarRepository lugarRepository;
    private final CiudadRepository ciudadRepository;
    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;

    public LugarController(LugarRepository lugarRepository, CiudadRepository ciudadRepository,
                           CategoriaRepository categoriaRepository, UsuarioRepository usuarioRepository) {
        this.lugarRepository = lugarRepository;
        this.ciudadRepository = ciudadRepository;
        this.categoriaRepository = categoriaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // GET /api/lugares                      → todos
    // GET /api/lugares?ciudad=3             → los de una ciudad
    // GET /api/lugares?categoria=1          → los de una categoría
    // GET /api/lugares?ciudad=3&categoria=1 → los de una ciudad y categoría
    @GetMapping
    public List<Lugar> listar(@RequestParam(required = false) Long ciudad,
                              @RequestParam(required = false) Long categoria) {
        if (ciudad != null && categoria != null) {
            return lugarRepository.findByCiudad_IdCiudadAndCategoria_IdCategoria(ciudad, categoria);
        }
        if (ciudad != null) {
            return lugarRepository.findByCiudad_IdCiudad(ciudad);
        }
        if (categoria != null) {
            return lugarRepository.findByCategoria_IdCategoria(categoria);
        }
        return lugarRepository.findAll();
    }

    // GET /api/lugares/1 → uno por id (404 si no existe)
    @GetMapping("/{id}")
    public ResponseEntity<Lugar> obtener(@PathVariable Long id) {
        return lugarRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/lugares → crea uno nuevo (400 si la ciudad o la categoría no existen)
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Lugar lugar) {
        Optional<Ciudad> ciudad = buscarCiudad(lugar);
        Optional<Categoria> categoria = buscarCategoria(lugar);
        if (ciudad.isEmpty()) {
            return error("La ciudad indicada no existe");
        }
        if (categoria.isEmpty()) {
            return error("La categoría indicada no existe");
        }

        lugar.setIdLugar(null); // el id lo genera la base de datos
        lugar.setCiudad(ciudad.get());
        lugar.setCategoria(categoria.get());
        return ResponseEntity.status(HttpStatus.CREATED).body(lugarRepository.save(lugar));
    }

    // PUT /api/lugares/1 → modifica uno existente
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Lugar datos) {
        Optional<Lugar> existente = lugarRepository.findById(id);
        if (existente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Optional<Ciudad> ciudad = buscarCiudad(datos);
        Optional<Categoria> categoria = buscarCategoria(datos);
        if (ciudad.isEmpty()) {
            return error("La ciudad indicada no existe");
        }
        if (categoria.isEmpty()) {
            return error("La categoría indicada no existe");
        }

        Lugar lugar = existente.get();
        lugar.setNombre(datos.getNombre());
        lugar.setDescripcion(datos.getDescripcion());
        lugar.setPrecioAprox(datos.getPrecioAprox());
        lugar.setCiudad(ciudad.get());
        lugar.setCategoria(categoria.get());
        return ResponseEntity.ok(lugarRepository.save(lugar));
    }

    // DELETE /api/lugares/1 → borra el lugar
    // Antes lo quita de los favoritos de los usuarios, para no romper la FK de la tabla "favorito".
    // @Transactional: o se hace todo, o no se hace nada.
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> borrar(@PathVariable Long id) {
        Optional<Lugar> lugar = lugarRepository.findById(id);
        if (lugar.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        for (Usuario usuario : usuarioRepository.findByFavoritos_IdLugar(id)) {
            usuario.removeFavorito(lugar.get());
        }
        lugarRepository.delete(lugar.get());
        return ResponseEntity.noContent().build();
    }

    // ---------- Métodos auxiliares ----------

    // Busca en la BD la ciudad que viene en el JSON ({"ciudad": {"idCiudad": 3}})
    private Optional<Ciudad> buscarCiudad(Lugar lugar) {
        if (lugar.getCiudad() == null || lugar.getCiudad().getIdCiudad() == null) {
            return Optional.empty();
        }
        return ciudadRepository.findById(lugar.getCiudad().getIdCiudad());
    }

    // Busca en la BD la categoría que viene en el JSON ({"categoria": {"idCategoria": 1}})
    private Optional<Categoria> buscarCategoria(Lugar lugar) {
        if (lugar.getCategoria() == null || lugar.getCategoria().getIdCategoria() == null) {
            return Optional.empty();
        }
        return categoriaRepository.findById(lugar.getCategoria().getIdCategoria());
    }

    // Devuelve un 400 Bad Request con un mensaje en JSON: {"error": "..."}
    private ResponseEntity<Map<String, String>> error(String mensaje) {
        return ResponseEntity.badRequest().body(Map.of("error", mensaje));
    }
}
