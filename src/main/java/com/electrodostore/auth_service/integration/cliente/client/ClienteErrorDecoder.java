package com.electrodostore.auth_service.integration.cliente.client;

import com.electrodostore.auth_service.exception.ClienteNotFoundException;
import com.electrodostore.auth_service.exception.ErrorCode;
import com.electrodostore.auth_service.integration.common.ErrorBodyResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
/**
 * Interpreta respuestas de error provenientes de cliente-service
 * y transforma errores conocidos en excepciones de dominio
 * antes de que Feign lance su excepción genérica.
 */
@Slf4j
public class ClienteErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objMapper = new ObjectMapper();

    /**
     * Intenta traducir la respuesta de error de Feign a excepción
     * conocida de dominio, si no es posible retorna excepción Feign
     * predeterminada.
     */
    @Override
    public Exception decode(String methodKey, Response response) {
        try{
            //Lee y mapea el cuerpo de la respuesta a DTO de error
            InputStream bodyIn = response.body().asInputStream();
            ErrorBodyResponseDto error = objMapper.readValue(bodyIn, ErrorBodyResponseDto.class);

            //Solo interpretamos errores NOT_FOUND provenientes del servicio remoto
            if(response.status() == 404){

                // Intenta mapear la respuesta de error a excepción de negocio
                switch (ErrorCode.valueOf(error.getErrorCode())){

                    case CLIENT_NOT_FOUND:
                        return new ClienteNotFoundException(error.getMensaje());

                }
            }

            return FeignException.errorStatus(methodKey, response);

            //Si hay algún problema leyendo el cuerpo de la respuesta, se retorna excepción Feign original
        }catch(IOException ex){
            log.error("Problema leyendo el body de la response", ex);
            return FeignException.errorStatus(methodKey, response);
        }

    }
}
