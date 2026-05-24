package com.electrodostore.auth_service.integration.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
//DTO para mapear respuestas de error provenientes de otros servicios
public class ErrorBodyResponseDto {

    //Campos mínimos necesarios para interpretar el error remoto
    private String errorCode;
    private String mensaje;
}
