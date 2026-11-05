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

        // ---------- Ciudades ----------
        Ciudad lisboa = ciudadRepository.save(new Ciudad("Lisboa", "Portugal"));
        Ciudad roma   = ciudadRepository.save(new Ciudad("Roma", "Italia"));
        Ciudad paris  = ciudadRepository.save(new Ciudad("París", "Francia"));
        Ciudad berlin = ciudadRepository.save(new Ciudad("Berlín", "Alemania"));

        // ---------- Categorías ----------
        Categoria restaurantes = categoriaRepository.save(new Categoria("Restaurantes", "Sitios para comer y cenar"));
        Categoria ocio         = categoriaRepository.save(new Categoria("Ocio", "Planes, actividades y vida nocturna"));
        Categoria museos       = categoriaRepository.save(new Categoria("Museos", "Arte, historia y cultura"));
        Categoria transporte   = categoriaRepository.save(new Categoria("Transporte", "Cómo moverse por la ciudad"));
        Categoria tramites     = categoriaRepository.save(new Categoria("Trámites", "Gestiones para estudiantes Erasmus"));

        // ---------- Lugares (precios aproximados en euros) ----------
        // Lisboa
        Lugar timeOut = lugarRepository.save(new Lugar("Time Out Market", "Mercado gastronómico con puestos de cocina portuguesa", 15.0, lisboa, restaurantes));
        lugarRepository.save(new Lugar("Torre de Belém", "Torre defensiva del siglo XVI junto al río Tajo", 10.0, lisboa, museos));
        lugarRepository.save(new Lugar("Tarjeta Viva Viagem", "Tarjeta recargable para metro, autobús y tranvía", 0.5, lisboa, transporte));

        // Roma
        Lugar coliseo = lugarRepository.save(new Lugar("Coliseo", "Anfiteatro romano, símbolo de la ciudad", 18.0, roma, museos));
        lugarRepository.save(new Lugar("Trastevere", "Barrio con ambiente para salir por la noche", 0.0, roma, ocio));
        lugarRepository.save(new Lugar("Oficina de Relaciones Internacionales", "Punto de atención para estudiantes de intercambio", 0.0, roma, tramites));

        // París
        Lugar louvre = lugarRepository.save(new Lugar("Museo del Louvre", "Uno de los museos de arte más grandes del mundo", 22.0, paris, museos));
        lugarRepository.save(new Lugar("Torre Eiffel", "Vistas de toda la ciudad desde lo alto", 29.0, paris, ocio));
        lugarRepository.save(new Lugar("Pase Navigo", "Abono de transporte público para toda la región", 2.0, paris, transporte));

        // Berlín
        lugarRepository.save(new Lugar("East Side Gallery", "Tramo del Muro de Berlín cubierto de murales", 0.0, berlin, ocio));
        lugarRepository.save(new Lugar("Isla de los Museos", "Conjunto de cinco museos en el centro de la ciudad", 24.0, berlin, museos));
        lugarRepository.save(new Lugar("Markthalle Neun", "Mercado cubierto con comida de todo el mundo", 12.0, berlin, restaurantes));

        // ---------- Usuario de demostración con favoritos (relación N:M) ----------
        Usuario demo = new Usuario("Usuario Demo", "demo@erasmate.com", "1234");
        demo.addFavorito(timeOut);
        demo.addFavorito(coliseo);
        demo.addFavorito(louvre);
        usuarioRepository.save(demo);

        System.out.println(">>> Datos de prueba cargados: 4 ciudades, 5 categorías, 12 lugares y 1 usuario demo");
    }
}