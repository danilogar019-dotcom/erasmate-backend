package com.erasmate.erasmate_backend.seguridad;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.io.IOException;
import java.util.Map;

// Se ejecuta ANTES de los endpoints protegidos y comprueba el token
@Component
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    public JwtInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean preHandle(HttpServletRequest peticion, HttpServletResponse respuesta, Object handler)
            throws IOException {
        // La petición OPTIONS (CORS) la manda el navegador sin token: se deja pasar
        if ("OPTIONS".equalsIgnoreCase(peticion.getMethod())) return true;

        // El token llega en la cabecera "Authorization: Bearer <token>"
        String cabecera = peticion.getHeader("Authorization");
        if (cabecera == null || !cabecera.startsWith("Bearer ")) {
            return error(respuesta, 401, "Debes iniciar sesión.");
        }

        Long idToken;
        try {
            idToken = jwtUtil.obtenerIdUsuario(cabecera.substring(7));
        } catch (ExpiredJwtException e) {
            return error(respuesta, 401, "Tu sesión ha caducado. Vuelve a iniciar sesión.");
        } catch (JwtException | IllegalArgumentException e) {
            return error(respuesta, 401, "Sesión no válida. Vuelve a iniciar sesión.");
        }

        // El {id} de la URL tiene que ser el mismo usuario del token
        Map<String, String> variables =
                (Map<String, String>) peticion.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        if (variables != null && variables.get("id") != null
                && !variables.get("id").equals(String.valueOf(idToken))) {
            return error(respuesta, 403, "No puedes acceder a los datos de otro usuario.");
        }
        return true; // token correcto: sigue hacia el controlador
    }

    private boolean error(HttpServletResponse respuesta, int estado, String mensaje) throws IOException {
        respuesta.setStatus(estado);
        respuesta.setContentType("application/json;charset=UTF-8");
        respuesta.getWriter().write("{\"error\": \"" + mensaje + "\"}");
        return false; // corta la petición
    }
}