package com.erasmate.erasmate_backend.controllers;

import com.erasmate.erasmate_backend.models.Ciudad;
import com.erasmate.erasmate_backend.repositories.CiudadRepository;
import com.erasmate.erasmate_backend.repositories.LugarRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ciudades")
@CrossOrigin(origins = "http://localhost:5173") // permite las peticiones desde tu React (Vite)
public class CiudadController {

    private final CiudadRepository ciudadRepository;
    private final LugarRepository lugarRepository;

    // Spring inyecta los repositorios automáticamente
    public CiudadController(CiudadRepository ciudadRepository, LugarRepository lugarRepository) {
        this.ciudadRepository = ciudadRepository;
        this.lugarRepository = lugarRepository;
    }

    // GET /api/ciudades → lista todas
    @GetMapping
    public List<Ciudad> listar() {
        return ciudadRepository.findAll();
    }

    // GET /api/ciudades/1 → una por id (404 si no existe)
    @GetMapping("/{id}")
    public ResponseEntity<Ciudad> obtener(@PathVariable Long id) {
        return ciudadRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/ciudades → crea una nueva (201 Created)
    @PostMapping
    public ResponseEntity<Ciudad> crear(@RequestBody Ciudad ciudad) {
        ciudad.setIdCiudad(null); // el id lo genera la base de datos
        return ResponseEntity.status(HttpStatus.CREATED).body(ciudadRepository.save(ciudad));
    }

    // PUT /api/ciudades/1 → modifica una existente
    @PutMapping("/{id}")
    public ResponseEntity<Ciudad> actualizar(@PathVariable Long id, @RequestBody Ciudad datos) {
        return ciudadRepository.findById(id)
                .map(ciudad -> {
                    ciudad.setNombre(datos.getNombre());
                    ciudad.setPais(datos.getPais());
                    return ResponseEntity.ok(ciudadRepository.save(ciudad));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/ciudades/1 → borra (409 si tiene lugares asociados)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrar(@PathVariable Long id) {
        if (!ciudadRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        if (lugarRepository.existsByCiudad_IdCiudad(id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        ciudadRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}