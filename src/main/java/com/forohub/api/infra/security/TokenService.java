package com.forohub.api.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.forohub.api.domain.Usuario.Usuario;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    Dotenv dotenv = Dotenv.load();

    public String generarToken(String email){
        System.setProperty("JWT_FIRMA", dotenv.get("JWT_FIRMA"));
        System.setProperty("JWT_ISSUER", dotenv.get("JWT_ISSUER"));

        try {
            Algorithm algorithm = Algorithm.HMAC256("{JWT_FIRMA}");
            String token = JWT.create()
                    .withIssuer("{JWT_ISSUER}")
                    .withSubject(email)
                    .withExpiresAt(fechaExpiracion())
                    .sign(algorithm);

            return token;
        } catch (JWTCreationException exception){
            throw new RuntimeException("Error en la creacion de token JWT", exception);
        }
    }

    private Instant fechaExpiracion() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-06:00"));
    }

    public String getSubject(String tokenJWT){
        try {
            Algorithm algorithm = Algorithm.HMAC256("{JWT_FIRMA}");
            return  JWT.require(algorithm)
                    .withIssuer("{JWT_ISSUER}")
                    .build()
                    .verify(tokenJWT)
                    .getSubject();

        } catch (JWTVerificationException exception){
            throw new RuntimeException("Token JWT no valido o expirado", exception);
        }
    }
}
