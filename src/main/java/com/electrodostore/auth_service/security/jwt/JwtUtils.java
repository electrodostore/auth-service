package com.electrodostore.auth_service.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

//Define utilidades u operaciones para gestionar el token JWT
@Component
public class JwtUtils {

    @Value("${jwt.user.generator}")
    private String userGenerator;

    private final KeyLoader keyLoader;

    public JwtUtils(KeyLoader keyLoader) {
        this.keyLoader = keyLoader;
    }

    /**
     * Crea y firma un token JWT usando RSA256.
     *
     * La firma se realiza con la clave privada RSA y posteriormente
     * puede validarse usando la clave pública correspondiente.
     */
    public String createToken(Authentication authentication) {

        Algorithm algorithm = Algorithm.RSA256(
                keyLoader.getPublicKey(),
                keyLoader.getPrivateKey()
        );

        String username = authentication.getName();

        List<String> authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        //Fecha base usada para emisión y expiración del token
        Instant now = Instant.now();

        return JWT.create()
                .withJWTId(UUID.randomUUID().toString())
                .withSubject(username)
                .withIssuer(userGenerator)
                .withIssuedAt(Date.from(now))
                .withExpiresAt(now.plus(Duration.ofMinutes(30)))
                .withClaim("authorities", authorities)
                .sign(algorithm);
    }
}
