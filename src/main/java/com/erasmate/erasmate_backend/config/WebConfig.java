package com.erasmate.erasmate_backend.config;

import com.erasmate.erasmate_backend.seguridad.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JwtInterceptor jwtInterceptor;

    public WebConfig(JwtInterceptor jwtInterceptor) {
        this.jwtInterceptor = jwtInterceptor;
    }

    // Rutas que necesitan token: los datos del usuario y sus favoritos.
    // Lugares, ciudades, categorías, registro y login siguen siendo públicos.
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/api/usuarios/*", "/api/usuarios/*/favoritos", "/api/usuarios/*/favoritos/*")
                .excludePathPatterns("/api/usuarios/login");
    }
}
