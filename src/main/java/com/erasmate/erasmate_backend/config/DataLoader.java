package com.erasmate.erasmate_backend.config;

import com.erasmate.erasmate_backend.models.*;
import com.erasmate.erasmate_backend.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// Carga datos de prueba al arrancar la aplicación, solo si la base de datos está vacía
@Component
public class DataLoader implements CommandLineRunner {

    private final CiudadRepository ciudadRepository;
    private final CategoriaRepository categoriaRepository;
    private final LugarRepository lugarRepository;
    private final UsuarioRepository usuarioRepository;

    public DataLoader(CiudadRepository ciudadRepository, CategoriaRepository categoriaRepository,
                      LugarRepository lugarRepository, UsuarioRepository usuarioRepository) {
        this.ciudadRepository = ciudadRepository;
        this.categoriaRepository = categoriaRepository;
        this.lugarRepository = lugarRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void run(String... args) {
        // Si ya hay datos, no hacemos nada (así no se duplican al reiniciar)
        if (ciudadRepository.count() > 0) {
            return;
        }

        // ---------- Ciudades (Canarias) ----------
        Ciudad lasPalmas = ciudadRepository.save(new Ciudad("Las Palmas de Gran Canaria", "España"));
        Ciudad santaCruz = ciudadRepository.save(new Ciudad("Santa Cruz de Tenerife", "España"));
        Ciudad laLaguna  = ciudadRepository.save(new Ciudad("San Cristóbal de La Laguna", "España"));
        Ciudad puerto    = ciudadRepository.save(new Ciudad("Puerto de la Cruz", "España"));

        // ---------- Categorías (alineadas con las secciones de la app) ----------
        Categoria tramites     = categoriaRepository.save(new Categoria("Trámites", "Gestiones para estudiantes Erasmus"));
        Categoria transporte   = categoriaRepository.save(new Categoria("Transporte", "Cómo moverse por la isla"));
        Categoria ocio         = categoriaRepository.save(new Categoria("Ocio", "Planes y actividades para el tiempo libre"));
        Categoria playas       = categoriaRepository.save(new Categoria("Playas", "Playas y zonas de baño"));
        Categoria restaurantes = categoriaRepository.save(new Categoria("Restaurantes", "Sitios para comer y cenar"));

        // ---------- Lugares (precios aproximados en euros; 0 = gratis) ----------
        // Las Palmas de Gran Canaria
        Lugar canteras = lugarRepository.save(new Lugar("Playa de Las Canteras", "Playa urbana con paseo marítimo y arrecife natural", 0.0, lasPalmas, playas));
        lugarRepository.save(new Lugar("Guaguas Municipales", "Autobuses urbanos de la ciudad; precio aproximado del billete sencillo", 1.4, lasPalmas, transporte));
        Lugar oriUlpgc = lugarRepository.save(new Lugar("Oficina de Relaciones Internacionales ULPGC", "Atención a estudiantes de intercambio de la Universidad de Las Palmas", 0.0, lasPalmas, tramites));
        lugarRepository.save(new Lugar("Mercado del Puerto", "Mercado con puestos de comida y tapas", 12.0, lasPalmas, restaurantes));

        // Santa Cruz de Tenerife
        Lugar teresitas = lugarRepository.save(new Lugar("Playa de Las Teresitas", "Playa de arena dorada junto a San Andrés", 0.0, santaCruz, playas));
        lugarRepository.save(new Lugar("Tranvía de Tenerife", "Une Santa Cruz con La Laguna; precio aproximado del billete", 1.35, santaCruz, transporte));
        lugarRepository.save(new Lugar("Parque García Sanabria", "Gran parque urbano en el centro de la ciudad", 0.0, santaCruz, ocio));

        // San Cristóbal de La Laguna
        lugarRepository.save(new Lugar("Oficina de Relaciones Internacionales ULL", "Atención a estudiantes de intercambio de la Universidad de La Laguna", 0.0, laLaguna, tramites));
        lugarRepository.save(new Lugar("Casco histórico de La Laguna", "Centro histórico Patrimonio de la Humanidad, ideal para pasear", 0.0, laLaguna, ocio));
        lugarRepository.save(new Lugar("Mercado Municipal de La Laguna", "Mercado con productos locales y zona de comidas", 8.0, laLaguna, restaurantes));

        // Puerto de la Cruz
        lugarRepository.save(new Lugar("Playa Jardín", "Playa de arena negra con jardines", 0.0, puerto, playas));
        lugarRepository.save(new Lugar("Lago Martiánez", "Complejo de piscinas de agua salada junto al mar", 6.0, puerto, ocio));
        lugarRepository.save(new Lugar("Loro Parque", "Parque zoológico y de animales marinos", 40.0, puerto, ocio));

        // ---------- Usuario de demostración con favoritos (relación N:M) ----------
        // Contraseña de 8+ caracteres para cumplir la validación del registro del frontend
        Usuario demo = new Usuario("Usuario Demo", "demo@erasmate.com", "erasmate2026");
        demo.addFavorito(canteras);
        demo.addFavorito(teresitas);
        demo.addFavorito(oriUlpgc);
        usuarioRepository.save(demo);

        System.out.println(">>> Datos de prueba cargados: 4 ciudades, 5 categorías, 13 lugares y 1 usuario demo");
    }
}