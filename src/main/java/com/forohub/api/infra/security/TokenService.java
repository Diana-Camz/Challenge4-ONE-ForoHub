package com.forohub.api.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    private final Algorithm algorithm;
    private final String issuer;

    public TokenService(@Value("${jwt.firma}") String firma, @Value("${jwt.issuer}") String issuer){
        this.algorithm = Algorithm.HMAC256(firma);
        this.issuer = issuer;
    };

    public String generarToken(String email){

        try {
            String token = JWT.create()
                    .withIssuer(issuer)
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
            return  JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build()
                    .verify(tokenJWT)
                    .getSubject();

        } catch (JWTVerificationException exception){
            throw new RuntimeException("Token JWT no valido o expirado", exception);
        }
    }
}
