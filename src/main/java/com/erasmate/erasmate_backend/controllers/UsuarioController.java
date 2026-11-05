package com.erasmate.erasmate_backend.controllers;

import com.erasmate.erasmate_backend.models.Lugar;
import com.erasmate.erasmate_backend.models.Usuario;
import com.erasmate.erasmate_backend.repositories.LugarRepository;
import com.erasmate.erasmate_backend.repositories.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:5173") // permite las peticiones desde tu React (Vite)
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final LugarRepository lugarRepository;

    public UsuarioController(UsuarioRepository usuarioRepository, LugarRepository lugarRepository) {
        this.usuarioRepository = usuarioRepository;
        this.lugarRepository = lugarRepository;
    }

    // POST /api/usuarios → registro (409 si el email ya está registrado)
    @PostMapping
    public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Ya existe un usuario con ese email"));
        }
        usuario.setIdUsuario(null); // el id lo genera la base de datos
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioRepository.save(usuario));
    }

    // POST /api/usuarios/login → login con email y contraseña (401 si son incorrectos)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario datos) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(datos.getEmail());
        if (usuario.isEmpty() || !usuario.get().getPassword().equals(datos.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Email o contraseña incorrectos"));
        }
        return ResponseEntity.ok(usuario.get());
    }

    // GET /api/usuarios/1 → datos de un usuario con sus favoritos (404 si no existe)
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtener(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ---------- FAVORITOS (relación N:M, tabla intermedia "favorito") ----------

    // GET /api/usuarios/1/favoritos → lista de lugares favoritos del usuario
    @GetMapping("/{id}/favoritos")
    public ResponseEntity<Set<Lugar>> listarFavoritos(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .map(usuario -> ResponseEntity.ok(usuario.getFavoritos()))
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/usuarios/1/favoritos/3 → añade el lugar 3 a los favoritos del usuario 1
    // Hibernate hace: INSERT INTO favorito (id_usuario, id_lugar) VALUES (1, 3)
    @PostMapping("/{id}/favoritos/{idLugar}")
    public ResponseEntity<?> anadirFavorito(@PathVariable Long id, @PathVariable Long idLugar) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        Optional<Lugar> lugar = lugarRepository.findById(idLugar);
        if (usuario.isEmpty() || lugar.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Usuario o lugar no encontrado"));
        }
        usuario.get().addFavorito(lugar.get());
        Usuario guardado = usuarioRepository.save(usuario.get());
        return ResponseEntity.ok(guardado.getFavoritos());
    }

    // DELETE /api/usuarios/1/favoritos/3 → quita el lugar 3 de los favoritos del usuario 1
    // Hibernate hace: DELETE FROM favorito WHERE id_usuario = 1 AND id_lugar = 3
    @DeleteMapping("/{id}/favoritos/{idLugar}")
    public ResponseEntity<?> quitarFavorito(@PathVariable Long id, @PathVariable Long idLugar) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        Optional<Lugar> lugar = lugarRepository.findById(idLugar);
        if (usuario.isEmpty() || lugar.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Usuario o lugar no encontrado"));
        }
        usuario.get().removeFavorito(lugar.get());
        usuarioRepository.save(usuario.get());
        return ResponseEntity.noContent().build();
    }
}
