package com.erasmate.erasmate_backend.seguridad;

import com.erasmate.erasmate_backend.models.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey clave;
    private final long duracionMs;

    // Lee la clave secreta y los minutos de application.properties
    public JwtUtil(@Value("${jwt.secreto}") String secreto,
                   @Value("${jwt.expiracion-minutos}") long minutos) {
        this.clave = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secreto));
        this.duracionMs = minutos * 60 * 1000;
    }

    // Crea un token firmado con el id del usuario que caduca a los X minutos
    public String generarToken(Usuario usuario) {
        Date ahora = new Date();
        return Jwts.builder()
                .subject(String.valueOf(usuario.getIdUsuario()))
                .claim("email", usuario.getEmail())
                .issuedAt(ahora)
                .expiration(new Date(ahora.getTime() + duracionMs))
                .signWith(clave)
                .compact();
    }

    // Comprueba la firma y la caducidad, y devuelve el id del usuario.
    // Si el token es falso o ha caducado, lanza una excepción (JwtException)
    public Long obtenerIdUsuario(String token) {
        String id = Jwts.parser()
                .verifyWith(clave)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
        return Long.valueOf(id);
    }
}