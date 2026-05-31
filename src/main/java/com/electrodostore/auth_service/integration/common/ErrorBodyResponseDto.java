package com.electrodostore.auth_service.integration.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO para mapear respuestas de error provenientes de otros servicios
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//Ignora propiedades de la respuesta de error no definidas en este dominio
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorBodyResponseDto {

    private String errorCode;
    private String mensaje;
}
