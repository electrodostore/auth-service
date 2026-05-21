package com.electrodostore.auth_service.dto;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

/**
 * DTO que define la estructura del cuerpo
 * de las excepciones personalizadas de la API
 */
public record ApiErrorResponse(
        LocalDateTime timestamp,
        Integer status,
        String error,
        String message
) {
}
