package com.electrodostore.auth_service.integration.cliente.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//Configuración de Feign para la integración con cliente-service
@Configuration
public class ClienteFeignConfig {

    //Registra el ErrorDecoder encargado de interpretar errores remotos
    @Bean
    public ClienteErrorDecoder errorDecoder(){
        return new ClienteErrorDecoder();
    }
}