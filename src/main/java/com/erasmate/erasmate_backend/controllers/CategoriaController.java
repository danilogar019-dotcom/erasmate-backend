package com.erasmate.erasmate_backend.controllers;

import com.erasmate.erasmate_backend.models.Categoria;
import com.erasmate.erasmate_backend.repositories.CategoriaRepository;
import com.erasmate.erasmate_backend.repositories.LugarRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "http://localhost:5173") // permite las peticiones desde tu React (Vite)
public class CategoriaController {

    private final CategoriaRepository categoriaRepository;
    private final LugarRepository lugarRepository;

    // Spring inyecta los repositorios automáticamente
    public CategoriaController(CategoriaRepository categoriaRepository, LugarRepository lugarRepository) {
        this.categoriaRepository = categoriaRepository;
        this.lugarRepository = lugarRepository;
    }

    // GET /api/categorias → lista todas
    @GetMapping
    public List<Categoria> listar() {
        return categoriaRepository.findAll();
    }

    // GET /api/categorias/1 → una por id (404 si no existe)
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> obtener(@PathVariable Long id) {
        return categoriaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/categorias → crea una nueva (201 Created)
    @PostMapping
    public ResponseEntity<Categoria> crear(@RequestBody Categoria categoria) {
        categoria.setIdCategoria(null); // el id lo genera la base de datos
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaRepository.save(categoria));
    }

    // PUT /api/categorias/1 → modifica una existente
    @PutMapping("/{id}")
    public ResponseEntity<Categoria> actualizar(@PathVariable Long id, @RequestBody Categoria datos) {
        return categoriaRepository.findById(id)
                .map(categoria -> {
                    categoria.setNombre(datos.getNombre());
                    categoria.setDescripcion(datos.getDescripcion());
                    return ResponseEntity.ok(categoriaRepository.save(categoria));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/categorias/1 → borra (409 si tiene lugares asociados)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrar(@PathVariable Long id) {
        if (!categoriaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        if (lugarRepository.existsByCategoria_IdCategoria(id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        categoriaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
