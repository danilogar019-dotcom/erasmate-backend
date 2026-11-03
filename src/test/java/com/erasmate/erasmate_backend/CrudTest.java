package com.erasmate.erasmate_backend;

import com.erasmate.erasmate_backend.models.*;
import com.erasmate.erasmate_backend.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CrudTest {

    @Autowired private CiudadRepository ciudadRepository;
    @Autowired private CategoriaRepository categoriaRepository;
    @Autowired private LugarRepository lugarRepository;
    @Autowired private UsuarioRepository usuarioRepository;

    @Test
    void conexionConLaBaseDeDatos() {
        // Si la conexión falla, count() lanza una excepción y el test falla
        assertTrue(ciudadRepository.count() >= 0);
    }

    @Test
    void crudCompletoDeLugar() {
        // ---------- CREATE ----------
        Ciudad ciudad = ciudadRepository.save(new Ciudad("Lisboa", "Portugal"));
        Categoria categoria = categoriaRepository.save(new Categoria("Restaurantes", "Sitios para comer"));
        Lugar lugar = lugarRepository.save(
                new Lugar("Time Out Market", "Mercado gastronómico", 15.0, ciudad, categoria));

        assertNotNull(lugar.getIdLugar(), "El lugar debería tener id tras guardarse");

        // ---------- READ ----------
        Lugar leido = lugarRepository.findById(lugar.getIdLugar()).orElseThrow();
        assertEquals("Time Out Market", leido.getNombre());
        assertEquals("Lisboa", leido.getCiudad().getNombre());          // relación 1:N con Ciudad
        assertEquals("Restaurantes", leido.getCategoria().getNombre()); // relación 1:N con Categoria

        // ---------- UPDATE ----------
        leido.setPrecioAprox(20.0);
        lugarRepository.save(leido);
        assertEquals(20.0, lugarRepository.findById(lugar.getIdLugar()).orElseThrow().getPrecioAprox());

        // ---------- DELETE ----------
        lugarRepository.deleteById(lugar.getIdLugar());
        assertFalse(lugarRepository.existsById(lugar.getIdLugar()), "El lugar debería estar borrado");

        // Limpieza: dejamos la base de datos como estaba
        ciudadRepository.delete(ciudad);
        categoriaRepository.delete(categoria);
    }

    @Test
    void favoritosRelacionNM() {
        Ciudad ciudad = ciudadRepository.save(new Ciudad("Roma", "Italia"));
        Categoria categoria = categoriaRepository.save(new Categoria("Ocio", null));
        Lugar lugar = lugarRepository.save(new Lugar("Coliseo", "Anfiteatro romano", 18.0, ciudad, categoria));

        // Email único para que no choque con la restricción UNIQUE si se repite el test
        Usuario usuario = new Usuario("Dani", "test" + System.currentTimeMillis() + "@erasmate.com", "1234");
        usuario.addFavorito(lugar);
        usuario = usuarioRepository.save(usuario);

        // Se ha guardado la fila en la tabla intermedia "favorito"
        Usuario leido = usuarioRepository.findById(usuario.getIdUsuario()).orElseThrow();
        assertEquals(1, leido.getFavoritos().size());
        assertEquals("Coliseo", leido.getFavoritos().iterator().next().getNombre());

        // Limpieza: al borrar el usuario se borran también sus filas de "favorito"
        usuarioRepository.delete(leido);
        lugarRepository.delete(lugar);
        ciudadRepository.delete(ciudad);
        categoriaRepository.delete(categoria);
    }
}
