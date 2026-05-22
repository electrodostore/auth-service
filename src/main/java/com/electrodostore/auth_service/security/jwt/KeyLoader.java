package com.electrodostore.auth_service.security.jwt;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
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
    private Resource privateKeyResource;

    @Value("${jwt.public.key}")
    private Resource publicKeyResource;

    @Getter
    private RSAPrivateKey privateKey;
    @Getter
    private RSAPublicKey publicKey;

    /**
     * Carga y transforma las claves PEM a objetos RSA
     * después de inicializar el bean.
     */
    @PostConstruct
    public void init(){

        try{
            //Carga de clave privada RSA (PKCS8)
            KeyFactory factory = KeyFactory.getInstance("RSA");

            String privateKeyContent = new String(
                    privateKeyResource.getInputStream().readAllBytes()
            );

            privateKeyContent = privateKeyContent
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] decodedPrivateKey = Base64.getDecoder().decode(privateKeyContent);

            PKCS8EncodedKeySpec spec =
                    new PKCS8EncodedKeySpec(decodedPrivateKey);

            this.privateKey= (RSAPrivateKey) factory.generatePrivate(spec);

            //carga de clave pública RSA (X509)
            String publicKeyContent = new String(
                    publicKeyResource.getInputStream().readAllBytes()
            );

            publicKeyContent = publicKeyContent
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] decodedPublicKey = Base64.getDecoder().decode(publicKeyContent);

            X509EncodedKeySpec specPublic =
                    new X509EncodedKeySpec(decodedPublicKey);

            this.publicKey = (RSAPublicKey) factory.generatePublic(specPublic);


        }catch (Exception ex){
            throw new IllegalStateException(
                    "error cargando clave RSA", ex
            );
        }
    }

}
