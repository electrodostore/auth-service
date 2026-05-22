package com.electrodostore.auth_service.security.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class KeyLoader {

    /**
     * Claves RSA usadas para firma y validación de JWT.
     *
     * - La clave privada firma tokens
     * - La clave pública valida firmas
     *
     * Los archivos PEM se leen y transforman
     * a objetos RSA utilizables por Java.
     */
    @Value("${jwt.private.key}")
    public Resource privateKeyResource;

    @Value("${jwt.public.key}")
    public Resource publicKeyResource;


    /**
     * Convierte el archivo PEM de clave privada
     * en un objeto RSAPrivateKey.
     *
     * PKCS8 es el formato estándar para claves privadas.
     */
    public RSAPrivateKey getPrivateKey() throws Exception {

        String key = new String(
                privateKeyResource.getInputStream().readAllBytes()
        );

        key = key
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        byte[] decoded = Base64.getDecoder().decode(key);

        PKCS8EncodedKeySpec spec =
                new PKCS8EncodedKeySpec(decoded);

        KeyFactory factory = KeyFactory.getInstance("RSA");

        return (RSAPrivateKey) factory.generatePrivate(spec);
    }

    /**
     * Convierte el archivo PEM de clave pública
     * en un objeto RSAPublicKey.
     *
     * X509 es el formato estándar para claves públicas.
     */
    public RSAPublicKey getPublicKey() throws Exception {

        String key = new String(
                publicKeyResource.getInputStream().readAllBytes()
        );

        key = key
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        byte[] decoded = Base64.getDecoder().decode(key);

        X509EncodedKeySpec spec =
                new X509EncodedKeySpec(decoded);

        KeyFactory factory = KeyFactory.getInstance("RSA");

        return (RSAPublicKey) factory.generatePublic(spec);
    }
}
