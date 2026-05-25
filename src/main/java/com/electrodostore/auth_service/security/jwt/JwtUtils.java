package com.electrodostore.auth_service.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.electrodostore.auth_service.exception.UserNotFoundException;
import com.electrodostore.auth_service.model.UserSec;
import com.electrodostore.auth_service.repository.IUserRepository;
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

    private final IUserRepository userRepo;
    private final KeyLoader keyLoader;

    public JwtUtils(IUserRepository userRepo, KeyLoader keyLoader) {
        this.userRepo = userRepo;
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

        //Consulta datos del usuario
        UserSec user = userRepo.findByUsername(username)
                .orElseThrow(
                        () -> new UserNotFoundException("No se encontró usuario con username: " + username)
                );

        /**
         * Extrae la identidad del usuario para agregarla como claims al JWT.
         * Si el usuario está asociado a un cliente, también se incluye su clientId.
         */
        Long userId = user.getId();
        Long clientId = null;

        if(user.getClienteId() != null){clientId = user.getClienteId();}

        List<String> authorities = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        //Fecha base usada para emisión y expiración del token
        Instant now = Instant.now();

        return JWT.create()
                .withJWTId(UUID.randomUUID().toString())
                .withSubject(username)
                .withClaim("userId", userId)
                .withClaim("clientId", clientId)
                .withIssuer(userGenerator)
                .withIssuedAt(Date.from(now))
                .withExpiresAt(now.plus(Duration.ofMinutes(30)))
                .withClaim("authorities", authorities)
                .sign(algorithm);
    }
}
