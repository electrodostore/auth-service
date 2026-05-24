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

    //Objeto usado para deserializar el body de la response de error
    private final ObjectMapper objMapper = new ObjectMapper();

    /**
     * Retorna la excepción que Feign propagará al método que realizó la petición.
     *
     * - Si el error remoto es reconocido, se transforma en una excepción de dominio.
     * - Si el error no puede interpretarse, se retorna la excepción genérica de Feign.
     *
     * methodKey -> nombre del método FeignClient que realizó la petición
     * response  -> response HTTP completa retornada por el servicio remoto
     */
    @Override
    public Exception decode(String methodKey, Response response) {
        try{

            //Obtiene el body de la response para deserializarlo
            InputStream bodyIn = response.body().asInputStream();

            //Reconstruye el body JSON y lo transforma al DTO de error
            ErrorBodyResponseDto error = objMapper.readValue(bodyIn, ErrorBodyResponseDto.class);

            //Solo interpretamos errores NOT_FOUND provenientes del servicio remoto
            if(response.status() == 404){

                //Identifica el recurso que no fue encontrado a partir del errorCode recibido
                switch (ErrorCode.valueOf(error.getErrorCode())){

                    //Transforma el error remoto en una excepción de dominio local
                    case CLIENT_NOT_FOUND:
                        return new ClienteNotFoundException(error.getMensaje());

                    /*
                     * Actualmente cliente-service solo expone errores
                     * relacionados con clientes no encontrados.
                     */
                }
            }

            //Si el error no puede interpretarse, se retorna la excepción genérica de Feign
            return FeignException.errorStatus(methodKey, response);

        }catch(IOException ex){

            //Error al leer o deserializar el body de la response
            log.error("Problema leyendo el body de la response", ex);

            return FeignException.errorStatus(methodKey, response);
        }

    }
}
